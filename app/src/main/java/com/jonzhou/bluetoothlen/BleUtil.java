package com.jonzhou.bluetoothlen;

/**
 * Created by zxx on 2016/7/19.
 */
public class BleUtil {

    private static BleUtil INSTANCE;

    private BleUtil() {
    }

    public static BleUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (BleUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BleUtil();
                }
            }
        }
        return INSTANCE;
    }

//    public boolean isGet(String name) {
//        return Arrays.asList(deviceNames).contains(name);
//    }

//    String[] deviceNames = {BRAND_XTY_YUYUELL, BRAND_TWJ_TIDA_01, BRAND_XYY_LK, BRAND_XYJ_BIOLAND, BRAND_XYJ_MAIBOBO, BRAND_XTY_YUYUELL_YE8600A};


//    public static ArrayMap<String, Integer> initDeviceType() {
//        ArrayMap<String, Integer> map = new ArrayMap<>();
//        map.put(BRAND_XTY_YUYUELL, TYPE_MODE_XTY);
//        map.put(BRAND_TWJ_TIDA_01, TYPE_MODE_TWJ);
//        map.put(BRAND_XYY_LK, TYPE_MODE_XYY);
//        map.put(BRAND_XYJ_BIOLAND, TYPE_MODE_XYJ);
//        map.put(BRAND_XTY_YUYUELL_YE8600A, TYPE_MODE_XYJ);
//        map.put(BRAND_XYJ_MAIBOBO, TYPE_MODE_XYJ);
//        return map;
//    }
//
//
//    public static ArrayMap<Integer, String> getNameKey() {
//        ArrayMap<Integer, String> map = new ArrayMap<>();
//        map.put(TYPE_MODE_XTY, BRAND_XTY_YUYUELL);
//        map.put(TYPE_MODE_TWJ, BRAND_TWJ_TIDA_01);
//        map.put(TYPE_MODE_XYY, BRAND_XYY_LK);
//        map.put(TYPE_MODE_XYJ, BRAND_XYJ_MAIBOBO);
//        return map;
//    }
//
//    public static ArrayMap<Integer, String> getAlias() {
//        ArrayMap<Integer, String> map = new ArrayMap<>();
//        map.put(TYPE_MODE_XTY, TYPE_MODE_XTY_ALIAS);
//        map.put(TYPE_MODE_TWJ, TYPE_MODE_TWJ_ALIAS);
//        map.put(TYPE_MODE_XYY, TYPE_MODE_XYY_ALIAS);
//        map.put(TYPE_MODE_XYJ, TYPE_MODE_XYJ_ALIAS);
//        return map;
//    }

    //    public static int  brand = 1 ;  //  区分品牌   爱奥乐血糖仪-1  倍儿康体温计- 2  体达-3  力康 4   爱奥乐血压 5   脉搏波血压 6
    public static final String BRAND_XYJ_BIOLAND = "Bioland-BPM"; //Bioland-BPM              爱奥乐血压计
    //    public static final String BRAND_XTY_YUYUELL = "Bioland-BGM"; //Bioland-BGM  44:A6:E5:1A:43:3E //爱奥乐血糖仪
    public static final String BRAND_XTY_YUYUELL = "Yuwell Glucose"; //Bioland-BGM  44:A6:E5:1A:43:3E //爱奥乐血糖仪
    public static final String BRAND_TWJ_TIDA_01 = "Bluetooth BP"; //Bluetooth BP  TD133
    public static final String BRAND_BENE_CHECK = "BeneCheck TC-B DONGLE"; //百捷
    public static final String BRAND_XYY_LK = "PC-60NW-1"; // PC-60NW-1                      力康血氧仪
    public static final String BRAND_XYJ_MAIBOBO = "BP06"; //BP06D21801040195    maibobo血压计  名字不同
    public static final String BRAND_XTY_YUYUELL_YE8600A = "Yuwell BP-YE8600A"; //Bioland-BGM  44:A6:E5:1A:43:3E //爱奥乐血糖仪
    public static final String BRAND_XTY_YUYUELL_YE650A = "Yuwell BP-YE650A"; //Bioland-BGM  44:A6:E5:1A:43:3E //爱奥乐血糖仪
    public static final String BRAND_XTY_YUYUELL_YE680A = "Yuwell BP-YE680A"; //Bioland-BGM  44:A6:E5:1A:43:3E //爱奥乐血糖仪
    public static final String BRAND_ELEC_LK = "PC80B-BLE"; //Bioland-BGM  44:A6:E5:1A:43:3E //爱奥乐血糖仪


//    public static final String TYPE_MODE_XTY_ALIAS = "鱼跃血糖仪"; //  血糖仪
//    public static final String TYPE_MODE_TWJ_ALIAS = "体达体温计"; //体温计
//    public static final String TYPE_MODE_XYY_ALIAS = "瑞康血氧仪"; //血氧仪
//    public static final String TYPE_MODE_XYJ_ALIAS = "脉搏波血压计"; //血压计

//    public static final int TYPE_MODE_TWJ = 0x11; //体温计
//    public static final int TYPE_MODE_XYY = 0x22; //血氧仪
//    public static final int TYPE_MODE_XYJ = 0x33; //血压计
//    public static final int TYPE_MODE_XTY = 0x44; //  血糖仪
//    public static final int TYPE_MODE_BENE = 0x55; //百捷
//    public static final int TYPE_MODE_ELEC = 0x66;  //心电
//    public static final int TYPE_MODE_ZG = 0x77; //足感
//    public static final int TYPE_MODE_PZ = 0x88; //拍照

//    public BleDevice scanDevice(int modeDevice, String deviceName, BluetoothDevice scanResult) {
//        synchronized (BleUtil.class) {
//            BleDevice bleDevice = new BleDevice(scanResult, modeDevice, getAlias().get(modeDevice));
//            Timber.i(deviceName + "   stopScan 停止扫描 ");
//            return bleDevice;
//        }
//    }


//    public List<InteDevice> getDevices() {
//        List<InteDevice> inteList = new LinkedList<>();
//        /**
//         * 脉搏波血压计
//         */
//        InteDevice inteDevice = new InteDevice();
//        inteDevice.setDeviceName(BRAND_XYJ_MAIBOBO).setDeviceAlias(TYPE_MODE_XYJ_ALIAS).setMode(TYPE_MODE_XYJ);
//        inteList.add(inteDevice);
//        /**
//         * 血糖仪
//         */
//        inteDevice = new InteDevice();
//        inteDevice.setDeviceName(BRAND_XTY_YUYUELL).setDeviceAlias(TYPE_MODE_XTY_ALIAS).setMode(TYPE_MODE_XTY);
//        inteList.add(inteDevice);
//        //血氧
//        inteDevice = new InteDevice();
//        inteDevice.setDeviceName(BRAND_XYY_LK).setDeviceAlias(TYPE_MODE_XYY_ALIAS).setMode(TYPE_MODE_XYY);
//        inteList.add(inteDevice);
//        //体温计
//        inteDevice = new InteDevice();
//        inteDevice.setDeviceName(BRAND_TWJ_TIDA_01).setDeviceAlias(TYPE_MODE_TWJ_ALIAS).setMode(TYPE_MODE_TWJ);
//        inteList.add(inteDevice);
//        return inteList;
//    }

    //打印hex日志
    public static String bytes2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            sb.append(tmp).append(" ");
        }
        return sb.toString();
    }


}
