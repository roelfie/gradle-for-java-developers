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

To obtain an HTML report of the dependency trees, add plugin `project-report` to build.gradle, and run:

```shell script
$ gradle htmlDependencyReport
```



