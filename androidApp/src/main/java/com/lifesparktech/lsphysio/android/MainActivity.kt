package com.lifesparktech.lsphysio.android
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.app.ActivityCompat
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.R
import com.lifesparktech.lsphysio.android.components.vibrateLeft
import com.lifesparktech.lsphysio.android.components.vibrateRight
import com.lifesparktech.lsphysio.android.pages.MyMaterial3App
import com.lifesparktech.lsphysio.android.pages.closeUnity
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val route: String
)
var unityPlayer: UnityPlayer? = null
var unityView: View? = null
class MainActivity : ComponentActivity() {

    private val enableBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == RESULT_OK) {
            println("Bluetooth enabled successfully")
        } else {
            println("Bluetooth enabling failed")
            requestBluetoothPermissions()
        }
    }
    private val enableLocationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            println("Location enabled successfully")
        } else {
            println("Location enabling failed")
            requestLocationPermissions()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // instance =
        unityPlayer = UnityPlayer(this)
//        setContentView(R.layout.activity_main) // Set the content view first, if not done yet
//
//        // Add the UnityPlayer's surface view to the layout
//        unityView = unityPlayer?.view
//
//        // Ensure the Unity view is added to the current view
//        unityView?.let { view ->
//            val parentLayout = findViewById<ViewGroup>(R.id.container_layout) // Assuming you have a container in your layout
//            parentLayout?.addView(view)
//        }
//
//        unityView = findViewById(com.unity3d.player.R.id.unitySurfaceView)
        enableEdgeToEdge()
        requestBluetoothPermissions()
        requestLocationPermissions()
        setContent {
           MyMaterial3App()

        }
    }
    internal fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Check if BLUETOOTH_CONNECT and BLUETOOTH_SCAN permissions are granted
            val permissionsToRequest = mutableListOf<String>()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }

            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), BLUETOOTH_PERMISSION_REQUEST_CODE)
            } else {
                enableBluetoothAndScan()
            }
        } else {
            enableBluetoothAndScan()
        }
    }
    internal fun enableBluetoothAndScan() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Bluetooth is not available on the device
            println("Bluetooth not supported on this device.")
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is disabled, request enabling
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
        } else {
            // Bluetooth is already enabled, proceed with scanning or other actions
            println("Bluetooth is already enabled.")
        }
    }
    internal fun requestLocationPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            enableLocation()
        }
    }
    @SuppressLint("ServiceCast")
    private fun enableLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            enableLocationLauncher.launch(intent)
        }
    }
    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 2
      //  var instance: MainActivity? = null
        @JvmStatic
        fun onUnityMessage(message: String) {
            println("Message received from Unity: $message")
            when (message) {
                "VL" -> {
                    mainScope.launch {
                        vibrateLeft()
                    }
                }
                "VR" -> {
                    mainScope.launch {
                        vibrateRight()
                    }
                }
            }
        }
        @JvmStatic
        fun exitUnity() {
            println("this is peripheral before exitUnity: ${PeripheralManager.peripheral}")

            try {
                // Ensure unityPlayer is initialized before proceeding
                if (unityPlayer != null) {
                    // Get the Unity view (GLSurfaceView or similar)


                    // Remove Unity view from the screen to stop rendering
                    if (unityView != null) {
                        val parentView = unityView?.parent as? ViewGroup
                        println("Parent view: $parentView")
                        parentView?.removeView(unityView) // Remove Unity's view
                        println("Unity view successfully removed from screen.")
                    } else {
                        println("Unity view is not available.")
                    }
                } else {
                    println("Unity player was not initialized.")
                }
            } catch (e: Exception) {
                println("Error during Unity cleanup: ${e.message}")
                e.printStackTrace()
            }

            println("this is peripheral after exitUnity: ${PeripheralManager.peripheral}")
        }

    }
}
