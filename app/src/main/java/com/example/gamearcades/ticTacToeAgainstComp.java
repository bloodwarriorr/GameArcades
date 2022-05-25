package com.example.gamearcades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ticTacToeAgainstComp extends AppCompatActivity implements View.OnClickListener {

    //creates the array of buttons
    Button[][] buttons=new Button[3][3];
    //reset button
    Button buttonReset;
    //bool for check if its player1 turn
    boolean playerTurn=true;
    //round counter
    int roundCount;
    //point counters
    int playerPoints,computerPoints;
    //catch each player text view to assemble points
    TextView player;
    TextView computer;
    //save points of players in local storage
    SharedPreferences sharedPref;
    //sound for winning and draw score
    MediaPlayer winning,draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_aganist_comp);
        //init views
        initViews();
        //assign button array values
        initBtnArr();
        //init ui
        initUI();
        //init media player sounds
        initMediaPlayer();
        //init reset button
        buttonReset=findViewById(R.id.resetBtn);
        //set reset button function
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });


    }

    //init music
    private void initMediaPlayer() {
        winning=MediaPlayer.create(ticTacToeAgainstComp.this,R.raw.good_guess);
        draw=MediaPlayer.create(ticTacToeAgainstComp.this,R.raw.draw_score);
    }

    //init the buttons array
    private void initBtnArr() {
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                //assign id per button according to xml id, 01,01,02 and so on
                String buttonID="btn_"+i+j;
                //build extra resource id to pass to find view by id
                int resID=getResources().getIdentifier(buttonID,"id",getPackageName());
                //assign values to button array according to res id
                buttons[i][j]=findViewById(resID);
                //set on click to each button
                buttons[i][j].setOnClickListener(this);


            }
        }
    }
    //init views method
    private void initViews() {
        player=findViewById(R.id.playerScore);
        computer=findViewById(R.id.computerScore);
        sharedPref = ticTacToeAgainstComp.this.getPreferences(Context.MODE_PRIVATE);
    }
    //init the stage number from local storage
    private void initUI() {
        player.setText("Player:"+sharedPref.getInt("Player:",0));
        computer.setText("Computer:"+sharedPref.getInt("Computer:",0));

    }
    //taking care of button array values
    @Override
    public void onClick(View view) {
        //check if button already was clicked and contains an empty string
        if(!((Button)view).getText().toString().equals("")){
            return;
        }
        //players turn

            ((Button)view).setText("X");
             roundCount++;
             if(CheckForWin()){
                 playerWins();
                 return;
             }
             else if(roundCount==9) {
                 draw();
                 return;
             }
             playerTurn=!playerTurn;
             //computer turn
             ComputerMove();
            if(CheckForWin()){
            ComputerWins();
            return;
            }
            else if(roundCount==9) {
             draw();
             return;
            }
            playerTurn=true;

    }

    //a method to if someone won
    private boolean CheckForWin(){
        //create a board to check all the buttons value-if someone has a row or column-he won
        //create a jugged string array to check this
        String[][] field=new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //assign buttons value to the string jugged array
                field[i][j]=buttons[i][j].getText().toString();

            }
        }
        //check the string value(the board if there is a row or column correct) (columns)
        for (int i = 0; i < 3; i++) {
            if(field[i][0].equals(field[i][1])&&field[i][0].equals(field[i][2])&&!field[i][0].equals("")){
                return true;
            }
        }
        //check the string value(the board if there is a row or column correct) (rows)
        for (int i = 0; i < 3; i++) {
            if(field[0][i].equals(field[1][i])&&field[0][i].equals(field[2][i])&&!field[0][i].equals("")){
                return true;
            }
        }
        //checks the right oblique line win
        if(field[0][0].equals(field[1][1])&&field[0][0].equals(field[2][2])&&!field[0][0].equals("")){
            return true;
        }
        //checks the right oblique line win
        if(field[0][2].equals(field[1][1])&&field[0][2].equals(field[2][0])&&!field[0][2].equals("")){
            return true;
        }
        //if none of the terms is true-no one wins yet
        return  false;

    }
    //player 1 win method
    private void playerWins(){
//increment player 1 points and show a message that he won
        playerPoints++;
        Toast.makeText(this,"Player wins!",Toast.LENGTH_SHORT).show();
        //play sound that player is won
        winning.start();
        //method to update the text view of player 1
        updatePointsText();
        //reset board to a new board method
        resetBoard();
    }
    //Computer win method
    private void ComputerWins(){
        //increment Computer points and show a message that he won
        computerPoints++;
        Toast.makeText(this,"Computer wins!",Toast.LENGTH_SHORT).show();
        //play sound that player is won
        winning.start();
        //method to update the text view of Computer
        updatePointsText();
        //reset board to a new board method
        resetBoard();

    }
    //draw situation method
    private void draw(){
        Toast.makeText(this,"Draw!",Toast.LENGTH_SHORT).show();
        //play draw sound
        draw.start();
        resetBoard();
    }
    //create methods of update text view and draw
    private void updatePointsText(){
        //re set the text views text content to the recent points count
        player.setText("Player:"+playerPoints);
        computer.setText("Computer:"+computerPoints);
    }
    //reset board method
    private  void resetBoard(){
//reset board by making all buttons text to an empty string
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <3; j++) {
                buttons[i][j].setText("");
            }
        }
        //reset round count
        roundCount=0;
        //set player 1 turns to true
        playerTurn=true;
        //save stats
        SaveStatsOnLocalStorage();
    }
    //reset game method
    private  void resetGame(){
        //set both player points to 0 and update their text view
        playerPoints=0;
        computerPoints=0;
        updatePointsText();
        //reset board to a new game
        resetBoard();


    }
    //save stats for a situation screen got landscape
    private void SaveStatsOnLocalStorage(){
        //save current stage number in an editor to show which stage is it
        SharedPreferences sharedPref = ticTacToeAgainstComp.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        SharedPreferences.Editor editor2 = sharedPref.edit();
        editor.putInt("Player:",playerPoints);
        editor.apply();
        editor2.putInt("Computer:",computerPoints);
        editor2.apply();
    }
    //taking care of rotate the device to avoid from screen delay to appear
    //method saves all the keys in a state and when we rotate the device it will contain their original situation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount",roundCount);
        outState.putInt("playerPoints",playerPoints);
        outState.putInt("computer",computerPoints);
        outState.putBoolean("playerTurn",playerTurn);
    }
    //gets the instance state situation by the key
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount=savedInstanceState.getInt("roundCount");
        playerPoints=savedInstanceState.getInt("playerPoints");
        computerPoints=savedInstanceState.getInt("computer");
        playerTurn=savedInstanceState.getBoolean("playerTurn");
    }

    private void ComputerMove(){
        int randomNumber=new Random().nextInt(3);
        int randomNumber2=new Random().nextInt(3);

        if(buttons[randomNumber][randomNumber2].getText().toString().equals("")) {
            buttons[randomNumber][randomNumber2].setText("O");
            roundCount++;
            return;
        }
        else{
            ComputerMove();
        }

    }














}

