package blueant.hackgt2015;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    ImageView picture;
    Button yami;
    Button yugi;
    Button kaiba;
    ArrayList<String> adapters;
    android.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picture = (ImageView) findViewById(R.id.character);
        yami = (Button) findViewById(R.id.buttonYami);
        yugi = (Button) findViewById(R.id.buttonYugi);
        kaiba = (Button) findViewById(R.id.buttonKaiba);
        addYugi();
        addYami();
        addKaiba();
//        bluetoothConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void addYami() {
        yami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.yami_tb);
            }
        });
    }

    public void addYugi() {
        yugi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.yugi);
            }
        });
    }

    public void addKaiba() {
        kaiba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.kaiba_tb);
            }
        });
    }

    public void startBluetooth() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bluetooth:
                startBluetooth();
                return true;
            default:
                return true;
        }
    }

//    public void bluetoothConnection() {
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            System.out.println("null bluetooth - bilal");
//        }
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                adapters.add(device.getName() + "\n" + device.getAddress());
//            }
//        }
//        System.out.println(adapters);
//    }
}
