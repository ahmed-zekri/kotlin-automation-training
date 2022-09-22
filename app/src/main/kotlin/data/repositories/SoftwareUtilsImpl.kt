package data.repositories

import common.FileFound
import common.runCommand
import data.wrapper_classes.Result
import domain.data.Software
import domain.repositories.SoftwareUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.File
import javax.swing.filechooser.FileSystemView


class SoftwareUtilsImpl : SoftwareUtils() {
    override fun getProgramsList(): Flow<Result<List<Software>>> = callbackFlow {

        val job =
            "C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe -Command \"Get-ItemProperty HKLM:\\Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\* | Select-Object DisplayName, DisplayVersion, Publisher, Size, InstallDate | Format-Table -AutoSize\"".runCommand(
                redirectToStdErr = false
            ).onEach { result ->


                val softwareString = result.data

                val installedSoftware = mutableListOf<Software>()
                softwareString?.split("\n")?.forEachIndexed {

                        index, line ->

                    if (index >= 3) {

                        val items = line.trim().split("   ").filter { it.trim().isNotEmpty() }
                        if (items.isNotEmpty())
                            installedSoftware.add(
                                Software(
                                    items.getOrNull(0)?.trim(),
                                    items.getOrNull(1)?.trim(), items.getOrNull(2)?.trim()
                                )
                            )

                    }


                }

                trySend(Result.Success(installedSoftware))
            }


                .launchIn(this)

        awaitClose { job.cancel() }
    }

    override fun searchProgramPath(program: String, preferredHardDrive: String?): Flow<Result<File?>> = flow {

        suspend fun recursiveSearch(searchTerm: String, files: Array<File>?) {
            var filesNumber = 0
            if (files != null) {

                for (file in files) {
                    if (file.isDirectory) {
                        val filesInFolder = file.listFiles()

                        if (filesInFolder != null) {
                            for (f in filesInFolder) {
                                if (f.isFile) {
                                    emit(
                                        Result.Loading(
                                            loadingProgress = filesNumber++,
                                            loadingMax = filesInFolder.size,
                                            info = file.absolutePath
                                        )
                                    )
                                    isTheSearchedFile(f, searchTerm)
                                }


                            }
                            for (f in filesInFolder) {
                                if (f.isDirectory) {
                                    recursiveSearch(searchTerm, arrayOf(f))
                                }
                            }
                        }
                    } else isTheSearchedFile(file, searchTerm)


                }
            }
        }
        try {
            val systemRoots = File.listRoots()
            preferredHardDrive?.apply {
                systemRoots.forEachIndexed { index, element ->
                    if (this.lowercase() in element.absolutePath.lowercase()) {
                        val temp = systemRoots[0]
                        systemRoots[0] = element
                        systemRoots[index] = temp

                        return@forEachIndexed

                    }

                }

            }
            FileSystemView.getFileSystemView().apply {
                for (root in systemRoots) {
                    emit(Result.Loading(info = "Searching Drive  ${root.absolutePath}"))
                    if (getSystemTypeDescription(root) == "Local Disk") {
                        val filesFromRoot = root.listFiles()
                        recursiveSearch(program, filesFromRoot)

                    }
                }

            }



            emit(Result.Success())
        } catch (fileFound: FileFound) {

            emit(Result.Success(fileFound.file))

        }
    }


    override fun launchProgram(program: String): Flow<Result<String?>> = program.runCommand()


    private fun isTheSearchedFile(file: File, searchTerm: String) =
        file.run {
            if (isFile && searchTerm.lowercase() in name.lowercase() && extension == "exe")

                throw FileFound(this)


        }


}
