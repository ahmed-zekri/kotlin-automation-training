package common

import data.wrapper_classes.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.awt.GraphicsEnvironment
import java.io.*
import java.util.concurrent.TimeUnit


fun String.runCommand(
    workingDir: File? = null, redirectToStdErr: Boolean = true, path: String? = null, input: Any? = null
): Flow<Result<String?>> = callbackFlow {
    var process: Process? = null
    try {
        withContext(Dispatchers.IO) {

            process = ProcessBuilder(*split(" ").toTypedArray()).directory(workingDir)
                .redirectOutput(if (redirectToStdErr) ProcessBuilder.Redirect.INHERIT else ProcessBuilder.Redirect.PIPE)
                .redirectError(if (redirectToStdErr) ProcessBuilder.Redirect.INHERIT else ProcessBuilder.Redirect.PIPE)
                .directory(File((path ?: ""))).start() ?: return@withContext
            process?.run {
                waitFor(5, TimeUnit.SECONDS)
                trySend(Result.Loading())
                trySend(Result.Success(if (redirectToStdErr) "" else inputStream.bufferedReader().readText()))

                input?.apply {
                    if (input is Map<*, *> && input.values.isNotEmpty() && input.values.first() is String) {

                        val stdout = inputStream
                        val stderr = errorStream
                        val writer = BufferedWriter(OutputStreamWriter(this@run.outputStream))
                        val reader = BufferedReader(InputStreamReader(stdout))

                        val error = BufferedReader(InputStreamReader(stderr))
                        writer.run {
                            // Don't forget the '\n' here, otherwise it'll continue to wait for input
                            // Display the output
                            // Display the output

                            write(
                                input.getOrDefault(
                                    "username", ""
                                ) as String + "\n"
                            )
                            flush()
                            delay(1000)
                            write(
                                input.getOrDefault(
                                    "password", ""
                                ) as String + "\n"
                            )
                            flush()

                            var line: String?
                            while (reader.readLine().also { line = it } != null) println(line)
// Display any errors
// Display any errors
                            while (error.readLine().also { line = it } != null) println(line)

                        }


                        writer.flush()

                    }


                }
            }


        }


    } catch (e: IOException) {

        trySend(Result.Error(e.message))
        cancel()
    }
    awaitClose {
        process?.destroy()
    }


}

fun String.openTerminal(path: String?) = callbackFlow<Result<Any>> {

    val console = System.console()
    trySend(Result.Loading())
    var runtime: Runtime? = null
    if (console == null && !GraphicsEnvironment.isHeadless()) {
        runtime = Runtime.getRuntime().run {
            withContext(Dispatchers.IO) {


                try {
                    launch {
                        withContext(Dispatchers.IO) {
                            exec(
                                arrayOf(
                                    "cmd", "/c", "start", "cmd", "/k", "dir"
                                ), null, File(path ?: "")
                            )

                        }

                    }
                    trySend(Result.Success())
                } catch (e: Exception) {
                    trySend(Result.Error(e.message))
                    cancel()

                }







                runtime


            }


        }


    }
    awaitClose { runtime?.exit(0) }
}


inline fun <reified K, reified V> Map<*, *>.toTypedMap(): Map<K, V> {

    val resultMap = mutableMapOf<K, V>()

    keys.filterIsInstance<K>().forEach { key ->
        val value = get(key)
        if (value is V)
            resultMap[key] = value


    }
    return resultMap.toMap()

}
