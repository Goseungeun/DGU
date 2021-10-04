package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Stopwatch{
    //각각의 자격증, 과목의 스톱워치
    //당일의 총 공부시간도 포함

    int running=0;            //0:멈춤, 1:움직임
    ImageView button;
    TextView Tv_time;

    long MillisecondTime, StartTime = 0L ;
    long TimeBuff, UpdateTime =0L;

    long TimeBuffTotal, UpdateTimeTotal =0L;
    Handler handler = new Handler();

    private Stopwatch_DB StopwatchDB;
    String Today;
    String ItemName;

    public Stopwatch(Context mContext){
        this.StopwatchDB = new Stopwatch_DB(mContext);
    }


    public void button_click_license(String _ItemName, String _TimeBuff, ImageView _button, TextView _time){
        this.ItemName=_ItemName;
        this.TimeBuff = StringToLong(_TimeBuff);
        this.button=_button;
        this.Tv_time=_time;
        this.Today=Today();


        //TODO 날이 지나면 초기화 되는거 구현해야함
        if(StopwatchDB.IsExist(Today)==null){
            //존재하는 행이 없다면
            StopwatchDB.InsertTotalStudyTime(Today,_TimeBuff);
            this.TimeBuffTotal =  StringToLong("00:00:00");
        }else{
            this.TimeBuffTotal =  StringToLong(StopwatchDB.IsExist(Today));
        }


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

        handler.removeCallbacks(runnable);

        //TODO : 시작하는 시간, 끝나는 시간 이용해서 타임테이블 구성하기
    }

    public final Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;    //개별 스톱워치
            UpdateTimeTotal = TimeBuffTotal + MillisecondTime;  //오늘 총 공부시간 스톱워치

            String UpdateTimeString = LongToString(UpdateTime);
            String UpdateTimeStringTotal = LongToString(UpdateTimeTotal);

            Tv_time.setText(UpdateTimeString);
            StopwatchDB.UpdateTotalStudyTime(Today,UpdateTimeStringTotal);

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

    public String Today(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }



}

