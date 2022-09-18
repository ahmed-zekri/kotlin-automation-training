package presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import presentation.types.MenuTypes

class MainMenuInteractor {
    fun setStateValue(pushCode: MenuTypes.PushCode) {
        _uiState.value = pushCode
    }

    private val _uiState: MutableState<MenuTypes> = mutableStateOf(MenuTypes.Home)
    val uiState: State<MenuTypes> = _uiState


}