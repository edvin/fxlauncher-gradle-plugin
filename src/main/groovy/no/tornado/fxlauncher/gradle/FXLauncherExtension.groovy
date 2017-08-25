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
    String fxlauncherVersion = '1.0.17'

    String applicationMainClass

    String applicationParameters

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
