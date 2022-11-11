# PoC for Thymeleaf Pattern Library

Demonstrates how UI components can be built based on Thymeleaf fragments and be show-cased in a pattern library application.

## Goal
- support definition of UI components based on Thymeleaf
- keep all required resources (HTML template, JavaScript, CSS, images) for the component within the same directory
- provide smooth developer experience with IDE support and live reload
- keep technology stack and build pipeline simple
- support simple integration of UI components into Spring Boot application

## Features
- define UI components using Thymeleaf fragments
- use Post-CSS in UI components
- use JavaScript in UI components
- use images in UI components (currently only png)
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
- use resulting jar file in application

## Issues
- examples template resolution not working when running built application via `java -jar ...`

## Improvements
- support component dialect when using UI components (e.g. https://github.com/Serbroda/thymeleaf-component-dialect or https://github.com/blynx/thymeleaf-fertilizer-dialect-playground)
- use fingerprinting and caching provided by Spring Boot
- support component documentation as part of UI component module (<component>.md)
- demonstrate unit testing of UI components
- demonstrate integration testing of UI components via pattern library application (e.g. using Playwright)
- use more specific root folder for UI components (to avoid collisions on class path)

# Learnings
- templates need to be referred to without leading slash, otherwise template cannot be found when running application from jar (probably due to different classloader hierarchy in fat jar)