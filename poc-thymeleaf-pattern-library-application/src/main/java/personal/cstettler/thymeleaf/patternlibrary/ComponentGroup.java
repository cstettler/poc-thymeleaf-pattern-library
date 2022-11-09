package personal.cstettler.thymeleaf.patternlibrary;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

public class ComponentGroup {

  private static final Parser PARSER = Parser.builder().build();
  private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();

  private final String id;
  private final List<Resource> resources;
  private final List<ComponentGroup> subGroups;

  public ComponentGroup(String id, List<Resource> resources) {
    this.id = id;
    this.resources = resources;
    this.subGroups = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    if (hasDocumentation()) {
      String mainDocumentationTitle = titleFrom(getDocumentation());

      if (mainDocumentationTitle != null) {
        return mainDocumentationTitle;
      }
    }

    return isRoot() ? null : capitalized(localId());
  }

  public boolean hasDocumentation() {
    if (isRoot()) {
      return hasResource("index.md");
    }
    return hasResource(localId() + ".md");
  }

  public String getDocumentationContent() {
    return render(stripTitle(extractResource(getDocumentation())));
  }

  public boolean hasExample() {
     return isRoot() ? false : hasResource(localId() + ".html");
  }

  public String getExamplePath() {
    return id + "/" + localId();
  }

  public String getExampleContent() {
    return extractResource(resourceFor(localId() + ".html"));
  }

  public boolean hasSubGroups() {
    return !subGroups.isEmpty();
  }

  public List<ComponentGroup> getSubGroups() {
    return subGroups;
  }

  public List<ShowCase> getShowCases() {
    return resources.stream()
      .filter(ComponentGroup::isExample)
      .filter(example -> !isMainExample(example))
      .map(example -> new ShowCase(id + "/" + baseName(example), example, lookupDocumentationFor(example)))
      .collect(toList());
  }

  void addSubGroup(ComponentGroup componentGroup) {
    subGroups.add(componentGroup);
  }

  boolean isRoot() {
    return id.equals("/");
  }

  private Resource getDocumentation() {
    if (isRoot()) {
      return resourceFor("index.md");
    }
    return resourceFor(localId() + ".md");
  }

  private Resource lookupDocumentationFor(Resource example) {
    return resources.stream()
      .filter(ComponentGroup::isDocumentation)
      .filter(documentation -> baseName(documentation).equals(baseName(example)))
      .filter(documentation -> !isMainDocumentation(documentation))
      .findFirst()
      .orElse(null);
  }

  private String localId() {
    String[] idSegments = id.split("/");
    String localId = idSegments[idSegments.length - 1];

    return localId;
  }

  private boolean isMainDocumentation(Resource resource) {
    return resource.getFilename().equals(localId() + ".md");
  }

  private boolean isMainExample(Resource resource) {
    return resource.getFilename().equals(localId() + ".html");
  }

  private static boolean isExample(Resource resource) {
    return resource.getFilename().endsWith(".html");
  }

  private static boolean isDocumentation(Resource resource) {
    return resource.getFilename().endsWith(".md");
  }

  private static String capitalized(String rawName) {
    return stream(rawName.split("[ -]"))
      .map(StringUtils::capitalize)
      .collect(joining(" "));
  }

  private static String baseName(Resource resource) {
    return resource.getFilename().substring(0, resource.getFilename().lastIndexOf("."));
  }

  private boolean hasResource(String filename) {
    return resources.stream().anyMatch(resource -> resource.getFilename().equals(filename));
  }

  private Resource resourceFor(String filename) {
    return resources.stream()
      .filter(resource -> resource.getFilename().equals(filename))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("no resource found with filename '" + filename + "'"));
  }

  private static String render(String markdown) {
    return HTML_RENDERER.render(PARSER.parse(markdown));
  }

  private static String extractResource(Resource resource) {
    if (resource == null) {
      return null;
    }

    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new IllegalStateException("unable to extract resource '" + resource + "'");
    }
  }

  public static String markdownToHTML(String markdown) {
    return HTML_RENDERER.render(PARSER.parse(markdown));
  }

  private static String titleFrom(Resource documentation) {
    String mainDocumentationSource = extractResource(documentation);
    List<String> allLines = readAllLines(mainDocumentationSource);

    if (!allLines.isEmpty() && allLines.get(0).startsWith("# ")) {
      return allLines.get(0).substring(2);
    }

    return null;
  }

  private static String stripTitle(String markdownSource) {
    if (markdownSource.startsWith("# ")) {
      return skipLines(markdownSource, 1);
    }

    return markdownSource;
  }

  private static String skipLines(String markdownSource, int numberOfLinesToSkip) {
    List<String> allLines = readAllLines(markdownSource);
    return allLines.subList(numberOfLinesToSkip, allLines.size()).stream().collect(joining("\n"));
  }

  private static List<String> readAllLines(String markdownSource) {
    try (BufferedReader reader = new BufferedReader(new StringReader(markdownSource))) {
      List<String> result = new ArrayList<>();
      String line;

      while ((line = reader.readLine()) != null) {
        result.add(line);
      }

      return result;
    } catch (IOException e) {
      throw new IllegalStateException("failed to read lines from string '" + markdownSource + "'", e);
    }
  }

  public static class ShowCase {

    private final String id;
    private final Resource example;
    private final Resource documentation;

    public ShowCase(String id, Resource example, Resource documentation) {
      this.id = id;
      this.example = example;
      this.documentation = documentation;
    }

    public String getId() {
      return id;
    }

    public String getName() {
      if (documentation != null) {
        String documentationTitle = titleFrom(documentation);

        if (documentationTitle != null) {
          return documentationTitle;
        }
      }

      return stream(example.getFilename().split("-"))
        .map(StringUtils::capitalize)
        .collect(joining(" "));
    }

    public boolean hasExample() {
      return example != null;
    }

    public String getExample() {
      return extractResource(example);
    }

    public boolean hasDocumentation() {
      return documentation != null;
    }

    public String getDocumentation() {
      return markdownToHTML(stripTitle(extractResource(documentation)));
    }
  }
}
