import androidx.compose.ui.window.application
import data.remote.Api
import data.repositories.GitUtilsImpl
import data.repositories.KeyboardUtilsImpl
import data.repositories.RemoteDataRepositoryImpl
import domain.repositories.GitUtils
import domain.repositories.KeyboardUtils
import domain.use_cases.GetBranchesUseCase
import domain.use_cases.GetCarsUseCase
import domain.use_cases.LaunchInteractiveRebaseUseCase
import domain.use_cases.PushCodeUseCase
import presentation.MainMenuInteractor
import presentation.mainMenu


private val gitUtils: GitUtils = GitUtilsImpl()
private val keyboardUtils: KeyboardUtils = KeyboardUtilsImpl()
private val pushCodeUseCase = PushCodeUseCase(gitUtils, keyboardUtils)
private val getCarsUseCase = GetCarsUseCase(RemoteDataRepositoryImpl(Api.getInstance()))
private val getBranchesUseCase = GetBranchesUseCase(gitUtils)
private val interactiveRebaseUseCase = LaunchInteractiveRebaseUseCase(getBranchesUseCase, gitUtils)

suspend fun main() = application {
    val mainMenuInteractor = MainMenuInteractor(
        getCarsUseCase
    )
    mainMenu(
        mainMenuInteractor = mainMenuInteractor,
        initializations = { keyboardUtils.disableLogger() },
        exitApp = ::exitApplication,
        pushCodeFunction = {
            pushCodeUseCase(
                "C:\\Users\\Lenovo\\IdeaProjects\\Kotlin automation testing",
                mapOf("username" to "E526729", "password" to "01JiNen2")
            )
        })
    {
        val path = "C:\\Users\\Lenovo\\AndroidStudioProjects\\android-PIMSVehicleMedia"
        interactiveRebaseUseCase(path)

    }


}
