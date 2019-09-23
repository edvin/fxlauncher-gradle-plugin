/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.tornado.fxlauncher.gradle

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * @author Andres Almiray
 */
@CompileStatic
class FXLauncherExtension {
    String fxlauncherVersion = '1.0.21'

    String applicationMainClass

    String applicationName

    String applicationParameters

    String applicationTitle

    String applicationVendor

    String applicationVersion

    String applicationUrl

    String deployTarget

    String whatsNew

    String preloadNativeLibraries

    String includeExtensions

    String nativeLibraryPath

    List<String> scpOptions

    List<String> javapackagerOptions

    /**
     * The type of native bundle you'd like the
     * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/deploy/packager.html">javapackager</a> to create.
     * <p>
     * If left blank, the default is "all".
     * <p>
     * The following values are valid:
     * <ul>
     * <li>jnlp: Generates the .jnlp and .html files for a Java Web Start application.</li>
     * <li>all: Runs all of the installers for the platform on which it’s running, and creates a disk image for the application. This value is used if type isn’t specified.</li>
     * <li>installer: Runs all of the installers for the platform on which it’s running.</li>
     * <li>image: Creates a disk image for the application.
     *   <ul>
     *     <li>Linux and Windows: The image is the directory that gets installed.</li>
     *     <li>macOS: The image is the .app file.</li>
     *   </ul>
     * </li>
     * <li><a href="https://docs.oracle.com/javase/8/docs/technotes/guides/deploy/self-contained-packaging.html#BCGGFFBG">exe</a>: Generates a Windows .exe package.</li>
     * <li><a href="https://docs.oracle.com/javase/8/docs/technotes/guides/deploy/self-contained-packaging.html#JSDPG601">msi</a>: Generates a Windows Installer package.</li>
     * <li><a href="https://docs.oracle.com/javase/8/docs/technotes/guides/deploy/self-contained-packaging.html#JSDPG602">dmg</a>: Generates a DMG file for macOS.</li>
     * <li>pkg: Generates a .pkg package for macOS.</li>
     * <li>mac.appStore: Generates a package for the Mac App Store.</li>
     * <li><a href="https://docs.oracle.com/javase/8/docs/technotes/guides/deploy/self-contained-packaging.html#JSDPG603">rpm</a>: Generates an RPM package for Linux.</li>
     * <li><a href="https://docs.oracle.com/javase/8/docs/technotes/guides/deploy/self-contained-packaging.html#JSDPG603">deb</a>: Generates a Debian package for Linux.</li>
     * </ul>
     */
    String javapackerNativeParam = ""

    /**
     * The text that the launcher will show in the FXLauncher updating window.
     * <p>
     * The default is <code>Updating...</code>.
     */
    String updateText

    /**
     * The CSS styling to apply to the {@link #updateText} Label.
     * <p>
     * The default is <code>-fx-font-weight: bold;</code>.
     */
    String updateLabelStyle

    /**
     * The CSS styling to apply to the progress bar.
     * <p>
     * The default is <code>-fx-pref-width: 200;</code>.
     */
    String progressBarStyle

    /**
     * The CSS styling to apply to the VBox that contains the update text and the progress bar.
     * <p>
     * The default is <code>-fx-spacing: 10; -fx-padding: 25;</code>.
     */
    String wrapperStyle

    String cacheDir

    Boolean acceptDowngrade

    Boolean lingeringUpdateScreen = false

    File workingDirectory

    boolean noDefaultRepositories = false

    boolean stopOnUpdateErrors = false

    final Project project


    FXLauncherExtension(Project project) {
        this.project = project
        applicationVersion = project.version
    }

    File resolveWorkingDirectory() {
        workingDirectory ?: project.file("${project.buildDir}/fxlauncher")
    }

    String resolveApplicationMainClass() {
        if (applicationMainClass) return applicationMainClass
        if (project.plugins.hasPlugin('application')) {
            return project.properties.mainClassName
        }
        throw new GradleException('Must define a value for `fxlauncher.applicationMainClass`!')
    }

    String resolveApplicationUrl() {
        if (applicationUrl) return applicationUrl
        throw new GradleException('Must define a value for `fxlauncher.applicationUrl`!')
    }

    String resolveApplicationParameters() {
        applicationParameters ?: ''
    }

    String resolveInstallerName() {
        String appName = resolveApplicationMainClass()
        int dot = appName.lastIndexOf('.')
        return dot != -1 ? appName[(dot + 1)..-1] : appName
    }
}
