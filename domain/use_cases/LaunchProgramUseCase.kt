package domain.use_cases

import common.runCommand
import data.wrapper_classes.Result
import domain.utils.SoftwareUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LaunchProgramUseCase(private val softwareUtils: SoftwareUtils, private val preferredHardDrive: String? = null) {
    suspend operator fun invoke(programName: String) = coroutineScope {
        softwareUtils.searchProgramPath(programName, preferredHardDrive = preferredHardDrive).onEach {
            if (it is Result.Loading) {
                println(if (it.loadingProgress == null) it.info else "Searching ${it.loadingProgress}/${it.loadingMax} files in folder ${it.info}")

            } else if (it is Result.Success) {
                println(if (it.data == null) "File not found" else "File found : ${it.data.absolutePath}")

                if (it.data != null)
                    it.data.absolutePath.runCommand().launchIn(this)


            }

        }.launchIn(this)

    }


}