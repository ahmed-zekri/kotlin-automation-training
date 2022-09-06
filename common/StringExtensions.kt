package common

import data.wrapper_classes.Result
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(workingDir: File? = null, redirectToStdErr: Boolean = true): Flow<Result<String?>> =
    callbackFlow {
        var process: Process? = null
        try {
            process = ProcessBuilder(*split(" ").toTypedArray())
                .directory(workingDir)
                .redirectOutput(if (redirectToStdErr) ProcessBuilder.Redirect.INHERIT else ProcessBuilder.Redirect.PIPE)
                .redirectError(if (redirectToStdErr) ProcessBuilder.Redirect.INHERIT else ProcessBuilder.Redirect.PIPE)
                .start()
            process.run {
                waitFor(5, TimeUnit.SECONDS)
                trySend(Result.Loading())


                trySend(Result.Success(if (redirectToStdErr) null else inputStream.bufferedReader().readText()))


            }


        } catch (e: IOException) {

            trySend(Result.Error(e.message))
            cancel()
        }
        awaitClose {
            process?.destroy()
        }


    }