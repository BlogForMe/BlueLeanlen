package com.jonzhou.bluetoothlen;

import android.bluetooth.le.AdvertiseData;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String TAG = "  MainActivity    ";
    private AdvertiseData AdvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final BluetoothAdapter BtAdapter = BluetoothAdapter.getDefaultAdapter();
//        final BluetoothLeAdvertiser BtAdv = BtAdapter.getBluetoothLeAdvertiser();
//
//        final AdvertiseSettings AdvSetting = new AdvertiseSettings.Builder().build();
//         AdvData = new AdvertiseData.Builder().build();
//
//
//        final AdvertiseCallback AdvCBack = new AdvertiseCallback() {
//            @Override
//            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
//                super.onStartSuccess(settingsInEffect);
//                Toast.makeText(getApplicationContext(),"start Advertise ok",Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        findViewById(R.id.bt_open).setOnClickListener(v->{
//            if(BtAdapter.isEnabled()) {
//                BtAdv.startAdvertising(AdvSetting, AdvData, AdvCBack);
//                Toast.makeText(getApplicationContext(),"start Advertise",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(getApplicationContext(),"Bluetooth is not enable!",Toast.LENGTH_SHORT).show();
//            }
//        });


        findViewById(R.id.bt_data).setOnClickListener(v->{
            for (ParcelUuid parcelUuid:AdvData.getServiceUuids()){
                Log.i(TAG,parcelUuid.toString());
            }
        });





    }
}
