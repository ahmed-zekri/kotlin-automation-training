package domain.use_cases

import common.runCommand
import data.wrapper_classes.Result
import domain.repositories.KeyboardUtils
import domain.repositories.SoftwareUtils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LaunchProgramUseCase(
    private val softwareUtils: SoftwareUtils,
    private val keyboardUtils: KeyboardUtils,
    private val preferredHardDrive: String? = null
) {
    suspend operator fun invoke(programName: String) = coroutineScope {
        var searchCanceled = false
        launch {
            keyboardUtils.getPressedKeys().onEach {
                if (it?.keyChar == 'c')
                    searchCanceled = true
            }.launchIn(this)
        }


        softwareUtils.searchProgramPath(programName, preferredHardDrive = preferredHardDrive).onEach {
            if (searchCanceled) {

                cancel("Canceled by user")

            }
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
