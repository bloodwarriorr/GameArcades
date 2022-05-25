package com.example.gamearcades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceMenu extends AppCompatActivity implements View.OnClickListener {
    Button single,twoPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_menu);
        single=findViewById(R.id.single);
        twoPlayers=findViewById(R.id.two_players);
        single.setOnClickListener(this);
        twoPlayers.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.single:
                startActivity(new Intent(ChoiceMenu.this, ticTacToeAgainstComp.class));
                break;
            case R.id.two_players:
                startActivity(new Intent(ChoiceMenu.this,TicTacToe.class));
                break;
        }
    }
}