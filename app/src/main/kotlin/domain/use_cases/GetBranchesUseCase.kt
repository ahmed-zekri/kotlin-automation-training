package domain.use_cases

import data.wrapper_classes.Result
import domain.repositories.GitUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GetBranchesUseCase(private val gitUtils: GitUtils) {
    operator fun invoke(path: String) = callbackFlow<Result<List<String>>> {

        gitUtils.getBranches(path).collect {

            when (it) {
                is Result.Success -> trySend(
                    Result.Success(
                        (it.data as String).split("\n").map { branch -> branch.trim() }
                    )
                )
                is Result.Error -> trySend(Result.Error(it.error))
                else -> {}
            }

        }
        awaitClose {}
    }


}
