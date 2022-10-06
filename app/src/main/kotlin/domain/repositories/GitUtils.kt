package domain.repositories

import data.wrapper_classes.Result
import kotlinx.coroutines.flow.Flow

interface GitUtils {
    fun pushCode(path: String, credentials: Map<String, String>?): Flow<Result<String?>>
    fun startInteractiveRebase(path: String, firstBranch: String = "", secondBranch: String = ""): Flow<Result<*>>
    fun getBranches(path: String, includeRemote: Boolean = false): Flow<Result<*>>
}
