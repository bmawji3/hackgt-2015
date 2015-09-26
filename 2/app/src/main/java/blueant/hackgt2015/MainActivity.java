package blueant.hackgt2015;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

    ImageView picture;
    Button yami;
    Button yugi;
    Button kaiba;
    int REQUEST_ENABLE_BT;

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
        bluetoothConnection();
    }

    public void addYami() {
        yami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.yami);
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
                picture.setImageResource(R.drawable.kaiba);
            }
        });
    }

    public void bluetoothConnection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            System.out.println("null bluetooth - bilal");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
}