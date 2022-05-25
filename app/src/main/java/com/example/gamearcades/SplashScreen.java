package com.example.gamearcades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {


    private static final int SPLASH_SCREEN_DELAY = 3000;
    ImageView icon_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        icon_screen=findViewById(R.id.imageView);
        icon_screen.animate().rotation(360f).setDuration(SPLASH_SCREEN_DELAY).start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));

            }
        },SPLASH_SCREEN_DELAY);




    }
}