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
package org.kordamp.gradle.stats

import no.tornado.fxlauncher.gradle.FXLauncherPlugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Andres Almiray
 */
@Unroll
class FXLauncherPluginSpec extends Specification {
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
    }

    @SuppressWarnings('MethodName')
    def "Applies plugin and checks default setup (task #taskName)"() {

        expect:
        project.tasks.findByName(taskName) == null

        when:
        project.apply plugin: FXLauncherPlugin

        then:
        Task task = project.tasks.findByName(taskName)
        task != null
        task.group == 'FXLauncher'

        where:
        taskName << ['copyAppDependencies', 'generateApplicationManifest']
    }
}
