package com.jonzhou.bluetoothlen.transport;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class  BlueAdapter extends BaseAdapter {

    private List<BluetoothDevice> mLeDevices;
    private LayoutInflater mInflator;

    public BlueAdapter() {
        mLeDevices = new LinkedList<>();
    }

    public  void  addDevice(BluetoothDevice device){
        if (!mLeDevices.contains(device)){
//            if (!(device.getBondState()==BluetoothDevice.BOND_BONDED))
            mLeDevices.add(device);
            notifyDataSetChanged();
        }
    }

    public void replace(BluetoothDevice device) {
        if (mLeDevices.contains(device)){
            mLeDevices.remove(device);
            mLeDevices.add(device);
        }
        notifyDataSetChanged();
    }

    public  BluetoothDevice getDevice(int position){
        return  mLeDevices.get(position);
    }

    public  void  clear(){
        if (mLeDevices!=null)
            mLeDevices.clear();
    }

    @Override
    public int getCount() {
        if (mLeDevices==null)
            return 0;
        return  mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1,viewGroup,false);
        BluetoothDevice device = mLeDevices.get(i);
        ((TextView)view).setText(device.getName()+"      " + device.getAddress()+"  " + (device.getBondState() == BluetoothDevice.BOND_BONDED ? "已绑定" : "未绑定") );

        return view;
    }


}