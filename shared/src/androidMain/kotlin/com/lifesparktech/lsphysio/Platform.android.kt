package com.lifesparktech.lsphysio
import android.content.Context
import com.juul.kable.Peripheral
import com.juul.kable.Characteristic
import kotlinx.coroutines.CoroutineScope

object PeripheralManager {
    var peripheral: Peripheral? = null
    var charWrite: Characteristic? = null
    var charRead: Characteristic? = null
    lateinit var mainScope: CoroutineScope;
    var gameContext: Context? = null
}
class AndroidPlatform : Platform {
    override var name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}
actual fun getPlatform(): Platform = AndroidPlatform()