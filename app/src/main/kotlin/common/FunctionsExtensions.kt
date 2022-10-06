package common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <R> (suspend () -> R).executeSuspendFunction(coroutineScope: CoroutineScope) =

    coroutineScope.launch {


        invoke()

    }


