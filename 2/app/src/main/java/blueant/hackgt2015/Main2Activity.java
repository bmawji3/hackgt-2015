package blueant.hackgt2015;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Found this code from
 * https://github.com/adrianstevens/Edison_to_Android_BLE/tree/master/Android/BLEConnect
 */
public class Main2Activity extends ActionBarActivity {

    //these values are specific to the Grove BLE V1 - update if you're using a different module
    private static String EDISON_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static String CHARACTERISTIC_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static String CHARACTERISTIC_RX = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 5000; //5 seconds
    private static final String DEVICE_NAME = "EdisonHackGT"; //display name for Edison BLE

    private BluetoothAdapter mBluetoothAdapter;//our local adapter
    private BluetoothGatt mBluetoothGatt; //provides the GATT functionality for communication
    private BluetoothGattService mBluetoothGattService; //service on mBlueoothGatt
    private static List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();//discovered devices in range
    private BluetoothDevice mDevice; //external BLE device (Grove BLE module)

    private Timer mTimer;
    private TextView textView;
    private TextView servo1Text;
    private TextView servo2Text;
    private TextView servo3Text;
    private Button servo1plus;
    private Button servo1minus;
    private Button servo2plus;
    private Button servo2minus;
    private Button servo3plus;
    private Button servo3minus;
    private int count1;
    private int count2;
    private int count3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTimer = new Timer();

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Hello Bluetooth LE!");
        textView.setMovementMethod(new ScrollingMovementMethod());

        //check to see if Bluetooth Low Energy is supported on this device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }

        statusUpdate("BLE supported on this device");

        //get a reference to the Bluetooth Manager
        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BLE not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Open settings if Bluetooth isn't enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth disabled", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        servo1Text = (TextView) findViewById(R.id.servo1Text);
        servo2Text = (TextView) findViewById(R.id.servo2Text);
        servo3Text = (TextView) findViewById(R.id.servo3Text);

        servo1plus = (Button) findViewById(R.id.servo1plus);
        servo1minus = (Button) findViewById(R.id.servo1minus);
        servo2plus = (Button) findViewById(R.id.servo2plus);
        servo2minus = (Button) findViewById(R.id.servo2minus);
        servo3plus = (Button) findViewById(R.id.servo3plus);
        servo3minus = (Button) findViewById(R.id.servo3minus);

        addListeners();

        servo1Text.setText("" + getCount1());
        servo2Text.setText("" + getCount2());
        servo3Text.setText("" + getCount3());

        //try to find the Grove BLE V1 module
        searchForDevices();
    }

    private void searchForDevices () {
        statusUpdate("Searching for devices ...");

        if(mTimer != null) {
            mTimer.cancel();
        }

//        scanLeDevice();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                statusUpdate("Search complete");
                findGroveBLE();
            }
        }, SCAN_PERIOD);
    }

    private void findGroveBLE () {
        if(mDevices == null || mDevices.size() == 0) {
            statusUpdate("No BLE devices found");
            statusUpdate("BLE Devices found: " + mDevices.size());
            return;
        }
        else if(mDevice == null) {
            statusUpdate("Unable to find Edison BLE");
            return;
        }
        else {
            statusUpdate("Found Edison BLE");
            statusUpdate("Address: " + mDevice.getAddress());
            connectDevice();
        }
    }

    private boolean connectDevice () {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mDevice.getAddress());
        if (device == null) {
            statusUpdate("Unable to connect");
            return false;
        }
        // directly connect to the device
        statusUpdate("Connecting ...");
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        return true;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                statusUpdate("Connected");
                statusUpdate("Searching for services");
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                statusUpdate("Device disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();

                for(BluetoothGattService gattService : gattServices) {
                    statusUpdate("Service discovered: " + gattService.getUuid());
                    if(EDISON_SERVICE.equals(gattService.getUuid().toString())) {
                        mBluetoothGattService = gattService;
                        statusUpdate("Found communication Service");
                        sendMessage();
                    }
                }
            } else {
                statusUpdate("onServicesDiscovered received: " + status);
            }
        }
    };

    private void sendMessage () {

        if (mBluetoothGattService == null)
            return;

        statusUpdate("Finding Characteristic...");
        BluetoothGattCharacteristic gattCharacteristic =
                mBluetoothGattService.getCharacteristic(UUID.fromString(CHARACTERISTIC_TX));

        if(gattCharacteristic == null) {
            statusUpdate("Couldn't find TX characteristic: " + CHARACTERISTIC_TX);
            return;
        }

        statusUpdate("Found TX characteristic: " + CHARACTERISTIC_TX);

        statusUpdate("Sending message 'Hello Edison BLE'");

        String msg = "Hello Edison BLE";

        byte b = 0x00;
        byte[] temp = msg.getBytes();
        byte[] tx = new byte[temp.length + 1];
        tx[0] = b;

        for(int i = 0; i < temp.length; i++)
            tx[i+1] = temp[i];

        gattCharacteristic.setValue(tx);
        mBluetoothGatt.writeCharacteristic(gattCharacteristic);
    }

//    private void scanLeDevice() {
//        new Thread() {
//
//            @Override
//            public void run() {
//                mBluetoothAdapter.startLeScan(mLeScanCallback);
//
//                try {
//                    Thread.sleep(SCAN_PERIOD);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            }
//        }.start();
//    }

//    private BluetoothAdapter.LeScanCallback mLeScanCallback
//            = new BluetoothAdapter.LeScanCallback() {
//
//        @Override
//        public void onLeScan(final BluetoothDevice device, final int rssi,
//                             byte[] scanRecord) {
//            if (device != null) {
//                //to avoid duplicate entries
//                if (mDevices.indexOf(device) == -1) {
//                    if (DEVICE_NAME.equals(device.getName())) {
//                        mDevice = device;//we found our device!
//                    }
//                    mDevices.add(device);
//                    statusUpdate("Found device " + device.getName());
//                }
//            }
//        }
//    };

    //output helper method
    private void statusUpdate (final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.w("BLE", msg);
                textView.setText(textView.getText() + "\r\n" + msg);
            }
        });
    }

    private void buttonPressed (final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("Button clicked", msg);
                textView.setText(textView.getText() + "\r\n" + msg);
            }
        });
    }

    private void addListeners () {
        servo1plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("clicked servo1plus");
                setCount1(1);
                servo1Text.setText("" + getCount1());
            }
        });
        servo1minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("clicked servo1minus");
                setCount1(-1);
                servo1Text.setText("" + getCount1());
            }
        });
        servo2plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("clicked servo2plus");
                setCount2(1);
                servo2Text.setText("" + getCount2());
            }
        });
        servo2minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("clicked servo2minus");
                setCount2(-1);
                servo2Text.setText("" + getCount2());
            }
        });
        servo3plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("clicked servo3plus");
                setCount3(1);
                servo3Text.setText("" + getCount3());
            }
        });
        servo3minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("clicked servo3minus");
                setCount3(-1);
                servo3Text.setText("" + getCount3());
            }
        });
    }

    public int getCount1() { return count1; }

    public void setCount1(int count1) {
        this.count1 += count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 += count2;
    }

    public int getCount3() {
        return count3;
    }

    public void setCount3(int count3) {
        this.count3 += count3;
    }
}
