package com.example.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class gameover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        // Get Ids
        TextView gOver = (TextView) findViewById(R.id.gOver);
        TextView yourScore = (TextView) findViewById(R.id.yourScore);
        TextView highScore = (TextView) findViewById(R.id.highScore);
        Button tryAg = (Button) findViewById(R.id.tryAg);

        // Get user score
        int getUsrScore = getIntent().getIntExtra("USR_SCORE",0);
        yourScore.setText("Your Score : " + getUsrScore);

        // Find high score
        SharedPreferences ok  = getSharedPreferences("PREV_SCORES", Context.MODE_PRIVATE);
        int high=  ok.getInt("HIGHSCORE", 0);

        if (getUsrScore > high){
            highScore.setText("Highest Score : " + getUsrScore);

            SharedPreferences.Editor hello = ok.edit();
            hello.putInt("HIGHSCORE",getUsrScore);
            hello.commit();
        }
        else{
            highScore.setText("Highest Score : " + high);
        }
    }

    public void ta(View view){
        // Try again
        Intent skipToMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(skipToMain);
    }
}
