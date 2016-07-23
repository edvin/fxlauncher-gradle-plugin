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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * @author Andres Almiray
 */
class FXLauncherPlugin implements Plugin<Project> {
    static String CONFIGURATION_NAME = 'internal_fxlauncher'

    void apply(Project project) {
        FXLauncherExtension extension = project.extensions.create('fxlauncher', FXLauncherExtension, project)
        Configuration configuration = project.configurations.maybeCreate(CONFIGURATION_NAME)
        configuration.visible = false

        boolean defaultDependenciesSupported = configuration.respondsTo('defaultDependencies')
        if (defaultDependenciesSupported) {
            configuration.defaultDependencies { dependencies ->
                dependencies.add(project.dependencies.create('no.tornado:fxlauncher:' + extension.fxlauncherVersion))
            }
        }

        project.afterEvaluate {
            if (!project.extensions.fxlauncher.noDefaultRepositories) {
                project.repositories {
                    jcenter()
                }
            }
            if (!defaultDependenciesSupported) {
                project.dependencies {
                    "${CONFIGURATION_NAME}"('no.tornado:fxlauncher:' + extension.fxlauncherVersion)
                }
            }
        }

        project.task('copyAppDependencies',
                type: CopyAppDependenciesTask,
                group: 'FXLauncher',
                description: 'Copies all application runtime dependencies into working directory',
                dependsOn: project.tasks.jar
        )

        project.task('generateApplicationManifest',
                type: GenerateApplicationManifestTask,
                group: 'FXLauncher',
                dependsOn: 'copyAppDependencies',
                description: 'Generates the application manifest'
        )

        project.task('embedApplicationManifest',
                type: EmbedApplicationManifestTask,
                group: 'FXLauncher',
                description: 'Embeds the application manifest in fxlauncher.jar',
                dependsOn: 'generateApplicationManifest'
        )

        project.task('generateNativeInstaller',
                type: GenerateNativeInstallerTask,
                group: 'FXLauncher',
                description: 'Generate a native installer for your platform using javapackager',
                dependsOn: 'embedApplicationManifest'
        )

        project.task('deployApp',
                type: DeployAppTask,
                group: 'FXLauncher',
                description: 'Deploy the application artifacts to the remote repository via scp',
                dependsOn: 'embedApplicationManifest'
        )
    }
}
