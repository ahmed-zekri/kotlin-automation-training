package data.repositories

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import domain.utils.KeyboardUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.awt.Robot
import kotlin.system.exitProcess


class KeyboardUtilsImpl : KeyboardUtils {
    private val robot = Robot()

    init {
        try {
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            System.err.println("There was a problem registering the native hook.")
            System.err.println(ex.message)
            exitProcess(1)
        }


    }

    override fun getPressedKeys(): Flow<NativeKeyEvent?> = callbackFlow {
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {

            override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {

                trySend(nativeEvent)
            }

            override fun nativeKeyPressed(nativeEvent: NativeKeyEvent?) {

            }

            override fun nativeKeyReleased(nativeEvent: NativeKeyEvent?) {

            }

        })
        awaitClose {
            GlobalScreen.unregisterNativeHook()
        }

    }

    override fun sendKey(key: Int) {
        robot.keyPress(key)
        robot.keyRelease(key)
    }

}