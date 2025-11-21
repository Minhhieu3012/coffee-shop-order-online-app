package vn.edu.ut.hieupm9898.customermobile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    // Hilt sẽ tự động sinh code ở đây, bạn không cần viết gì thêm
}