package liamkengineering.led_matrix;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;





public class MainActivity extends AppCompatActivity {

    private final Handler h = new Handler(); // handler and runnable for intro screen
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(MainActivity.this, MainScreen.class);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h.postDelayed(r,1500);
    }


}
