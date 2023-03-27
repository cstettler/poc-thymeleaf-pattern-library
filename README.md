# PoC for Thymeleaf Pattern Library

Demonstrates how UI components can be built based on Thymeleaf fragments and be show-cased in a pattern library application.
It also provides a custom Thymeleaf dialect that simplifies the use of Thymeleaf fragments in a more component-style.

## Credits

The fundamental idea of the pattern library application is based on https://github.com/joyheron/spring-boot-pattern-library</a>.

The custom Thymeleaf dialect mimics ideas taken from https://github.com/Serbroda/thymeleaf-component-dialect and https://github.com/blynx/thymeleaf-fertilizer-dialect-playground.

## Goal

- support definition of UI components based on Thymeleaf
- keep all required resources (HTML template, JavaScript, CSS, images) for the component within the same directory
- provide smooth developer experience with IDE support and live reload
- keep technology stack and build pipeline simple
- support simple integration of UI components into Spring Boot application
- support convenient use of Thymeleaf fragments as components

## Features

- define UI components using Thymeleaf fragments
- use Post-CSS in UI components
- use JavaScript in UI components
- use images in UI components (currently only png)
- use Thymeleaf fragments as components with help of the custom dialect
- live reload when editing UI components or pattern library application


## Approach

- the UI components and pattern library application form a separate project, built with Maven
- all UI components are developed in a separate Maven module
- a UI component consists of a Thymeleaf template, plus optional JavaScript code, CSS, and images
- all JavaScript and CSS code is bundled using vite/rollup
- additional assets are copied using rollup-copy-plugin
- the UI components module provides a jar file containing all Thymeleaf templates and additional assets (bundled JS/CS, images) ready to be consumed / served by a Spring Boot application
  - templates are in the default `/templates` directory used by Thymeleaf
  - bundled JS/CS and images are in the default `/static` directory used by Spring Boot to serve static content
- the pattern library application is a normal Spring Boot application in a separate Maven module
- the pattern library application defines a Maven dependency to the UI components jar file
- the UI components are being developed while being show-cased in the pattern library application
- the custom Thymeleaf dialect is developed in a separate Maven module, provided as a separate jar file
- the pattern library application defines a Maven dependency to the custom Thymeleaf dialect jar file


## Developer Workflow

- start pattern library application in IDE(A)
- start watcher on UI components using `npm run watch` in directory `poc-thymeleaf-pattern-library-components`
- open browser and navigate to `http://localhost:9090/`
- edit the Thymeleaf template of a UI component, then press `CTRL + F9` and see changes in browser (without reload)
- edit the JavaScript or CSS resource of a UI component, then press `CMD + S` and see changes in browser (without reload)
- edit the Thymeleaf template of the pattern application itself, then press `CTRL + F9`  and see changes in browser (without reload)

Hint: remap `CMD + S` to `Make Project` to consistently use same command for triggering update)


## Production Build

- execute `./mvnw clean install`
- start pattern library application via `java -jar poc-thymeleaf-pattern-library-application/target/poc-thymeleaf-pattern-library-application-0.0.1-SNAPSHOT.jar`
- use resulting jar file `poc-thymeleaf-pattern-library-components/target/poc-thymeleaf-pattern-library-components-0.0.1-SNAPSHOT.jar` (contains all UI components) as dependency in application
- use resulting jar file `poc-thymeleaf-pattern-library-dialect/target/poc-thymeleaf-pattern-library-dialect-0.0.1-SNAPSHOT.jar` (contains the custom Thymeleaf dialect) as dependency in application


## Improvement Ideas

### Application
- use fingerprinting and caching provided by Spring Boot
- support component documentation as part of UI component module (<component>.md)
- demonstrate integration testing of UI components via pattern library application (e.g. using Playwright)
- support / demonstrate bundling and tree-shaking in consuming application

### Components
- demonstrate unit testing of UI components
- add versioning to component library
- add version-based scoping for css selectors
- page objects for Playwright as part of component test root

### Dialect
- support arbitrary attributes on dialect components (pass onto "root" element, or first, if no root defined)
- introduce list component with complex model object
- support strategy for registering components (with static and classpath-based implementation)
