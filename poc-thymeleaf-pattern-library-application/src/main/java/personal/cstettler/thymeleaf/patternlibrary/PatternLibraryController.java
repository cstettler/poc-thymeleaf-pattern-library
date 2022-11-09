package personal.cstettler.thymeleaf.patternlibrary;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PatternLibraryController {

  private static final String ROOT_GROUP_ID = "/";

  private final String applicationName;
  private final List<ComponentGroup> componentGroups;
  private final ComponentGroup rootComponentGroup;

  PatternLibraryController(
    @Value("${pattern-library.application-name}") String applicationName,
    @Value("${pattern-library.components-resource-path}") String componentsResourcePath
  ) throws Exception {
    this.applicationName = applicationName;

    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
    Resource componentResourcesRoot = resourcePatternResolver.getResource(componentsResourcePath);

    if (!componentResourcesRoot.exists()) {
      throw new IllegalStateException("components resource path '" + componentsResourcePath + "' does not exist");
    }

    Resource indexDocumentationResource = resourcePatternResolver.getResource(componentsResourcePath + "/index.md");

    if (!indexDocumentationResource.exists()) {
      throw new IllegalStateException("no index.md found in folder " + uriFor(componentResourcesRoot));
    }

    Resource[] componentResources = resourcePatternResolver.getResources(componentsResourcePath + "/**/*");
    componentGroups = collectComponentGroups(componentResourcesRoot, componentResources);
    rootComponentGroup = componentGroupFor(ROOT_GROUP_ID).orElseThrow(() -> new IllegalStateException("root component group not found"));
  }

  @GetMapping("/")
  public ModelAndView index(@RequestParam(name = "id", required = false) String groupId) {
    return componentGroupFor(groupId != null ? groupId : ROOT_GROUP_ID)
      .map(componentGroup -> new ModelAndView("pattern-library/component-group", model(componentGroup)))
      .orElse(new ModelAndView("pattern-library/error"));
  }

  @GetMapping("/example")
  public ModelAndView example(@RequestParam String title, @RequestParam String templatePath) {
    return new ModelAndView("pattern-library/example-container", model(title, templatePath));
  }

  private Optional<ComponentGroup> componentGroupFor(String groupId) {
    return componentGroups.stream()
      .filter(componentGroup -> componentGroup.getId().equals(groupId))
      .findFirst();
  }

  private Map<String, Object> model(ComponentGroup componentGroup) {
    Map<String, Object> model = new HashMap<>();
    model.put("applicationName", applicationName);
    model.put("rootComponentGroup", rootComponentGroup);
    model.put("componentGroup", componentGroup);

    return model;
  }

  private static Map<String, String> model(String title, String templatePath) {
    return Map.of(
      "title", title,
      "templatePath", templatePath
    );
  }

  private static List<ComponentGroup> collectComponentGroups(Resource componentResourcesRoot, Resource[] componentResources) {
    List<ComponentGroup> componentGroups = buildComponentGroups(componentResourcesRoot, componentResources);
    buildComponentGroupHierarchy(componentGroups);

    return componentGroups;
  }

  private static List<ComponentGroup> buildComponentGroups(Resource componentResourcesRoot, Resource[] componentResources) {
    Map<String, List<Resource>> componentResourcesByGroupId = stream(componentResources)
      .reduce(new HashMap<>(), (index, componentResource) -> {
        String groupId = groupIdFor(componentResourcesRoot, componentResource);
        index.computeIfAbsent(groupId, (key) -> new ArrayList<>()).add(componentResource);
        return index;
      }, (a, b) -> a);

    return componentResourcesByGroupId.entrySet().stream()
      .map(groupIdAndComponentResources -> new ComponentGroup(
        groupIdAndComponentResources.getKey(),
        groupIdAndComponentResources.getValue()
      ))
      .collect(toList());
  }

  private static void buildComponentGroupHierarchy(List<ComponentGroup> componentGroups) {
    componentGroups.forEach(componentGroup -> findParent(componentGroups, componentGroup)
      .ifPresent(parentComponentGroup -> parentComponentGroup.addSubGroup(componentGroup)));
  }

  private static String groupIdFor(Resource componentResourcesRoot, Resource componentResource) {
    String relativePath = relativePathFor(componentResourcesRoot, componentResource);

    if (relativePath.endsWith(ROOT_GROUP_ID)) {
      return relativePath;
    }

    return relativePath.substring(0, relativePath.lastIndexOf(ROOT_GROUP_ID) + 1);
  }

  private static String parentGroupIdFor(ComponentGroup componentGroup) {
    String[] groupIdSegments = componentGroup.getId().split(ROOT_GROUP_ID);

    return stream(groupIdSegments)
      .limit(groupIdSegments.length - 1)
      .collect(joining(ROOT_GROUP_ID, "", ROOT_GROUP_ID));
  }

  private static Optional<ComponentGroup> findParent(List<ComponentGroup> componentGroups, ComponentGroup componentGroup) {
    if (componentGroup.isRoot()) {
      return Optional.empty();
    }

    return componentGroups.stream()
      .filter(currentComponentGroup -> currentComponentGroup.getId().equals(parentGroupIdFor(componentGroup)))
      .findFirst();
  }

  private static String relativePathFor(Resource componentResourcesRoot, Resource componentResource) {
    String componentResourcesRootUri = uriFor(componentResourcesRoot);
    String componentResourcesUri = uriFor(componentResource);

    return componentResourcesUri.substring(componentResourcesRootUri.length());
  }

  private static String uriFor(Resource resource) {
    try {
      return resource.getURI().toString();
    } catch (IOException e) {
      throw new IllegalStateException("unable to get uri for resource '" + resource + "'", e);
    }
  }
}
