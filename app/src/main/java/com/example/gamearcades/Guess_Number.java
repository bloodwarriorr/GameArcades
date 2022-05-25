package com.example.gamearcades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Guess_Number extends AppCompatActivity {
//create an counter for tries, buttons,edit text and othe vars for the game
    static int Tries=10;
    Button btnGuess,ResetGame;
    EditText guessingNumberInput;
    ImageView winOrLoseImg;
    //random number to redraw
    Random r;
    //vars to save random number,tries and stage number
    int randomNumber,tries=Tries,stageNum=1;
//text views of remaining tries and stage number
    TextView remainTries;
    TextView stageNumber;
    //save current score in local storage object
    SharedPreferences sharedPref;
    //win and lose sounds
    MediaPlayer win,loseRound,successRound;
    //boolean to decide if show the next number message or not
    boolean finishFlag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_number);
        //initialize views
        initViews();
        //initialize Variables
        initVarbs();
        //initialize ui
        initUI();
        //init music sounds
        initMediaPlayer();

//set an event listener to guess button
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guessingNumberInput.getText().length()==0)
                    return;
                tries--;
                if(tries==0)
                {
                    Toast.makeText(Guess_Number.this, "Game over",Toast.LENGTH_SHORT).show();
                    btnGuess.setClickable(false);
                    finishFlag=true;
                    stageNum=1;
                    MoveToProfilePage();
                    resetUpdateGame(stageNum,finishFlag);
                    return;
                }

                remainTries.setText("remaines tries:" + tries);
                if(randomNumber == Integer.parseInt(guessingNumberInput.getText().toString()))
                {
                    SetWinOrLoseImage(R.drawable.thumbsup);
                    win.start();
                    if(stageNum==3)
                    {
                        Toast.makeText(Guess_Number.this, "you win the game!!!!!!!!!!",Toast.LENGTH_SHORT).show();
                        finishFlag=true;
                        ResetGame.setClickable(false);
                        successRound.start();
                        MoveToProfilePage();
                        stageNum=0;
                    }
                    stageNum++;
                    resetUpdateGame(stageNum,finishFlag);
                }
                else {
                    SetWinOrLoseImage(R.drawable.frogloser);
                    loseRound.start();
                }
            }
        });
//reset game button event listener
        ResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stageNum=1;
                resetUpdateGame(stageNum,finishFlag);

            }
        });


    }
    //init music
    private void initMediaPlayer() {
        successRound=MediaPlayer.create(Guess_Number.this,R.raw.win_stage);
        win=MediaPlayer.create(Guess_Number.this,R.raw.good_guess);
        loseRound=MediaPlayer.create(Guess_Number.this,R.raw.lose_level);

    }

    //init the stage number from local storage
    private void initUI() {
        stageNum = sharedPref.getInt("stage",1);
        resetUpdateGame(stageNum,finishFlag);
    }
    //set the image according to good/bad guess
    private void SetWinOrLoseImage(int drawable_index){
        winOrLoseImg.setImageResource(drawable_index);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                winOrLoseImg.setImageResource(R.color.transparent);
            }
        },1000);
    }
    //navigate to profile page with 1 sec of delay
    private void MoveToProfilePage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Guess_Number.this,ProfilePage.class));
            }
        },1000);
    }
//init the random vars and save this in the local storage
    private void initVarbs() {
        r=new Random();
        randomNumber=r.nextInt(10);
        sharedPref = Guess_Number.this.getPreferences(Context.MODE_PRIVATE);
    }
//init views by id
    private void initViews() {
        btnGuess=findViewById(R.id.Guess);
        ResetGame=findViewById(R.id.ResetGameId);
        remainTries=findViewById(R.id.Remain_tries);
        remainTries.setText("Guessing remain:"+tries);
        guessingNumberInput=findViewById(R.id.Guess_Number_Input);
        stageNumber=findViewById(R.id.Stage_Number);
        winOrLoseImg=findViewById(R.id.ShowWinOrLose);
    }
//reset the amount of tries number, get the current stage number from local storage
    private void resetUpdateGame(int stageNum,boolean finishFlag) {
        switch(stageNum)
        {
            case 1: {tries=Tries;  randomNumber = r.nextInt(10); }  break;
            //redraw higher random number than before
            case 2: {tries=7;  randomNumber = r.nextInt(15)+5; } break;
            //redraw higher random number than before
            case 3: {tries=5;  randomNumber = r.nextInt(20)+10;} break;

        }
        //save stage
        SaveCurrentStage();
        //update values
        UpdateGameValues(finishFlag);
    }
    //save current stage number in an editor to show which stage is it
    private void SaveCurrentStage(){
        SharedPreferences sharedPref = Guess_Number.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("stage",stageNum);
        editor.apply();

    }
    //update values for game
    private void UpdateGameValues(boolean finishFlag){
        stageNumber.setText("stage level:" + stageNum);
        remainTries.setText("remaines tries:" + tries);
        //make reset game button clickbale again
        ResetGame.setClickable(true);
        if (!finishFlag) {
            //show us the next random number
            Toast.makeText(Guess_Number.this, "Hint" +" "+ randomNumber, Toast.LENGTH_SHORT).show();
        }


    }


}