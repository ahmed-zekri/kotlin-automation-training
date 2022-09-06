package data.wrapper_classes


sealed class Result<T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = true,
    val loadingProgress: Int? = null, val loadingMax: Int? = null, val info: String? = null
) {
    class Success<T>(data: T? = null) : Result<T>(data = data, loading = false)
    class Loading<T>(loadingProgress: Int? = null, loadingMax: Int? = null, info: String? = null) :
        Result<T>(loading = true, loadingProgress = loadingProgress, loadingMax = loadingMax, info = info)

    class Error<T>(error: String?) : Result<T>(error = error, loading = false)


}
