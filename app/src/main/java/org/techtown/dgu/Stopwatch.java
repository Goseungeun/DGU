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

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Stopwatch {

    int running=0;            //0:멈춤, 1:움직임

    ImageView button;
    TextView Tv_time;


    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler = new Handler();


    //String으로 값을 받고, 계산할 때는 long으로
    public void button_click(String _TimeBuff,ImageView _button, TextView _time){

        this.TimeBuff = StringToLong(_TimeBuff);
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

            String timeText = LongToString(UpdateTime);

            Tv_time.setText(timeText);

            handler.postDelayed(this, 0);
        }

    };

    //String을 long으로 전환
    public long StringToLong(String time){

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        long result = 0;
        try {
            date = formatter.parse(time);
            result = date.getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return result;

    }

    //long을 String으로 전환
    public String LongToString(long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String result;
        result = formatter.format(date);

        return result;
    }



}

