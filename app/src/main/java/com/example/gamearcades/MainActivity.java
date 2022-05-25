package com.example.gamearcades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//create login and sign up buttons, text views, password and email fields, firebase object
    Button btnLogin, btnSignUp;
    ImageButton musicBtn;
    boolean isMusicOn=true;
    TextView emailInput, PassInput;
    String password, email;
    FirebaseAuth mAuth;
    MediaPlayer gameIntroAudio;
    //handle exit from app with dialog
    @Override
    public void onBackPressed() {
        CreateAlertDialog();
    }
    //handle with user that is already logged in-if so-move to profile directly
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,ProfilePage.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init buttons, vars and text views,music
        initBtn();
        initVarbs();
        initTextViews();
        initMediaPlayer();
        musicBtn.setOnClickListener(this);
        //set an event listener to the login button
        btnLogin.setOnClickListener(this);

//set an event listener to the sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });
    }
//init media sounds
    private void initMediaPlayer() {
        gameIntroAudio=MediaPlayer.create(MainActivity.this,R.raw.bensound_anewquest);
        gameIntroAudio.setLooping(true);
        gameIntroAudio.start();
    }

    //init text views
    private void initTextViews() {
        emailInput = findViewById(R.id.userEmailInput);
        PassInput = findViewById(R.id.userEmailPassword);
    }
//init buttons
    private void initBtn() {
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        musicBtn=findViewById(R.id.music_btn);
    }
//init firebase var
    private void initVarbs() {
        mAuth=FirebaseAuth.getInstance();
    }
//deal with login click button
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                logIn();
                break;
            case R.id.music_btn:
                if(isMusicOn){
                    gameIntroAudio.stop();
                    isMusicOn=false;
                }
                else{
                    initMediaPlayer();
                    isMusicOn=true;
                }
                break;

        }
    }
//login method-catch the email and password input, and sign in to firebase data base.
    private void logIn() {
        email=emailInput.getText().toString();
        password=PassInput.getText().toString();
        if(email.isEmpty()||password.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Cant leave empty fields!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            //stop intro music to make sure profile set his theme music
                            gameIntroAudio.stop();
                          startActivity(new Intent(MainActivity.this,ProfilePage.class));


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                });
    }
    //deal with resume/pause/destory audio
    @Override
    protected void onResume() {
        super.onResume();
        gameIntroAudio.stop();
        initMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameIntroAudio.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameIntroAudio.stop();
        gameIntroAudio.release();
    }

    //create an alert dialog
    private void CreateAlertDialog(){
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Wait!");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                finishAffinity();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
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

