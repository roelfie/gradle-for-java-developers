# Gradle for Java Developers

In the root directory of the project (where the build.gradle resides):

```shell script
gradle build
```

## Groovy closures

Example of a build.gradle:    

```groovy
plugins {
    id 'java'
}

group 'top.kerstholt.gradle'
version '1.0.0'

sourceCompatibility = 1.11

repositories {
    mavenCentral()
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'
    compile 'com.google.code.gson:gson:2.8.0'
}
```

All the constructs of the form

```groovy
someProperty {
    something "a"
    anotherThing "b"
}
```

are actually Groovy closures being passed in to a method.

## POM

The Groovy POM (project object model):

- Project
  - Tasks (>=0 per project)
    - Actions (>=0 per task) the functions performed by Gradle

Gradle defines an implicit variable 'project'. 
All properties in build.gradle without a prefix are properties of the Project.
So in the above example we might as well use:

```groovy
project.plugins
project.group
project.version
project.sourceCompatibility
project.repositories
project.dependencies
```

For the complete list of 'project' properties, take a look at the 
[Gradle documentation](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html) 
or do

```shell script
$ gradle properties
```

## Tasks

The tasks provided by Gradle

```shell script
$ gradle tasks
```

### Common tasks

```shell script
$ gradle help [--task <task>]
$ gradle properties
$ gradle tasks

$ gradle build
$ gradle test

$ gradle dependencies [--configuration compile|testRuntimeClasspath]
```
Use `-q` (quiet) for less verbose output.

To obtain an HTML report of the dependency trees, add plugin `project-report` to build.gradle, and run:

```shell script
$ gradle htmlDependencyReport
```
#### Logging & Build lifecycle

Gradle has a maven-like [build lifecycle](https://docs.gradle.org/current/userguide/build_lifecycle.html). 

Log levels are (in order):
1. ERROR
2. QUIET
3. WARNING
4. LIFECYCLE (default)
5. INFO
6. DEBUG

```shell script
$ gradle [-q|-w|-i|-d] <task>
```

With the default log level [LIFECYCLE](https://docs.gradle.org/current/userguide/logging.html) you can supposedly 
see lifecycle progress information messages, but I do not (gradle 6.1.1). On log level INFO you do see lifecycle information:

```shell script
$ gradle [-i|--info] <task>
```

## Java plugin

In the example `build.gradle` there's an import of the 'java' [plugin](https://docs.gradle.org/current/userguide/java_plugin.html).
This plugin adds extra tasks to gradle: 

```shell script
$ gradle clean     
$ gradle classes       # compiles Java classes and copies production resources
$ gradle testClasses   # compiles test Java classes and copies test resources
$ gradle test          # depends on testClasses and runs all JUnit tests
$ gradle jar           # 
$ gradle etc...
```

## Dependency management

A dependency on a gradle submodule is added like this:

```groovy
dependencies {
    compile project(':json-display')
}
```

## Project structure

Overview of submodules in a multi-module gradle project: 
```groovy
$ gradle -q projects
```

#### Gradle wrapper (gradlew)

To enforce a uniform way of building your gradle project across environments, you can use the gradle wrapper.
The gradle wrapper is added to your project by `gradle init`.

```shell script
<project>
 |__ gradle
 |   |__ wrapper
 |       |__ gradle-wrapper.jar          # implementation (checks version & downloads gradle)
 |       |__ gradle-wrapper.properties   # specifies what gradle version to use 
 |__ gradlew                             # bash script around the wrapper
 |__ gradlew.bat                         # (Windows version)
```

The gradle wrapper `gradlew`
- checks that the requested version of gradle is installed
- if not, downloads and installs that version
- passes the commands on to the real gradle

If there is no gradle wrapper in your project, you can add one using 
```shell script
$ gradle wrapper --gradle-version 6.1.1
```

:warning: Make sure to always check in `gradlew` and the `gradle` directory into vcs.

## Writing Custom Tasks

The following will add a task named `showDate` to `build.gradle`:
```shell script
task showDate {
    doLast {
        group = 'My tasks'
        description = 'Show current date'
        println 'Current date: ' + new Date()
    }
}
```

This can be executed as follows: 
```shell script
$ gradle -q showDate
Current date: Fri Feb 07 14:22:25 CET 2020
```

:warning: If you don't define a `group` then `gradle tasks` will show this task only if you add the `--all` flag,
and in IntelliJ the task will appear in the 'other' group. 


