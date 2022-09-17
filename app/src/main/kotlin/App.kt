import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.repositories.GitUtilsImpl
import data.repositories.KeyboardUtilsImpl
import domain.use_cases.PushCodeUseCase
import domain.utils.GitUtils
import domain.utils.KeyboardUtils
import kotlinx.coroutines.launch


private val gitUtils: GitUtils = GitUtilsImpl()
private val keyboardUtils: KeyboardUtils = KeyboardUtilsImpl()
private val pushCodeUseCase = PushCodeUseCase(gitUtils, keyboardUtils)

suspend fun main() = application {
    val coroutineScope = rememberCoroutineScope()

    val startPushCodeEvent = {
        coroutineScope.launch {
            pushCodeUseCase(
                "C:\\Users\\Lenovo\\AndroidStudioProjects\\android-PIMSVehicleMedia",
                mapOf("username" to "E526729", "password" to "01JiNen2")
            )
        }

    }


    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        val count = remember { mutableStateOf(0) }
        MaterialTheme {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        count.value++
                    }) {
                    Text(if (count.value == 0) "Hello World" else "Clicked ${count.value}!")
                }
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        count.value = 0
                    }) {
                    Text("Reset")
                }

                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        startPushCodeEvent()
                    }) {
                    Text("push code")
                }
            }
        }
    }
}