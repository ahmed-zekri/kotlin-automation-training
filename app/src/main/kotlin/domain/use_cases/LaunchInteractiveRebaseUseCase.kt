package domain.use_cases

import data.wrapper_classes.Result
import domain.repositories.GitUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LaunchInteractiveRebaseUseCase(
    private val getBranchesUseCase: GetBranchesUseCase,
    private val gitUtils: GitUtils
) {
    suspend operator fun invoke(path: String, firstBranch: String = "", secondBranch: String = "") =
        coroutineScope {


            getBranchesUseCase(path).onEach {

                when (it) {
                    is Result.Success -> {
                        it.data?.forEach { branch ->
                            launch {

                                gitUtils.startInteractiveRebase(path).collect { res ->
                                    when (res) {

                                        is Result.Success -> println(branch)

                                        else -> {}
                                    }
                                }

                            }


                        }


                    }


                    else -> {}
                }


            }.launchIn(this)

        }

}

