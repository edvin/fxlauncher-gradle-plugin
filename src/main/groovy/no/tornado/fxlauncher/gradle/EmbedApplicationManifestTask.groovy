package no.tornado.fxlauncher.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.TaskAction

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * This task embeds the app.xml manifest inside the fxlauncher.jar.
 * It also  handles embedding of what's new html if it is supplied with
 * the `whatsNew` option.
 */
class EmbedApplicationManifestTask extends DefaultTask {

    @TaskAction
    void embed() {
        Configuration config = project.configurations.getByName(FXLauncherPlugin.CONFIGURATION_NAME)
        ResolvedArtifact fxlauncher = config.resolvedConfiguration.resolvedArtifacts.find { it.name == 'fxlauncher' }
        File workingDirectory = project.extensions.fxlauncher.resolveWorkingDirectory()
        File destination = new File(workingDirectory, "${fxlauncher.name}.${fxlauncher.extension}")
        File manifest = new File(workingDirectory, "app.xml")
        String whatsNewRef = project.extensions.fxlauncher.whatsNew
        File whatsNew = whatsNewRef != null ? new File(whatsNewRef) : null
        copyZipAndAddManifest(fxlauncher.file, whatsNew, destination, manifest)
    }

    static void copyZipAndAddManifest(File zipFile, File whatsNew, File targetZip, File manifestContent){
        def zin = new ZipFile(zipFile)
        targetZip.withOutputStream { os ->
            def zos = new ZipOutputStream(os)

            zos.putNextEntry(new ZipEntry("app.xml"))
            zos << manifestContent.bytes
            zos.closeEntry()

            if (whatsNew != null) {
                if (whatsNew.exists()) {
                    zos.putNextEntry(new ZipEntry(whatsNew.name))
                    zos << whatsNew.bytes
                    zos.closeEntry()
                } else {
                    println "What's new file $whatsNew doesn't exist, skipping embedding step"
                }
            }

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
