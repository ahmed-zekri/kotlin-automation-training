package domain.utils

import data.wrapper_classes.Result
import kotlinx.coroutines.flow.Flow

interface GitUtils {
    fun pushCode(path: String, credentials: Map<String, String>?): Flow<Result<String?>>
}
