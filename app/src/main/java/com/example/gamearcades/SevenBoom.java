package com.example.gamearcades;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SevenBoom extends AppCompatActivity {

//init click counts and text view
    private Integer click=0;
    ImageView explodeImage;
    TextView counter;
    MediaPlayer boomSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_boom);
        //init the counter
        counter=findViewById(R.id.counter);
        //init image view
        explodeImage=findViewById(R.id.explodeImage);
        initMediaPlayer();
//create an event listener to the roll button
        findViewById(R.id.SevenBoom).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                click++;
                counter.setText(""+click);
                if(click%7==0){
                    Toast.makeText(SevenBoom.this,"Boom!!!!",Toast.LENGTH_LONG).show();
                    boomSound.start();
                    SetExplodeImage(R.drawable.explode);

                } else if (click.toString().contains("7")) {
                    Toast.makeText(SevenBoom.this,"Boom!!!!",Toast.LENGTH_LONG).show();
                    boomSound.start();
                    SetExplodeImage(R.drawable.explode);
                }
            }
        });
    }
    //init sound
    private void initMediaPlayer() {
        boomSound=MediaPlayer.create(SevenBoom.this,R.raw.explosion_sound);
    }

    //set the image according to good/bad guess
    private void SetExplodeImage(int drawable_index){
        explodeImage.setImageResource(drawable_index);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                explodeImage.setImageResource(R.color.transparent);
            }
        },1000);
    }
}