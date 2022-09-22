package domain.use_cases

import data.wrapper_classes.Result
import domain.repositories.GitUtils
import domain.repositories.KeyboardUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jnativehook.keyboard.NativeKeyEvent

class PushCodeUseCase(private val gitUtils: GitUtils, private val keyboardUtils: KeyboardUtils) {
    suspend operator fun invoke(path: String, credentials: Map<String, String>?) = coroutineScope {
        keyboardUtils.disableLogger()
        keyboardUtils.getPressedKeys().onEach {

            if (it?.keyCode == NativeKeyEvent.VC_P)
                gitUtils.pushCode(path, credentials).onEach { result ->
                    if (result is Result.Success)
                        println(result.data)
                    if (result is Result.Error)
                        System.err.println(result.error)

                }.launchIn(this)

        }.launchIn(this)


    }
}
