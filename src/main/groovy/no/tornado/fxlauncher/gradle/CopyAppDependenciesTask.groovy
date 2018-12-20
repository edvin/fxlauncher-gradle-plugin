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
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

/**
 * @author Andres Almiray
 */
public class CopyAppDependenciesTask extends DefaultTask {
    @InputFiles
    private FileCollection dependencies
    @OutputDirectory
    private File directoryToCopyTo


    @TaskAction
    void copyDependencies() {
        File workingDirectory = project.extensions.fxlauncher.resolveWorkingDirectory()
        if (!workingDirectory.exists()) workingDirectory.mkdirs()
        project.configurations.runtime.resolvedConfiguration.resolvedArtifacts.each { artifact ->
            project.copy {
                from artifact.file
                into workingDirectory
                if (artifact.classifier != null) {
                    rename { "${artifact.name}-${artifact.classifier}.${artifact.extension}" }
                } else {
                    rename { "${artifact.name}.${artifact.extension}" }
                }
            }
        }
        project.copy {
            from project.tasks.jar.archivePath
            into workingDirectory
        }
        if (project.extensions.fxlauncher.nativeLibraryPath) {
            project.copy {
                from project.extensions.fxlauncher.nativeLibraryPath
                into workingDirectory
            }
        }
    }
}
