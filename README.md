# cscd350-f24-UnhandledExceptions

## About this project
This is a Java-based UML (Unified Modeling Language) editor that supports both a command line interface (CLI) or a graphical user interface (GUI). This tool allows users to create, edit, and save UML diagrams with ease, making it easy for software developers and designers to visualize their designs. We wanted to create flexibility with our program, so we made it easy to transition from working in the CLI to the GUI and vice versa via save files. 

### Note: Currently does not work on Linux operating systems.

### Access/view the Code-Cov report
In order to generate the CodeCov report to view code coverage, the following is needed on your system:
   - [Maven 3+](https://maven.apache.org/)
   - [Live Server](https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer) Visual Studio Code extension by Ritwick Dey or any other way of opening/viewing html files. 
### Steps:
   1. Download or clone the entire repository.
   2. In the terminal, ensure your working directory is `cscd350-f24-UnhandledExceptions/uml` by using the `cd` command.
   3. Run `mvn clean install` through the terminal.
   4. Run `mvn jacoco:report` to generate the report.
   5. Open the file: `cscd350-f24-UnhandledExceptions/uml/target/site/jacoco/index.html` using the live server extension or alternative html viewing method to view the report.

### Requirements to use the program

- Java 21+ [Installation](https://www.java.com/en/download/help/download_options.html)
    * Note: to ensure you have Java installed, run `java --version`.
- JavaFX [Installation](https://openjfx.io/)

### Downloading and running the program

1. Download uml.jar found in the root directory of this repository.
2. Once downloaded, go to the directory containing uml.jar in your terminal
3. run the command `java -jar uml.jar` to run the program.
