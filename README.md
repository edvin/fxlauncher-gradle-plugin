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
        classpath 'no.tornado:fxlauncher-gradle-plugin:1.0.15'
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

Optionally include `scpOptions '-v'` to enable verbose output from scp if you run into trouble.
