# cscd350-f24-UnhandledExceptions

## About this project
This is a Java-based UML (Unified Modeling Language) editor that supports both a command line interface (CLI) or a graphical user interface (GUI). This tool allows users to create, edit, and save UML diagrams with ease, making it easy for software developers and designers to visualize their designs. We wanted to create flexibility with our program, so we made it easy to transition from working in the CLI to the GUI and vice versa via save files. 

## Access/view the Code-Cov report
- First, cd into the uml directory from the terminal.
- Next, run 'mvn clean install' through the terminal.
- After the clean install, run 'mvn jacoco:report' for the most up to date report.
- Lastly, to access the report: from uml you want to access the directory'/target/site\jacoco/index.html' which will open a window giving you the latest Code-cov report.

### Requirements to get started

- Java 21+ [Installation](https://www.java.com/en/download/help/download_options.html)
    * Note: to ensure you have Java installed, run `java --version`.
- JavaFX [Installation](https://openjfx.io/)

### Downloading and running

1. Download uml.jar found in the root directory of this repository.
2. Once downloaded, go to the directory containing uml.jar in your terminal
3. run the command `java -jar uml.jar` to run the program.
