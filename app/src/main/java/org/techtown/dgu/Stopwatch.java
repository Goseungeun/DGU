package org.techtown.dgu;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stopwatch {

    int running=0;            //0:멈춤, 1:움직임

    ImageView button;
    TextView Tv_time;


    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler = new Handler();

    int Seconds, Minutes, Hours ;

    public void button_click(long _TimeBuff,ImageView _button, TextView _time){
        this.TimeBuff = _TimeBuff;
        this.button=_button;
        this.Tv_time=_time;

        //0:멈춤, 1:움직임
        if(running==0){
            start();
            running=1;
        } else{
            stop();
            running=0;
        }
    }

    public void start(){
        button.setImageResource(R.drawable.pause);

        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    public void stop(){
        button.setImageResource(R.drawable.play);

        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Hours = Seconds /360;
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;


            String timeText=""+ String.format("%02d", Hours)+":"
                                + String.format("%02d", Minutes)+":"
                                + String.format("%02d", Seconds);


            Tv_time.setText(timeText);
            handler.postDelayed(this, 0);
        }

    };



}

