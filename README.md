# FXLauncher Gradle Plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.tornado/fxlauncher-gradle-plugin/badge.svg)](https://search.maven.org/#search|ga|1|no.tornado.fxlauncher-gradle-plugin)
[![Apache License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

See usage example in the [fxldemo-gradle demo project](https://github.com/edvin/fxldemo-gradle)

The plugin supports the following tasks:

- **copyAppDependencies**: Assembles the application into `build/fxlauncher`
- **generateApplicationManifest**: Generates app.xml into `build/fxlauncher`
- **embedApplicationManifest**: Copies app.xml into `fxlauncher.jar`
- **deployApp**: Transfers application to `deployTarget` via scp
- **generateNativeInstaller**: Generates native installer

Configuration example:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'no.tornado:fxlauncher-gradle-plugin:1.0.17'
    }
}

apply plugin: 'no.tornado.fxlauncher'

fxlauncher {
    applicationVendor 'My Company'
    applicationUrl 'http://host/path'
    applicationMainClass 'com.example.Application'
    deployTarget 'username@hostname:path'
}
```

**Please note that the `deployApp` task requires the scp binary to be present in your path, and that you have installed the corresponding target host key locally and the public key on the target**

The `deployTarget` option also accepts deploying to local file system paths, for example: `c:/some/path`.

Optionally include `scpOptions = ["-v"]` to enable verbose output from scp if you run into trouble.

## What's new file

Supply `whatsNew https://whatsnew/url` to include an HTML link that will be shown to the user after update.

## Optional parameters

If you need to further customize the parameters to `javapackager` you can supply the `javapackagerOptions` parameter, which takes a `List<String>`. Consult
the [JavaPackager docs](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javapackager.html) for more information about possible parameters.

Use `javapackerNativeParam` to supply arguments to `javapackager -native` flag such as `msi` or `dep` in order to generate native installer packages instead of MSI.


### Keep update screen until primary stage is shown, even if update is completed

```groovy
lingeringUpdateScreen true
```