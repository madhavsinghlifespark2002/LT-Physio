package com.lifesparktech.lsphysio
import com.juul.kable.Peripheral
import com.juul.kable.Characteristic
import kotlinx.coroutines.CoroutineScope

object PeripheralManager {
    var peripheral: Peripheral? = null
    var charWrite: Characteristic? = null
    lateinit var mainScope: CoroutineScope;
}
class AndroidPlatform : Platform {
    override var name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()