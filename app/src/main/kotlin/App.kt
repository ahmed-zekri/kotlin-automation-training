import androidx.compose.ui.window.application
import data.repositories.GitUtilsImpl
import data.repositories.KeyboardUtilsImpl
import domain.use_cases.PushCodeUseCase
import domain.utils.GitUtils
import domain.utils.KeyboardUtils
import presentation.mainMenu


private val gitUtils: GitUtils = GitUtilsImpl()
private val keyboardUtils: KeyboardUtils = KeyboardUtilsImpl()
private val pushCodeUseCase = PushCodeUseCase(gitUtils, keyboardUtils)

suspend fun main() = application {
    mainMenu(::exitApplication) {
        pushCodeUseCase(
            "C:\\Users\\Lenovo\\AndroidStudioProjects\\android-PIMSVehicleMedia",
            mapOf("username" to "E526729", "password" to "01JiNen2")
        )
    }
}