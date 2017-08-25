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

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction

/**
 * @author Andres Almiray
 */
class GenerateApplicationManifestTask extends DefaultTask {
    private static final CREATE_MANIFEST_CLASSNAME = 'fxlauncher.CreateManifest'

    private static ClassLoader cl
    Configuration classpath

    @TaskAction
    void generate() {
        if (classpath == null)
            classpath = project.configurations.getByName(FXLauncherPlugin.CONFIGURATION_NAME)

        setupClassLoader()

        FXLauncherExtension fxlauncher = project.extensions.fxlauncher

        def args = [
            fxlauncher.resolveApplicationUrl(),
            fxlauncher.resolveApplicationMainClass(),
            fxlauncher.resolveWorkingDirectory().absolutePath
        ]

        if (fxlauncher.cacheDir)
            args += '--cache-dir=' + fxlauncher.cacheDir

        if(fxlauncher.stopOnUpdateErrors)
            args += '--stopOnUpdateErrors'

        if (fxlauncher.acceptDowngrade)
            args += '--accept-downgrade=' + fxlauncher.acceptDowngrade

        if (fxlauncher.whatsNew)
            args += '--whats-new=' + fxlauncher.whatsNew

        if (fxlauncher.preloadNativeLibraries)
            args += '--preload-native-libraries=' + fxlauncher.preloadNativeLibraries

        if (fxlauncher.includeExtensions)
            args += '--include-extensions=' + fxlauncher.includeExtensions


        args += '--lingering-update-screen=' + fxlauncher.lingeringUpdateScreen

        def appParams = fxlauncher.resolveApplicationParameters()
        if (!appParams.isEmpty()) args += appParams

        loadClass(CREATE_MANIFEST_CLASSNAME).main(args as String[])
    }

    private static Class loadClass(String className) {
        cl.loadClass(className)
    }

    @SuppressWarnings('AssignmentToStaticFieldFromInstanceMethod')
    private void setupClassLoader() {
        if (classpath?.files) {
            def urls = classpath.files.collect { it.toURI().toURL() }
            cl = new URLClassLoader(urls as URL[], Thread.currentThread().contextClassLoader)
            Thread.currentThread().contextClassLoader = cl
        } else {
            cl = Thread.currentThread().contextClassLoader
        }
    }
}
