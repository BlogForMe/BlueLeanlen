package com.jonzhou.bluetoothlen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.casanube.scan.BleManager
import com.casanube.scan.BluetoothScanManager
import com.casanube.scan.ScanOverListener
import com.casanube.scan.blecompat.ScanCallbackCompat
import com.casanube.scan.blecompat.ScanFilterCompat
import com.casanube.scan.blecompat.ScanResultCompat
import com.casanube.scan.device.BleParamsOptions
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_scan.*

const val BRAND_TWJ_TIDA_01 = "Bluetooth BP" //Bluetooth BP  TD133

const val BRAND_XTY_YUYUELL_YE8600A = "Yuwell BP-YE8600A"
const val BRAND_XTY_YUYUELL_YE650A = "Yuwell BP-YE650A"
const val BRAND_XTY_YUYUELL_YE680A = "Yuwell BP-YE680A"

class ScanActivity : AppCompatActivity() {
    private var scanManager: BluetoothScanManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

//        BleManager.setBleParamsOptions(ConstValue.getBleOptions(this));
        BleManager.setBleParamsOptions(BleParamsOptions.Builder() //                .setBackgroundBetweenScanPeriod(5 * 60 * 1000)
                //                .setBackgroundScanPeriod(10000)
                .setForegroundBetweenScanPeriod(10000)
                .setForegroundScanPeriod(10000)
                .setDebugMode(BuildConfig.DEBUG) //                .setMaxConnectDeviceNum(5)
                //                .setReconnectBaseSpaceTime(8000)
                //                .setReconnectMaxTimes(Integer.MAX_VALUE)
                //                .setReconnectStrategy(ConnectConfig.RECONNECT_LINE_EXPONENT)
                //                .setReconnectedLineToExponentTimes(5)
                .setConnectTimeOutTimes(20000)
                .build())
        initScan();
        bt_scan.setOnClickListener {
            Log.d("scanManager", "scanManager")
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                            当某条权限之前已经请求过，并且用户已经拒绝了该权限时，shouldShowRequestPermissionRationale ()方法返回的是true

                } else {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 5);
                }
            } else {
                scanManager?.startScanNow()
            }
        }

        bt_stop.setOnClickListener {
            scanManager?.stopCycleScan()
        }
    }

    fun initScan() {
        scanManager = BluetoothScanManager.getInstance(this)
        scanManager?.addScanFilterCompats(ScanFilterCompat.Builder().setDeviceName(BRAND_TWJ_TIDA_01).build())
        scanManager?.setScanOverListener(object : ScanOverListener {
            override fun onScanOver() {

            }
        })
        scanManager?.setScanCallbackCompat(object : ScanCallbackCompat() {
            override fun onScanResult(callbackType: Int, result: ScanResultCompat?) {
                super.onScanResult(callbackType, result)
                val deviceName = result?.scanRecord?.deviceName
                Logger.i("scan device " + result?.getLeDevice()?.getAddress() + " " + deviceName);
            }

            override fun onBatchScanResults(results: MutableList<ScanResultCompat>?) {
                super.onBatchScanResults(results)
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        scanManager?.stopCycleScan()
    }
}
