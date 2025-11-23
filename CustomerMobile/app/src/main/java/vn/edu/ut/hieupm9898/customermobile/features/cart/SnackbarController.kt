package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Singleton quản lý Snackbar/Toast toàn app
 */
object SnackbarController {

    private var snackbarHostState: SnackbarHostState? = null
    private var coroutineScope: CoroutineScope? = null

    /**
     * Khởi tạo controller (gọi trong MainActivity hoặc RootComposable)
     */
    fun initialize(hostState: SnackbarHostState, scope: CoroutineScope) {
        snackbarHostState = hostState
        coroutineScope = scope
    }

    /**
     * Hiển thị Snackbar thành công (màu xanh)
     */
    fun showSuccess(
        message: String,
        actionLabel: String? = null,
        onActionClick: (() -> Unit)? = null
    ) {
        show(
            message = "✅ $message",
            actionLabel = actionLabel,
            onActionClick = onActionClick
        )
    }

    /**
     * Hiển thị Snackbar lỗi (màu đỏ)
     */
    fun showError(
        message: String,
        actionLabel: String? = "Thử lại",
        onActionClick: (() -> Unit)? = null
    ) {
        show(
            message = "❌ $message",
            actionLabel = actionLabel,
            onActionClick = onActionClick,
            duration = SnackbarDuration.Long
        )
    }

    /**
     * Hiển thị Snackbar thông thường
     */
    fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onActionClick: (() -> Unit)? = null
    ) {
        coroutineScope?.launch {
            val result = snackbarHostState?.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )

            if (result == SnackbarResult.ActionPerformed && onActionClick != null) {
                onActionClick()
            }
        }
    }
}