# discord-bot

## Getting Started
### Install Java (JDK) & Set-up Environmental Variables (windows)
```shell
# Set Environmental Variables - JDK
Navigate to environmental variables -> system variables (bottom pane) -> left-click path -> edit -> new -> paste in your path (example: C:\Program Files\Java\YOUR_JDK_HERE\bin)

# Set JAVA_HOME
Navigate back to System variables -> new -> variable name is JAVA_HOME -> variable value is C:\Program Files\Java\YOUR_JDK_HERE
  ```
### Install Java (JDK) & Set-up Environmental Variables (windows)
```shell
coming soon
```

### Cloning this Repository
```shell
# Clone to your machine
git clone https://github.com/MitchellTFord/discord-bot.git

# Change to the new directory
cd discord-bot
```

### Building the Application
```shell
# Linux and Mac
./gradlew build

# Windows
.\gradlew build
```

### Running the Application
```shell
# Linux and Mac
./gradlew bootRun

# Windows
.\gradlew bootRun
```

### Generate & View Code Coverage Report Using JaCoCo
```shell
# Run unit tests and generate report
./gradlew test

# Generate report without re-running tests
./gradlew jacocoTestReport

# You can view the report under build/jacoco/test/html/index.html
```

## Resources
### Recommended Software
* [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* [GitHub Desktop](https://desktop.github.com/) (recommended but not required)

### Git and GitHub
* [Git Guide](https://docs.github.com/en/get-started/using-git/about-git)
* [GitHub Flow Guide](https://docs.github.com/en/get-started/quickstart/github-flow)
* [Git Download](https://git-scm.com/downloads)

### Java Development
* [Baeldung's Java “Back to Basics” Tutorial](https://www.baeldung.com/java-tutorial)
* [Baeldung's Java Object-Oriented Programming Guide](https://www.baeldung.com/java-oop)
* [Baeldung's "Introduction to Gradle" Guide](https://www.baeldung.com/gradle)

### Spring and Spring Boot
* [Spring's Website](https://spring.io/)
* [Baeldung's Guide to Dependency Injection using Spring](https://www.baeldung.com/spring-dependency-injection)
* [Baeldung's Buide to Spring Boot](https://www.baeldung.com/spring-boot)

### Java 17 Installation
* [Download here - Oracle](https://www.oracle.com/java/technologies/downloads/\#jdk17-linux)