package no.tornado.fxlauncher.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 * This task requires the scp command and established credentials to the remote server
 * where the application artifacts should be uploaded.
 */
class DeployAppTask extends DefaultTask {
    @TaskAction
    void deploy() {
        FXLauncherExtension fxlauncher = project.extensions.fxlauncher

        if (!fxlauncher.deployTarget)
            throw new GradleException('Must define a value for `fxlauncher.deployTarget`! (Example user@host:path or c:/some/path)')

        def params = ["scp"]
        if (fxlauncher.scpOptions) params.addAll(fxlauncher.scpOptions)
        params.addAll("-r", fxlauncher.resolveWorkingDirectory().toString() + '/.', fxlauncher.deployTarget)

        def status = new ProcessBuilder(params).start().waitFor()

        if (status != 0)
            throw new GradleException("scp exited with status ${status}")

        println("Artifacts in ${fxlauncher.resolveWorkingDirectory()} were deployed to to ${fxlauncher.deployTarget}")
    }
}
