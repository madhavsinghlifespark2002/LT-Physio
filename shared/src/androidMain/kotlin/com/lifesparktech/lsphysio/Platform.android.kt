package com.lifesparktech.lsphysio

class AndroidPlatform : Platform {
    override var name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()