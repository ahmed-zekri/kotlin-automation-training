import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope

suspend fun main() {




    try {


        coroutineScope {


        }
    } catch (cancellationException: CancellationException) {
        println("Canceled by user")

    }

}