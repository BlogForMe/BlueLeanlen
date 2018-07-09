package com.jonzhou.bluetoothlen.transport;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jonzhou.bluetoothlen.BytePrintUtil;
import com.jonzhou.bluetoothlen.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn
 * 蓝牙协议
 */
public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public final String TAG = getClass().getSimpleName();

    public static final String GATT_SERVICE_PRIMARY_1 = "0000fff0-0000-1000-8000-00805f9b34fb";  // 体达 体温计
    public static final String CHARACTERISTIC_NOTIFY_1 = "0000fff1-0000-1000-8000-00805f9b34fb";

    public static final UUID CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    private static final int REQUEST_ENABLE_BT = 666;
    private static final int REQUEST_PERMISSION_ACCESS_LOCATION = 777;//权限
    private BluetoothAdapter mBluetoothAdapter;
    private ListView lvBlue;
    private BlueAdapter blueAdapter;

    private Handler mHandler;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private boolean mScanning;
    private BluetoothGatt bluetoothGatt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        lvBlue = findViewById(R.id.lv_blue);
        blueAdapter = new BlueAdapter();
        lvBlue.setAdapter(blueAdapter);
        lvBlue.setOnItemClickListener(this);
        //android 5.0以下
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        // Register the BroadcastReceiver

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mOirginReceiver, intentFilter);// Don't forget to unregister during onDestroy


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1000:
                        blueAdapter.notifyDataSetChanged();
                        Log.i(TAG, " 刷新数据");
                        break;
                }
            }
        };

        findViewById(R.id.bt_search_blue).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_blue://打开蓝牙
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                break;
            case R.id.bt_search_blue:
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(this, "开启蓝牙", Toast.LENGTH_SHORT).show();
                }
                requestPermission();
                break;
        }
    }


    /**
     * 判断设备是否支持蓝牙权限
     *
     * @param view
     */
    public void btSupport(View view) {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "设备不支持蓝牙权限", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "支持设备", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        int checkAccessPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkAccessPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION_ACCESS_LOCATION);
//            Timber.e("没有权限，请求权限");
            return;
        } else {
//            searchOrigin();//android4.3以下 ????  不确定
//             scanLeDevice(true); //android4.3 -- android5.0
            scanDevices();
        }
//        Timber.i("已有定位权限");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Timber.d("开启权限permission granted!");
//                    searchOrigin();
//                    scanLeDevice(true);
                    scanDevices();
                } else {
//                    Timber.d("没有定位权限，请先开启!");
                }
            }
        }
    }

    /**
     * 打开蓝牙
     *
     * @param view
     */
    public void btStartBlue(View view) {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                Log.i("devices", device.getName() + "  " + device.getAddress());
            }
        }
    }

    /**
     * 已经配对的设备
     *
     * @param view
     */
    public void btBondDevices(View view) {
        Set<BluetoothDevice> bondDevices = mBluetoothAdapter.getBondedDevices();

    }


    private final BroadcastReceiver mOirginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView

                blueAdapter.addDevice(device);
                Log.i(TAG, device.getName() + "      " + device.getAddress());
            }
        }
    };


    /**
     * android5.0以上使用
     */
    private void scanDevices() {
        BluetoothLeScanner mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothScanner.startScan(scanCallback);
    }

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //把byte数组转成16进制字符串，方便查看
            Log.e(TAG, "onScanResult :" + result.getScanRecord().toString());
            BluetoothDevice device = result.getDevice();
            ScanRecord scanRecord = result.getScanRecord();
            int rssi = result.getRssi();
            blueAdapter.addDevice(device);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "搜索失败");
        }
    };


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        bluetoothGatt = blueAdapter.getDevice(position).connectGatt(this, false, mGattCallback);
    }


    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * Callback indicating when GATT client has connected/disconnected to/from a remote GATT server
         * @param gatt     返回连接建立的gatt对象
         * @param status   返回的是此次gatt操作的结果，成功了返回0
         * @param newState 每次client连接或断开连接状态变化，STATE_CONNECTED 0，STATE_CONNECTING 1,STATE_DISCONNECTED 2,STATE_DISCONNECTING 3
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();        //连接成功， 开始搜索服务
                Log.i(TAG, "onConnectionStateChange  连接成功" + status);
                Toast.makeText(BluetoothActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Callback invoked when the list of remote services, characteristics and descriptors for the remote device have been updated, ie new services have been discovered.
         * @param gatt   返回的是本次连接的gatt对象
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i(TAG, "onServicesDiscovered status" + status);
            List<BluetoothGattService> mServiceList = gatt.getServices();
            for (BluetoothGattService service : mServiceList) {
                Log.i(TAG, "onServicesDiscovered " + service.getUuid());
            }
            BluetoothGattService service = gatt.getService(UUID.fromString(GATT_SERVICE_PRIMARY_1));
            BluetoothGattCharacteristic characterisetic = service.getCharacteristic(UUID.fromString(CHARACTERISTIC_NOTIFY_1));
            //调用以便当命令发送后返回信息可以自动返回
            BluetoothGattDescriptor descriptor = characterisetic.getDescriptor(CCC);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
            boolean isNotify = gatt.setCharacteristicNotification(characterisetic, true);

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(TAG, gatt.getDevice().getName() + " write successfully");
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(TAG, gatt.getDevice().getName() + " recieved ");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, "The response is " + "onCharacteristicChanged" + BytePrintUtil.bytes2hex(characteristic.getValue()));
        }
    };


    /**
     * android 4.3 --  android5.0使用的扫描方法
     * @param enable
     */
//        private void scanLeDevice(boolean enable) {
//            if (enable) {
//                // Stops scanning after a pre-defined scan period.
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mScanning = false;
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    }
//                }, SCAN_PERIOD);
//                mScanning = true;
//                mBluetoothAdapter.startLeScan(mLeScanCallback);
//            } else {
//                mScanning = false;
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            }
//        }

    // Device scan callback.
//        private BluetoothAdapter.LeScanCallback mLeScanCallback =
//                new BluetoothAdapter.LeScanCallback() {
//                    @Override
//                    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
//                        blueAdapter.addDevice(bluetoothDevice);
//                    }
//                };

    /**
     * //应该是android 4.3以下查找蓝牙设备的方式
     */
    private void searchOrigin() {
        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
        Log.i(TAG, "开始搜索");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOirginReceiver != null) {
            unregisterReceiver(mOirginReceiver);
        }
        if (mBluetoothAdapter != null)
            mBluetoothAdapter.cancelDiscovery();
    }


}
