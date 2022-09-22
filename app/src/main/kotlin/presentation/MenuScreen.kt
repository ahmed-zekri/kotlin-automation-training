package presentation

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.wrapper_classes.Result
import kotlinx.coroutines.launch
import presentation.types.MenuTypes


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun mainMenu(
    mainMenuInteractor: MainMenuInteractor,
    initializations: (suspend () -> Unit)? = null,
    exitApp: (() -> Unit)? = null,
    pushCodeFunction: suspend () -> Unit
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
                val scrollState = rememberLazyListState(0)
                val requester = remember { FocusRequester() }


                when (carsListValue) {
                    is Result.Error -> Text(carsListValue.error ?: "Unknown error")
                    is Result.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
                    }
                    is Result.Success -> {


                        if (scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == scrollState.layoutInfo.totalItemsCount - 1)
                            LaunchedEffect(false) {
                                mainMenuInteractor.getCars(this)
                            }

                        LaunchedEffect(Unit) {
                            requester.requestFocus()
                        }
                        LazyColumn(
                            state = scrollState,
                            modifier = Modifier.draggable(
                                orientation = Orientation.Vertical,
                                state = rememberDraggableState { delta ->
                                    coroutineScope.launch {
                                        scrollState.scrollBy(-delta)
                                    }
                                },
                            ).onKeyEvent { keyEvent ->
                                coroutineScope.launch {
                                    when (keyEvent.key) {
                                        Key.Z ->
                                            scrollState.scrollBy(-20f)
                                        Key.S ->
                                            scrollState.scrollBy(20f)

                                    }

                                }
                                true

                            }.focusRequester(requester)
                                .focusable()
                        ) {
                            carsListValue.data?.apply {
                                items(size) { index ->
                                    this@apply[index].apply {
                                        Column {
                                            Text("Year : $year")
                                            Text("Model : $category")
                                            Text("Model : $model")

                                        }
                                        Spacer(Modifier.height(20.dp))

                                    }
                                }

                            }
                        }
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
            val startPushCodeEvent = {
                coroutineScope.launch {

                    pushCodeFunction()
                }
            }

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
            Text(mainMenuInteractor.counterState.value)

        }


}
