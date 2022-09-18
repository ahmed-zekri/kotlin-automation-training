package presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import presentation.types.MenuTypes

@Composable
fun mainMenu(
    mainMenuInteractor: MainMenuInteractor,
    initializations: (suspend () -> Unit)? = null,
    exitApp: (() -> Unit)? = null,
    pushCodeFunction: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = null) {

        initializations?.invoke()

    }




    Window(
        onCloseRequest = exitApp ?: {},
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        val startPushCodeEvent = {
            coroutineScope.launch {

                pushCodeFunction()
            }
        }
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
                        mainMenuInteractor.setStateValue(MenuTypes.PushCode)
                        startPushCodeEvent()
                    }) {
                    Text("push code")
                }

                Text(
                    text = when (mainMenuInteractor.uiState.value) {
                        MenuTypes.Home -> "Home"
                        MenuTypes.Exit -> "Exiting"


                        MenuTypes.PushCode -> "Pushing code"
                    }
                )
            }
        }
    }


}
