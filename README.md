# Gradle for Java Developers

## 1. Introduction

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

### Groovy closures
All the constructs of the form

```groovy
someProperty {
    something "a"
    anotherThing "b"
}
```

are actually Groovy closures being passed in to a method.

### Submodules

To define a gradle submodule:
* create a new gradle project in subdirectory `my-sub-module`
* include it in setings.gradle: `include 'my-sub-module'`
* add a dependency in build.gradle: `compile project(':my-sub-module')`

This prints the structure of a multi-module gradle project: 
```groovy
$ gradle -q projects
```

## 2. POM

The Gradle POM (project object model):

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

## 3. Tasks

List (all) tasks:

```shell script
$ gradle tasks [--all]
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

## 4. Java plugin

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


## 5. Writing Custom Tasks

You can define custom tasks in `build.gradle`. 

Custom task written in DSL:

```shell script
task showDate {
    dependsOn build

    group = 'My custom tasks'
    description = 'Show current date'

    doLast {
        println 'Current date: ' + new Date()
    }
}
```

Custom task written in Groovy: 
```shell script
class HelloWorld extends DefaultTask {
    @Override
    String getGroup() { return "My custom tasks" }

    @Override
    String getDescription() { return "Hello World" }

    @TaskAction
    void hello() { println "Hello World!" }
}

task hello(type: HelloWorld)
```

Listing all tasks in a particular group: `gradle tasks --group 'My custom tasks'`

Gradle plugins (see below) are also a means of writing tasks.
By putting tasks in a plugin you make them reusable.

```shell script
$ gradle -q showToday
Current date: Fri Feb 07 14:22:25 CET 2020
```
```shell script
$ gradle -q hello
Hello World!
```

:warning: If you don't define a `group` then `gradle tasks` will show this task only if you add the `--all` flag. 
In IntelliJ the task will appear in the 'other' group.

More information [here](https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html).

## 6. Logging & Build lifecycle

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

## 7. Gradle wrapper (gradlew)

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


## 8. Plugins

Plugins are a means to make tasks reusable by wrapping them in a library.
The sub-module [`my-plugin`](java-project/my-plugin) defines a task `myPluginTask`. 

Take a look in [`settings.gradle`](java-project/settings.gradle) and [`build.gradle`](java-project/build.gradle) of the parent module 
to find out how to import the plugin.


## 9. Profiling

Add the `--profile` flag to generate profile reports.

```shell script
$ gradle build --profile
```

## 10. Gradle Cloud Build Scan

* Generate advanced build reports
* These reports are saved to the cloud
* Managed by Gradle
* Available as free and commercial version

```shell script
$ gradle build --scan
```

See [scans.gradle.com](https://scans.gradle.com/) for more information.
