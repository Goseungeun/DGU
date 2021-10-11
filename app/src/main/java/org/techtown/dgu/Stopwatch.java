package org.techtown.dgu;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.dgu.studylicense.LicenseItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stopwatch {
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
    LicenseItem licenseItem;
    String ItemName;
    String DefalutTime = "00:00:00";

    public Stopwatch(Context mContext){
        this.StopwatchDB = new Stopwatch_DB(mContext);
        this.Today=Today();
    }

    public void InsertTotalStudyTime(){
        Today=Today();
        StopwatchDB.InsertTotalStudyTime(Today,DefalutTime);
        TimeBuffTotal=StringToLong(DefalutTime);
        TimeBuff=StringToLong(DefalutTime);
    }


    public LicenseItem button_click_license(LicenseItem _licenseItem, ImageView _button, TextView _time){
        this.licenseItem = _licenseItem;
        //this.ItemName=licenseItem.getName();
        //this.TimeBuff = StringToLong(licenseItem.getStudytime());
        this.button=_button;
        this.Tv_time=_time;

        //TODO 날이 지나면 초기화 되는거 구현해야함
        //TODO 바뀐 화면과 연결하기


        //행이 존재할수도 없을수도 있어서
        if(ChangeDate()){
            //존재하는 행이 없다면
            InsertTotalStudyTime();
        }else{
            this.TimeBuffTotal =  StringToLong(StopwatchDB.IsExist(Today));
        }

        //0:멈춤, 1:움직임
        if(running==0){
            start();
        } else{
            stop();
        }
        return licenseItem;
    }

    public void start(){
        button.setImageResource(R.drawable.pause);

        StartTime = SystemClock.uptimeMillis();
        running=1;
        handler.postDelayed(runnable, 0);
    }

    public void stop(){
        button.setImageResource(R.drawable.play);

        handler.removeCallbacks(runnable);
        running=0;
        //TODO : 시작하는 시간, 끝나는 시간 이용해서 타임테이블 구성하기
    }

    public final Runnable runnable = new Runnable() {

        public void run() {
            //돌아가는 와중에 다른날로 넘어갈 시 공부시간을 초기화해준다.
            if(ChangeDate()){
                InsertTotalStudyTime();
            }

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;    //개별 스톱워치
            UpdateTimeTotal = TimeBuffTotal + MillisecondTime;  //오늘 총 공부시간 스톱워치

            String UpdateTimeString = LongToString(UpdateTime);
            String UpdateTimeStringTotal = LongToString(UpdateTimeTotal);

            //licenseItem.setStudytime(UpdateTimeString);
            Tv_time.setText(UpdateTimeString);

            //Log.v("innerStopwatch",licenseItem.getStudytime());
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
        String result = sdf.format(date);
        return result;
    }

    //바뀐거면 true, 안바뀐거면 false
    public boolean ChangeDate() {
        if(StopwatchDB.IsExist(Today())==null){
            Log.v("ChangeDate",Today());
            return true;}
        else{return false;}
    }

}

