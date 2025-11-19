package vn.edu.ut.hieupm9898.customermobile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // Quan trọng nhất: Cái này giúp Hilt biết đây là app container
class BaseApplication : Application()