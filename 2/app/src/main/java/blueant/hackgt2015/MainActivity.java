package blueant.hackgt2015;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picture = (ImageView) findViewById(R.id.character);
        yami = (Button) findViewById(R.id.buttonYami);
        yugi = (Button) findViewById(R.id.buttonYugi);
        kaiba = (Button) findViewById(R.id.buttonKaiba);
        addResources();
    }

    public void addResources() {
        yami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.yami);
            }
        });
        yugi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.yugi);
            }
        });
        kaiba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.kaiba);
            }
        });
    }
}
