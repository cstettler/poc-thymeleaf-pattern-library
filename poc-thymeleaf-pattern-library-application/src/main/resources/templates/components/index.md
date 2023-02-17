# PoC for Thymeleaf-based Pattern Library

This PoC demonstrates the use of UI components defined as Thymeleaf fragments.

The Thymeleaf templates and the corresponding JS/CSS assets have been bundled as a Jar file and integrated into the pattern library application.

## Credits

The fundamental idea if this pattern library application is based on <a href="https://github.com/joyheron/spring-boot-pattern-library" target="_blank">https://github.com/joyheron/spring-boot-pattern-library</a>.

## Defining Components

## Showcasing Components

Components can be demonstrated and documented via multiple showcases, organized in component groups.
Component groups and showcases reside in the showcase root folder `poc-thymeleaf-pattern-library-application/src/main/resources/templates/components`.

The structure is built by component groups that may contain a main documentation and multiple showcases, optionally with their own documentation.
The folder and file structure in the showcase root directory is used to automatically render the navigation in the pattern library application and the component group pages.

### Component Groups

A component group is defined by a folder in the showcase.
Component groups can be nested to build a deep hierarchy.

By default, the name of a component group is derived from the folder name.

#### Ordering
The ordering of the component groups on the same level is based on the alphabetical sorting of the folder name.
The ordering can be customized by adding an ordering prefix (e.g. `01-`) to a folder name.
If added, the ordering prefix is automatically stripped of the folder name when deriving the component group name.

#### Main Documentation
Each component group may contain a main documentation.
The main documentation is rendered at the top of the corresponding component group page.

The main documentation is identified by a Markdown file with the same basename as the folder defining the component group (e.g. `my-group.md` for the component group `my-group`).
If the folder contains an ordering prefix, the main documentation file may or may not use the ordering prefix (e.g. both `01-my-group.md` and `my-group.md` are valid main documentation files for the component group `01-my-group`, with preference for the former).

If a component group contains a main documentation, the component group name is defined by the title in the main documentation, if available.

### Showcases

A showcase is defined by an HTML file (e.g. `my-showcase.html`) that is used to render the showcase.
A showcase may have an optional showcase documentation as a Markdown file having the same basename (e.g. `my-showcase.md`).

By default, the name of a showcase is derived from the file name.

#### Ordering
The ordering of the showcases within a component group is based on the alphabetical sorting of the file name.
The ordering can be customized by adding an ordering prefix (e.g. `01-`) to a file name.
If added, the ordering prefix is automatically stripped of the file name when deriving the showcase name.

#### Documentation
Each showcase may have its own documentation.
This documentation is rendered above the corresponding showcase on the component group page.

The documentation of a showcase is identified by a Markdown file with the same basename as the file defining the showcase (e.g. `my-showcase.md` for the showcase `my-shwocase.html`).
If the showcase file contains an ordering prefix, the documentation file may or may not use the ordering prefix (e.g. both `01-my-showcase.md` and `my-showcase.md` are valid documentation files for the showcase `01-my-showcase`, with preference for the former).

If a showcase contains a documentation, the showcase name is defined by the title in the documentation, if available.
