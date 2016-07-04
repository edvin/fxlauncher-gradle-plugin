package no.tornado.fxlauncher.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.TaskAction

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class EmbedApplicationManifestTask extends DefaultTask {

    @TaskAction
    void embed() {
        Configuration config = project.configurations.getByName(FXLauncherPlugin.CONFIGURATION_NAME)
        ResolvedArtifact fxlauncher = config.resolvedConfiguration.resolvedArtifacts.find { it.name == 'fxlauncher' }
        File workingDirectory = project.extensions.fxlauncher.resolveWorkingDirectory()
        File destination = new File(workingDirectory, "${fxlauncher.name}.${fxlauncher.extension}")
        File manifest = new File(workingDirectory, "app.xml")
        copyZipAndAddManifest(fxlauncher.file, destination, manifest)
    }

    static void copyZipAndAddManifest(File zipFile, File targetZip, File manifestContent){
        def zin = new ZipFile(zipFile)
        targetZip.withOutputStream { os ->
            def zos = new ZipOutputStream(os)

            zos.putNextEntry(new ZipEntry("app.xml"))
            zos << manifestContent.bytes
            zos.closeEntry()

            zin.entries().each { entry ->
                if (entry.name != "app.xml") {
                    zos.putNextEntry(entry)
                    zos << zin.getInputStream(entry).bytes
                    zos.closeEntry()
                }
            }
            zos.close()
        }
        zin.close()
    }
}
