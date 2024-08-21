package com.lifesparktech.lsphysio

import com.example.lsphysio.IOSPlatform
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override var name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()