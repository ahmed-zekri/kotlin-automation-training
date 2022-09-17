import data.repositories.GitUtilsImpl
import data.repositories.KeyboardUtilsImpl
import domain.use_cases.PushCodeUseCase
import domain.utils.GitUtils
import domain.utils.KeyboardUtils


suspend fun main() {
    val gitUtils: GitUtils = GitUtilsImpl()
    val keyboardUtils: KeyboardUtils = KeyboardUtilsImpl()
    PushCodeUseCase(gitUtils, keyboardUtils)(
        "C:\\Users\\Lenovo\\AndroidStudioProjects\\android-PIMSVehicleMedia",
        mapOf("username" to "E526729", "password" to "01JiNen2")
    )




}