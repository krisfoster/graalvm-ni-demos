# GraalVM Native Image Workshop

## Introduction

This workshop aims to walk you through a number of the features of GraalVM's Native Image,
namely:

1. Run a basic Java app
2. Make a native Image from it and see how that compares in terms of startup time
3. Look at how we use the tracing agent to identify reflection etc.
4. How we can run some of the application at build time

## Setup

Please install GraalVM. Instructions can be found [here](https://gist.github.com/krisfoster/1d4cce34996a47f55e9aa124413c666b).

You need a working version of Maven as well, and this should be covered in the installation
instructions.

## Outline of Demo

We are going to use a fairly trivial application to walk through some of the features that
are available ithin GraalVM Native Image. These are:

1. How you can turn a Java app into a native executable
2. How you deal with reflection etc.
3. Using the command line tools for generating a native image, as well as using the maven tooling

The application code that you have checked out builds a Java command line app that
counts the number of files within the current directory and sub directories. It also calculates their
total size.

You will need to update the code, in the following various steps, in order to steps through the
points we listed above.

Let's beign!

## Quick Note on Maven Profile

The maven build has been split into several different profiles, each of which does something
different. These profiles are called by passing a parameter that contains the name of the profile
to maven. In the example below the `JAVA` proflie is called:

`$ mvn clean package -PJAVA`

The name of the proflie to be called is appended to the `-P` flag. We have the following profiles defined:

1. `JAVA` : This builds the Java applicaiton
2. `JAVA_AGENT_LIB` : Ths builds the Java application with agent tracing. More on this later
3. `NATIVE_IMAGE` : This builds the native image

## Build the Basic Java App

So first off, let's check that we can build the application adn that it works as expected.

From a command line:

`$ mvn clean package exec:exec -PJAVA`

What the above does is to:

1. Clean the project - get rid of any generated or compiled stuff
2. Create a Jar with our application in it. We will also be compiling an uber jar
3. Runs the application by running the exec plugin

This should generate som eoutput. Did it work for you?

## Build a Native Image from the App

OK. Now let's build a native image version of our application.

We will do this by hand first. First check that you have a compiled uber jar in your `target` dir:

```
    $ ls ./target
      drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 archive-tmp
      drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 classes
      drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 generated-sources
      -rw-rw-r-- 1 krf krf  496273 Mar  4 11:38 graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar
      -rw-rw-r-- 1 krf krf    7894 Mar  4 11:38 graalvmnidemos-1.0-SNAPSHOT.jar
      drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 maven-archiver
      drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 maven-status
```

The file we want is, `graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar`.

Now we can generate a native image as follows, within the root of the project:

`$ native-image -jar ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar --no-fal
lback --no-server -H:Class=oracle.App -H:Name=file-count`

This will generate a file called, `file-count`, which you can run as follows:

`./file-count`

Try timing it:

`time ./file-count`

Compare that to running the app as Java:

`time java -cp ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar oracle.App`

What do the various parameters we passed to the `native-image` command do? Full documentation on these can be found [here](https://www.graalvm.org/docs/reference-manual/native-image/#image-generation-options):

* `--no-server` : Don't start a build server process. For our examples we just want to run the builds
* `--no-fallback` : Don't generate a fallback image. A fallback image requires the JVM and we generally dont want this
* `-H:Class` : Tells the native-image tool what the class is with the entry point method (main)
* `-H:Name` : This specifies what the output executable should be called

We can also run the `native-image` tool using maven. If you look at the `pom.xml` file in the project
you should be able to find the following snippet:

```
<!-- Native Image -->
<plugin>
    <groupId>org.graalvm.nativeimage</groupId>
    <artifactId>native-image-maven-plugin</artifactId>
    <version>${graalvm.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>native-image</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <!-- Set this to true if you need to switch this off -->
        <skip>false</skip>
        <!-- The output name for the executable -->
        <imageName>${exe.file.name}</imageName>
        <!-- Set any parameters we need to pass to the native-image tool.
            no-fallback : create a native image that doesn't fall back to the JVM
            no-server : don't start a build server, which you then just need to shut down,
                        in order to build the image
        -->
        <buildArgs>
            --no-fallback --no-server
        </buildArgs>
    </configuration>
</plugin>

```

This plugin does the heavy lifting of running the native image build. It can alsways be turned off suing the `<skip>true</skip>`
tags. Note also that we can pass parameters to `native-image` through the `<buildArgs />` tag.

## Add Log4J and Why We Need the Tracing Agent

So far, so good. But say we now want to add a library, or some code, to our project that
relies on reflection. A good candidate for testing this out would be to add `log4j`. Let's do that.

We've already added it as a dependency in the `pom.xml` file, all we need to do is to open
up the `ListDir.java` file and uncomment some things. Go through and uncomment the various lines
that add the imports and the logging code.

OK, so now we have added logging, let's see if it works by rebuilding an running our Java app:

`$ mvn clean package exec:exec -PJAVA`


## Running Parts of the Application at Build Time