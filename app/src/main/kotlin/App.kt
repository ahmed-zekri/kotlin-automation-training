import androidx.compose.ui.window.application
import data.remote.Api
import data.repositories.GitUtilsImpl
import data.repositories.KeyboardUtilsImpl
import data.repositories.RemoteDataRepositoryImpl
import domain.use_cases.PushCodeUseCase
import domain.repositories.GitUtils
import domain.repositories.KeyboardUtils
import domain.use_cases.GetCarsUseCase
import presentation.MainMenuInteractor
import presentation.mainMenu


private val gitUtils: GitUtils = GitUtilsImpl()
private val keyboardUtils: KeyboardUtils = KeyboardUtilsImpl()
private val pushCodeUseCase = PushCodeUseCase(gitUtils, keyboardUtils)

suspend fun main() = application {
    mainMenu(
        mainMenuInteractor = MainMenuInteractor(GetCarsUseCase(RemoteDataRepositoryImpl(Api.getInstance()))),
        initializations = { keyboardUtils.disableLogger() },
        exitApp = ::exitApplication
    ) {
        pushCodeUseCase(
            "C:\\Users\\Lenovo\\IdeaProjects\\Kotlin automation testing",
            mapOf("username" to "E526729", "password" to "01JiNen2")
        )
    }
}