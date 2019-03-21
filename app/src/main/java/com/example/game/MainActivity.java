package com.example.game;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // sprites
    private TextView score;
    private TextView start;
    private ImageView shark;
    private ImageView bigfish;
    private ImageView smallfish;
    private ImageView bomb;

    // different boxes
    private FrameLayout frame;
    private Button pause;

    // dimensions
    private int flen;
    private int sharkSize;
    private int Width;
    private int Height;

    //coordinates
    private float sharkY;
    private float bigfishX;
    private float smallfishX;
    private float bombX;
    private float bigfishY;
    private float smallfishY;
    private float bombY;

    // classes used in code
    private Handler handler = new Handler();
    private Timer time = new Timer();
    private SoundPlayer sound;

    //flags
    private boolean actionFlag = false;
    private boolean startFlag = false;
    private boolean pauseFlag = true;

    // score
    int keepscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound = new SoundPlayer(this);

        // Get IDs of all everything
        score = (TextView) findViewById(R.id.score);
        start = (TextView) findViewById(R.id.start);
        shark = (ImageView) findViewById(R.id.shark);
        bigfish = (ImageView) findViewById(R.id.bigfish);
        smallfish = (ImageView) findViewById(R.id.smallfish);
        bomb = (ImageView) findViewById(R.id.bomb);
        pause = (Button) findViewById(R.id.pause);

        // Finding dimensions of phone
        Point st = new Point();
        getWindowManager().getDefaultDisplay().getSize(st);
        Width = st.x;
        Height = st.y;

        // Use IDs to set everything behind screen
        bigfish.setX(-80);
        smallfish.setX(-80);
        bomb.setX(-80);
        bigfish.setY(-80);
        smallfish.setY(-80);
        bomb.setY(-80);

        // initialise position
        sharkY = 500;

    }

    public boolean isHit(ImageView sprite, ImageView shark){
        // checks if the shark is hit by any sprite
        float centreX = sprite.getX() + sprite.getWidth()/2;
        float centreY =  sprite.getY()+sprite.getHeight()/2;
        sharkY = shark.getY();
        sharkSize = shark.getHeight();
        if (sprite.getX()>=0 && sharkSize>= centreX && sharkY <=centreY && centreY <= sharkY + sharkSize){
            return true;
        }
        return false;
    }

    public void poschange(){

        // check if hit by any of the sprites
        if (isHit(bigfish,shark)){
            keepscore += 30;
            bigfishX = -10;
            sound.eatFish();
        }

        if (isHit(smallfish,shark)){
            keepscore += 10;
            smallfishX = -10;
            sound.eatFish();
        }

        if (isHit(bomb,shark)){
            time.cancel();
            time = null;
            sound.finishGame();
            // Skip to GameOver page

            Intent newint = new Intent(getApplicationContext(),gameover.class);
            newint.putExtra("USR_SCORE",keepscore);
            startActivity(newint);
        }

        // movement of sprites
        bigfishX = bigfishX-18;
        if (bigfishX<0){
            bigfishX = Width +60;
            bigfishY = (float)Math.random()*(flen - bigfish.getHeight());
        }
        bigfish.setY(bigfishY);
        bigfish.setX(bigfishX);

        smallfishX = smallfishX-14;
        if (smallfishX<0){
            smallfishX = Width +16;
            smallfishY = (float)Math.random()*(flen - smallfish.getHeight());
        }
        smallfish.setY(smallfishY);
        smallfish.setX(smallfishX);

        bombX = bombX-24;
        if (bombX<0){
            bombX = Width +30;
            bombY = (float)Math.random()*(flen - bomb.getHeight());
        }
        bomb.setY(bombY);
        bomb.setX(bombX);

        // movement of shark by user
        if (actionFlag == true){
            sharkY = sharkY -20;
        }
        else{
            sharkY = sharkY+ 20;
        }
        if (sharkY<0){
            sharkY = flen - sharkSize;
        }
        if (sharkY> flen - sharkSize){
            sharkY = 0;
        }
        shark.setY(sharkY);
        // update score
        score.setText("Score : "+Integer.toString(keepscore));
    }
    // on touch event
    public boolean onTouchEvent(MotionEvent t){
        // when the finger touches the phone initially, the if statement is executed
        if (startFlag == false){
            startFlag  = true;
            frame = (FrameLayout) findViewById(R.id.frame);
            flen = frame.getHeight();
            sharkY = shark.getY();
            sharkSize = shark.getHeight();

            start.setVisibility(View.GONE);
            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            poschange();
                        }
                    });
                }
            },0,20);
        }
        else{
            if (t.getAction() == MotionEvent.ACTION_DOWN){
                actionFlag = true;
            }
            else if (t.getAction() == MotionEvent.ACTION_UP){
                actionFlag = false;
            }
        }
        return true;
    }

    public void stopGame(View view){
        if (pauseFlag){
            pauseFlag = false;
            time.cancel();
            time = null;
            pause.setText("START");
        }
        else {
            pauseFlag = true;
            pause.setText("PAUSE");
            time = new Timer();
            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            poschange();
                        }
                    });
                }
            }, 0, 20);
        }
    }

}
