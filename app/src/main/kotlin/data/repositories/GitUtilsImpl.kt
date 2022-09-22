package data.repositories

import common.runCommand
import domain.repositories.GitUtils


class GitUtilsImpl : GitUtils {
    override fun pushCode(path: String, credentials: Map<String, String>?) =


        "git push".runCommand(
            redirectToStdErr = true, input = credentials, path = path
        )

}
