package domain.utils

import kotlinx.coroutines.flow.Flow
import org.jnativehook.keyboard.NativeKeyEvent

interface KeyboardUtils {


    fun getPressedKeys(): Flow<NativeKeyEvent?>
    fun sendKey(key: Int)
    fun disableLogger()

}