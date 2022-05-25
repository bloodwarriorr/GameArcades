package com.example.gamearcades;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity implements View.OnClickListener {
//create all buttons, welcome headline and firebase objects
    ImageButton sevenBoom,GuessNumber,NewGame,musicBtn;
    boolean isMusicOn=true;
    TextView headLine;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    MediaPlayer gameAudio;

    //handle log out via back button
    @Override
    public void onBackPressed() {
        CreateAlertDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        //initialize btn values
        initBtn();
        initTextView();
        initUserLogin();
        initMediaPlayer();
        musicBtn.setOnClickListener(this);
//make buttons and event listener with implement
        sevenBoom.setOnClickListener(this);
        GuessNumber.setOnClickListener(this);
        NewGame.setOnClickListener(this);
        //get fire base instance
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users").child(mAuth.getUid());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            //Get users first name and print it on top of profile page
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);
                headLine.setText("Welcome"+" "+value.getFullName());

            }
            //display error if could not get the first name from fire base
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
    //init music
    private void initMediaPlayer() {
        gameAudio=MediaPlayer.create(ProfilePage.this,R.raw.bensound_battlefield);
        gameAudio.setLooping(true);
        gameAudio.start();
    }

    //init user login
    private void initUserLogin() {
        mAuth=FirebaseAuth.getInstance();

    }
//init text views
    private void initTextView() {
        headLine=findViewById(R.id.WelcomeHeadline);
    }
//init buttons
    private void initBtn() {
        sevenBoom=findViewById(R.id.SevenBoom);
        GuessNumber=findViewById(R.id.GuessNumber);
        NewGame=findViewById(R.id.NewGame);
        musicBtn=findViewById(R.id.music_btn);

    }
//deal with buttons click-move to a separate activity
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SevenBoom:
                gameAudio.stop();
                startActivity(new Intent(ProfilePage.this, SevenBoom.class));
                break;
            case R.id.GuessNumber:
                gameAudio.stop();
                startActivity(new Intent(ProfilePage.this,Guess_Number.class));
                break;
            case R.id.NewGame:
                gameAudio.stop();
                startActivity(new Intent(ProfilePage.this, ChoiceMenu.class));
                break;
            case R.id.music_btn:
                if(isMusicOn){
                    gameAudio.stop();
                    isMusicOn=false;
                }
                else{
                    initMediaPlayer();
                    isMusicOn=true;
                }

        }
    }
    //deal with resume/pause/destroy audio
    @Override
    protected void onResume() {
        super.onResume();
        gameAudio.stop();
        initMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameAudio.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameAudio.stop();
        gameAudio.release();
    }
    //create sign out dialog
    private void CreateAlertDialog(){
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(ProfilePage.this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure you want to sign out??");
        // Set Alert Title
        builder.setTitle("sign out warning!!");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Sign Out",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                mAuth.signOut();
                                startActivity(new Intent(ProfilePage.this,MainActivity.class));
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "Stay Here",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}