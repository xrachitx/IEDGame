package com.example.game;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int biteFish;
    private static int explosion;

    public SoundPlayer(Context context){

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        biteFish = soundPool.load(context,R.raw.bite,1);
        explosion = soundPool.load(context,R.raw.bomb,1);
    }
    public void eatFish(){
        soundPool.play(biteFish,1.0f,1.0f,1,0,1.0f);
    }
    public void finishGame(){
        soundPool.play(explosion,1.0f,1.0f,1,0,1.0f);
    }

}
