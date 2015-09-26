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
        picture = (ImageView) findViewById(R.id.kaiba);
//        iv.setOnClickListener(this);
    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.
//        }
//    }


}
