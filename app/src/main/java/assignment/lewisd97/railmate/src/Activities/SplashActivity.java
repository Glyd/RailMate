package assignment.lewisd97.railmate.src.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import assignment.lewisd97.railmate.R;

/**
 * Created by lewisd97 on 08/02/2018.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME = 1000; //in ms
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
