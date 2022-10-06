package presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import common.executeSuspendFunction
import data.wrapper_classes.Result
import presentation.types.MenuTypes


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun mainMenu(
    mainMenuInteractor: MainMenuInteractor,
    initializations: (suspend () -> Unit)? = null,
    exitApp: (() -> Unit)? = null,
    pushCodeFunction: suspend () -> Unit,
    startInteractiveRebase: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val newScreenValue by mainMenuInteractor.loadNewScreen
    val carsListValue by mainMenuInteractor.carsListState



    LaunchedEffect(key1 = null) {

        initializations?.invoke()
        mainMenuInteractor.startCounter(1)

    }


    if (newScreenValue) {

        Window(title = "Load list", onCloseRequest = { mainMenuInteractor.setLoadNewScreen(false) }) {

            Box(modifier = Modifier.fillMaxSize()) {
                LaunchedEffect(key1 = false) {

                    mainMenuInteractor.getCars(scope = this)
                }

                when (carsListValue) {
                    is Result.Error -> Text(carsListValue.error ?: "Unknown error")
                    is Result.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
                    }
                    is Result.Success -> {
                        displayItems(
                            coroutineScope = coroutineScope,
                            mainMenuInteractor = mainMenuInteractor,
                            carsListValue = carsListValue
                        )
                    }
                }
            }
        }

    } else

        Window(
            onCloseRequest = exitApp ?: {},
            title = "Compose for Desktop",
            state = rememberWindowState(width = 300.dp, height = 300.dp)
        ) {

            MaterialTheme {
                Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                        mainMenuInteractor.setLoadNewScreen(true)
                    }) {
                        Text("Load list")
                    }
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {


                    }) {
                        Text("Reset")
                    }

                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                        mainMenuInteractor.setStateValue(MenuTypes.PushCode)
                        pushCodeFunction.executeSuspendFunction(coroutineScope)
                    }) {
                        Text("push code")
                    }

                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                        mainMenuInteractor.setStateValue(MenuTypes.InteractiveRebase)
                        startInteractiveRebase.executeSuspendFunction(coroutineScope)

                    }) {
                        Text("Interactive rebase")
                    }

                    Text(
                        text = when (mainMenuInteractor.uiState.value) {
                            MenuTypes.Home -> "Home"
                            MenuTypes.Exit -> "Exiting"


                            MenuTypes.PushCode -> "Pushing code"
                            else -> ""
                        }
                    )
                }
            }
            Text(mainMenuInteractor.counterState.value)

        }


}
