package vn.edu.ut.hieupm9898.customermobile.data.remote

/**
 * Sealed class đại diện cho trạng thái của API call
 * - Success: Dữ liệu tải thành công
 * - Error: Có lỗi xảy ra
 * - Loading: Đang tải dữ liệu
 */
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

/**
 * Extension function để xử lý result dễ dàng hơn
 */
inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        action(data)
    }
    return this
}

inline fun <T> NetworkResult<T>.onError(action: (String) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) {
        action(message)
    }
    return this
}

inline fun <T> NetworkResult<T>.onLoading(action: () -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Loading) {
        action()
    }
    return this
}