package presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import data.dto.Car
import data.wrapper_classes.Result
import domain.use_cases.GetCarsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import presentation.types.MenuTypes
import java.util.*

class MainMenuInteractor(
    private val getCarsUseCase: GetCarsUseCase
) {
    fun setStateValue(menuType: MenuTypes) {
        _uiState.value = menuType
    }

    private var pageNumber: Int = 1

    private val _uiState: MutableState<MenuTypes> = mutableStateOf(MenuTypes.Home)
    val uiState: State<MenuTypes> = _uiState

    private val _carsListState: MutableState<Result<List<Car>>> = mutableStateOf(
        Result.Loading()
    )
    val carsListState: State<Result<List<Car>>> = _carsListState


    private val _counterState: MutableState<String> = mutableStateOf("00:00")
    val counterState: State<String> = _counterState

    private val _loadNewScreen: MutableState<Boolean> = mutableStateOf(false)
    val loadNewScreen: State<Boolean> = _loadNewScreen


    private var seconds = 0
    val startCounter: suspend Int.() -> Unit = {

        while (true) {
            delay(1000)
            seconds++
            _counterState.value = Date((seconds + 1).toLong() * 1000).toString().slice(14..19)


        }


    }

    fun setLoadNewScreen(value: Boolean) {
        _loadNewScreen.value = value
    }

    fun getCars(scope: CoroutineScope) = scope.launch(Dispatchers.IO) {

        getCarsUseCase(page = pageNumber).onEach { result ->
            when (result) {
                is Result.Success<*> -> {
                    println(pageNumber)
                    pageNumber++
                    val previousList = carsListState.value.data?.toMutableList()
                    if (previousList != result.data)
                        previousList?.addAll(result.data!!)
                    _carsListState.value =
                        Result.Success(data = previousList ?: result.data)
                }


                else -> {}
            }

        }.launchIn(this)

    }
}
