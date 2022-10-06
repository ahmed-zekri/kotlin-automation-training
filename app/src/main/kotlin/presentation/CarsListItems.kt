package presentation

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import data.dto.Car
import data.wrapper_classes.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun displayItems(
    mainMenuInteractor: MainMenuInteractor,
    coroutineScope: CoroutineScope,
    carsListValue: Result<List<Car>>
) {
    val scrollState = rememberLazyListState(0)
    val requester = remember { FocusRequester() }



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