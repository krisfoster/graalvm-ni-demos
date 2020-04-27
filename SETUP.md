
## Getting Setup

### Installing GraalVM

The full instructions on getting setup, can be found here, [Installing ](https://www.graalvm.org/docs/getting-started/#install-graalvm).

A quick summary of this is:

1. Download the latest version of GraalVM EE for your OS, from [Download](https://www.oracle.com/downloads/graalvm-downloads.html)
2. You will need to download the following packages:
    - Oracle GraalVM Enterprise Edition for JDK8 (Version 20.0.0)
    - Oracle GraalVM Enterprise Editipon Native Image Early Adopter based on JDK8 (Version 20.0.0)
    - Oracle GraalVM Enterprise Edition Python for JDK8 (Version 20.0.0)
    - Oracle GraalVM Enterprise Edition Ruby for JDK8 (Version 20.0.0)
3. Install the downloaded GraalVM EE. This is a `tar.gz` file. You will need to extract it to a location that works for you and that will then become the root of your install. I chose the following: `~/bin/graal/20.0.1/java8/graalvm-ee-java8-20.0.0`
4. Update your environment in order to add a `GRAALVM_HOME` and addthings to the path. You can do this by updating your shell startup script, in my case  `~/.zshrc` as follows:

    ```
    # Note that I am using the JDK8 version.
    export GRAALVM_HOME=~/bin/graal/20.0.0/java8/graalvm-ee-java8-20.0.0
    export JAVA_HOME="${GRAALVM_HOME}"
    # Add the bin dir of GraalVM to your path, so you will be able to reference the exes
    export PATH="${GRAALVM_HOME}/bin:$PATH"
    ```
### Testing Your Installation

Create a new shell (or source your shell script) and type the following:

    ```
    java -version
    java version "1.8.0_241"
    Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
    Java HotSpot(TM) 64-Bit Server VM GraalVM EE 20.0.0 (build 25.241-b07-jvmci-20.0-b02, mixed mode)
    ```

Did you see the same output as above? If so, then it worked.

Test out the `gu` package tool:

    $ gu --help

Did that work? If it did you now have access to the package tool that will allow you to install the various additional packages that come with GraalVM. We will go through a few of these shortly.

### Installing Language Modules

We use the `gu` package tool to install the extra additional modules that you can use with GraalVm, such as the various language runtimes. In this section we will step through installing these.

All language / component installations are carrid out using the `gu` tool that is distributed with GraalVM. The latest instructions on using this toll to install components can be found at:

[gu Tool - Enterprise Edition](https://docs.oracle.com/en/graalvm/enterprise/20/guide/reference/graalvm-updater.html)

#### Installing JS / Node

Nothing to do here, these are available by default.

You can test the `node` and `JS` tooling as follows:

    $ node --help
    $ js --show-version

Both of the above commands will make reference to graalvm and the version. If you have node already installed, you may need to change your path or explicitly specify the path to the GraalVm version of node

### Installing the llvm-toolchain

A number of the packages require the `llvm-toolchain` in order to work. Regardless of whether you are using the Enterprise, or Community, Edition you will need to install the same version of this.

This is installed simply, using the `gu` command:

```
$ gu install llvm-toolchain
```
#### Installing Native Image

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image), but the process is very similar to that for Python.

1. Ensure you have the prerequisite libs available on your system: `glibc-devel, zlib-devel`
    - On linux these can be installed using your package manager
2. `$ gu -L install <DOWNLOAD-LOCATION>/native-image-installable-svm-svmee-java8-linux-amd64-20.0.0.jar`
3. Test the installation with: `native-image --version`

### Intalling Ruby

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/languages/ruby/#installing-ruby).

But the basic steps are:

1. `$gu install -L <DOWNLOAD-LOCATION>/ruby-installable-svm-svmee-java8-linux-amd64-20.0.0.jar`

Test that your installation works by running ruby:

```
$ which ruby
$ ruby --version
```

If you already have Ruby installed, then you will need to adjust your path or use the full path to the ruby binary.

#### Installing Python

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/languages/python/#installing-python).

But the basic steps are:

1. `$ gu -L install <DOWNLOAD-LOCATION>/python-installable-svm-svmee-java8-linux-amd64-20.0.0.jar`
2. Test that Python is now installed:

        ```
        $ gu list
        # You should see that Python EE version 20.0.0 is now installed
        $ graalpython
        ```

#### Installing R

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/languages/r/#installing-r).

When installing the R language module, it is best to consult the installation page, above, as thwere are a number of prerequisites that need to be installed.

### Install Visual Studio Code

A number of plugins ahve been written for this editor, that allow for better integration with the GraalVM eco-system.

You are more than welcome to use another editor. When it comes to the polyglot debugging, please make sure that you have the Google Chrome browser installed.

### Install VS Code Plugins

Please install the following plugins:

1. GraalVM Extension Pack

This will install all of the individual extensions

## Docker - Pre-built Docker Images

GraalVM is available on a number of pre-built docker images that can be downloaded from Docker Hub. Currently only the Community Edition is available hee, but we will be making the Enterprise Edition available through Docker Hub soon.

You can find the Docker Images [here](https://hub.docker.com/r/oracle/graalvm-ce).

## Polyglot Demos

## Native Image Demos

## Proof of Concepts (POCs)

