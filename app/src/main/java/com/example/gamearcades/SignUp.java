package com.example.gamearcades;

import static android.widget.Toast.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
//create sign up button, fields text views
    Button btn;
    //email and password pattern regex
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    Pattern passPattern=Pattern.compile("^" + "(?=.*[@#$%^&+=_0-9])" + "(?=\\S+$)" + ".{4,}" + "$");
    boolean isMusicOn=true;
    ImageButton musicBtn;
    EditText userEmail,UserPassword,UserRePassword,UserPhoneNumber,userFirstName;
    //create firebase object to sign up and update real time data base
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    MediaPlayer gameIntroAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //init all program vars and buttons
        initViews();
        initBtn();
        initVars();
        initMediaPlayer();
        musicBtn.setOnClickListener(this);

    }
//init firebase vars
    private void initVars() {
        mAuth=FirebaseAuth.getInstance();
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

    }
//init sign up button and make him an event listener
    private void initBtn() {
        btn=findViewById(R.id.btnSignUp);
        btn.setOnClickListener(this);
        musicBtn=findViewById(R.id.music_btn);
    }
//init all views
    private void initViews() {
        userEmail=findViewById(R.id.userEmailInput);
        UserPassword=findViewById(R.id.userPasswordInput);
        UserRePassword=findViewById(R.id.userRePassword);
        UserPhoneNumber=findViewById(R.id.userPhoneNumber);
        userFirstName=findViewById(R.id.userName);


    }
//deal with sign up fields
    //sign up action
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp: {
                if(RegCheck()==false){
                    return;
                }
                SignUpMethod();
                break;
            }
            case R.id.music_btn:
                if(isMusicOn){
                    gameIntroAudio.stop();
                    isMusicOn=false;
                }
                else{
                    initMediaPlayer();
                    isMusicOn=true;
                }
        }
    }
//sign up method-with email and password auth, move to the login page
    private void SignUpMethod() {

        String email=userEmail.getText().toString();
        String password=UserPassword.getText().toString();
        String repass=UserRePassword.getText().toString();
        if(!EmailAndPassValidation(email,password)){
            Toast.makeText(SignUp.this, "Password or email are not in the correct format.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        User user=new User(userFirstName.getText().toString(),UserPhoneNumber.getText().toString());
        if (password.equals(repass)) {

            SignUpToFireBase(email,password,user);
        }
        else{
            makeText(SignUp.this, "Passwords not match!",
                    LENGTH_SHORT).show();
        }
    }
//check for empty fields
    private boolean RegCheck() {

        if(userEmail.getText().toString().isEmpty() ||UserPassword.getText().toString().isEmpty() || UserRePassword.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "cant leave empty values!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;


    }
    //method to sign up user to fire base
    private void SignUpToFireBase(String email,String password,User user){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //save user to the database
                            myRef.child(mAuth.getUid()).setValue(user);
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this,MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //email and pass validation
    private boolean EmailAndPassValidation(String email,String password){
        if(pattern.matcher(email).matches()&&passPattern.matcher(password).matches()){
            return true;
        }
        return  false;
    }
    //init media sounds
    private void initMediaPlayer() {
        gameIntroAudio= MediaPlayer.create(SignUp.this,R.raw.bensound_anewquest);
        gameIntroAudio.setLooping(true);
        gameIntroAudio.start();
    }
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


}
