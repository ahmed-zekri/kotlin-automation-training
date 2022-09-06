package domain.utils

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import kotlinx.coroutines.flow.Flow

interface KeyboardUtils {



    fun getPressedKeys(): Flow<NativeKeyEvent?>
    fun sendKey(key: Int)

}