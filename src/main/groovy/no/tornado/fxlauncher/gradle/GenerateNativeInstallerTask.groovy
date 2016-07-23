package no.tornado.fxlauncher.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 * @author Edvin Syse
 */
class GenerateNativeInstallerTask extends DefaultTask {
    @TaskAction
    void generate() {
        FXLauncherExtension fxlauncher = project.extensions.fxlauncher

        def status = new ProcessBuilder('javapackager',
                '-deploy',
                '-native',
                '-outdir',
                'installer',
                '-outfile', project.name,
                '-srcdir', fxlauncher.resolveWorkingDirectory().toString(),
                '-srcfiles', 'fxlauncher.jar',
                '-appclass', 'fxlauncher.Launcher',
                '-name', project.name,
                '-title', project.name,
                '-vendor', fxlauncher.applicationVendor ?: "Acme Inc",
                '-Bidentifier=' + project.group + '.' + project.name,
                '-BappVersion=' + project.version)
        .start()
        .waitFor()

        if (status != 0)
            throw new GradleException('javapackager exited with status ' + status)
    }

}
