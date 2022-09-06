package domain.utils

import data.wrapper_classes.Result
import kotlinx.coroutines.flow.Flow
import java.io.File

abstract class SoftwareUtils {


    abstract fun getProgramsList(): Flow<Result<*>>

    abstract fun searchProgramPath(program: String, preferredHardDrive: String? = null): Flow<Result<File?>>

    abstract fun launchProgram(program: String): Flow<Result<String?>>


}




