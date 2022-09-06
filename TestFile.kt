import data.repositories.SoftwareUtilsImpl
import domain.use_cases.LaunchProgramUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope

suspend fun main() {
    val softwareUtils = SoftwareUtilsImpl()
    val launchProgramUseCase = LaunchProgramUseCase(softwareUtils, preferredHardDrive = "d")


    try {


        coroutineScope {
            /* var searchCanceled: Boolean = false
             launch {
                 keyboardUtils.getPressedKeys().onEach {
                     if (it?.keyChar == 'c')
                         searchCanceled = true
                 }.launchIn(this)
             }*/
            launchProgramUseCase("elden")

        }
    } catch (cancellationException: CancellationException) {
        println("Canceled by user")

    }

}