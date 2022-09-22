package data.repositories

import domain.repositories.KeyboardUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.awt.Robot
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger
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


            }

            override fun nativeKeyPressed(nativeEvent: NativeKeyEvent?) {

            }

            override fun nativeKeyReleased(nativeEvent: NativeKeyEvent?) {
                trySend(nativeEvent)
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

    override fun disableLogger() {
        // Clear previous logging configurations.
        // Clear previous logging configurations.
        LogManager.getLogManager().reset()

// Get the logger for "org.jnativehook" and set the level to off.

// Get the logger for "org.jnativehook" and set the level to off.
        val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
        logger.level = Level.OFF
    }

}