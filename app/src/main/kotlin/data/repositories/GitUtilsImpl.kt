package data.repositories

import common.openTerminal
import common.runCommand
import data.wrapper_classes.Result
import domain.repositories.GitUtils
import kotlinx.coroutines.flow.Flow


class GitUtilsImpl : GitUtils {
    override fun pushCode(path: String, credentials: Map<String, String>?) =


        "git push".runCommand(
            redirectToStdErr = true, input = credentials, path = path
        )

    override fun startInteractiveRebase(path: String, firstBranch: String, secondBranch: String): Flow<Result<*>> =
        path.openTerminal(path)

    override fun getBranches(path: String, includeRemote: Boolean): Flow<Result<*>> =
        "git branch".runCommand(path = path, redirectToStdErr = false)

}
