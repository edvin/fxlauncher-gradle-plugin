# FXLauncher Gradle Plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.tornado/fxlauncher-gradle-plugin/badge.svg)](https://search.maven.org/#search|ga|1|no.tornado.fxlauncher-gradle-plugin)
[![Apache License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

See usage example in the [fxldemo-gradle demo project](https://github.com/edvin/fxldemo-gradle)

The plugin supports the following tasks:

- **copyAppDependencies**: Assembles the application into `build/fxlauncher`
- **generateApplicationManifest**: Generates app.xml into `build/fxlauncher`
- **embedApplicationManifest**: Copies app.xml into `fxlauncher.jar`
- **deployApp**: Transfers application to `deployTarget` via scp
- **generateNativeInstaller**: Generates a native installer

Configuration example:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'no.tornado:fxlauncher-gradle-plugin:1.0.21'
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

The `deployTarget` option also accepts deploying to local file system paths, for example: `C:/some/path`.

Optionally include `scpOptions = ["-v"]` to enable verbose output from scp if you run into trouble.

## User interface customization
You can change how the FXLauncher's user interface looks when it's updating the app.

### Update Text
#### Actual Text
To change the default "Updating..." text, add something like `updateText 'Updating CoolApp...'` to the fxlauncher 
section of your build.gradle file.

#### Text Style
To style the update text, add something like `updateLabelStyle '-fx-underline: true;'` to the fxlauncher section of 
your build.gradle file. You can use any valid CSS rule that applies to a Labeled. Here's [the list for Labeled][Labeled CSS], 
but don't forget about the rules for its parents, [Control][Control CSS], [Region][Region CSS], [Parent][Parent CSS], 
and [Node][Node CSS].

### Progress Bar
To style the progress bar, add something like `progressBarStyle '-fx-accent: lime;'` to the fxlauncher section of 
your build.gradle file. (Hint: `-fx-accent` happens to be the rule that controls the progress bar's color. See java's 
modena.css as well as this [StackOverflow question][StackOverflow Question About Bar Color])

### Text and Bar Wrapper
There's a VBox around everything. To style it, add something like `-fx-spacing: 20;`to the fxlauncher section of your 
build.gradle file. You can use any valid CSS rule that applies to a VBox. Here's the [list for VBox][VBox CSS], but 
don't forget about the rules for its parents, [Pane][Pane CSS], [Region][Region CSS], [Parent][Parent CSS], and 
[Node][Node CSS].

[StackOverflow Question About Bar Color]: https://stackoverflow.com/questions/13357077/javafx-progressbar-how-to-change-bar-color/13372086#13372086

[Labeled CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#labeled
[Control CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#control
[VBox CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#vbox
[Pane CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#pane
[Region CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#region
[Parent CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#parent
[Node CSS]: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#node

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
