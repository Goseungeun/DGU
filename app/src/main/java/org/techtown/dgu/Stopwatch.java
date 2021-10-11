/*
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

    //TODO 여기다가 프래그먼트안에 있는 애들 id로 연결
    TextView Tv_time;

    long MillisecondTime, StartTime = 0L ;
    long TimeBuff, UpdateTime =0L;
    long TimeBuffTotal, UpdateTimeTotal =0L;
    long UpdateTimeFocus=0L;

    Handler handler = new Handler();

    private DGUDB StopwatchDB;



    String ItemName;
    String DefalutTime = "00:00:00";


    String subid;
    String licenseid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Stopwatch(Context mContext){
        this.StopwatchDB = new DGUDB(mContext);

    }

    public void InsertTotalStudyTime(String _subid, String _licenseid){
        StopwatchDB.InsertStudyTime(_subid,_licenseid);
        TimeBuffTotal=StringToLong(DefalutTime);
        TimeBuff=StringToLong(DefalutTime);
    }


    public void button_click(String _subid, String _licenseid){
        this.subid =_subid;
        this.licenseid=_licenseid;
        //this.ItemName=licenseItem.getName();
        //this.TimeBuff = StringToLong(licenseItem.getStudytime());

        //this.Tv_time=_time;

        //TODO 날이 지나면 초기화 되는거 구현해야함
        //TODO 바뀐 화면과 연결하기


        //행이 존재할수도 없을수도 있어서
        if(ChangeDate()){
            //존재하는 행이 없다면
            setId(StopwatchDB.InsertStudyTime(subid,licenseid));
        }else{
            setId(StopwatchDB.SearchStudytimeID(subid,licenseid));
            //this.TimeBuffTotal =  StringToLong(StopwatchDB.IsExist(getId()));
        }

        start();
    }


    public void start(){

        StartTime = SystemClock.uptimeMillis();

        handler.postDelayed(runnable, 0);
    }

    public void stop(){
        handler.removeCallbacks(runnable);
        //TODO : 시작하는 시간, 끝나는 시간 이용해서 타임테이블 구성하기
    }

    public final Runnable runnable = new Runnable() {

        public void run() {
            //돌아가는 와중에 다른날로 넘어갈 시 공부시간을 초기화해준다.
            if(ChangeDate()){
                setId(StopwatchDB.InsertStudyTime(subid,licenseid));
            }

            //어디다가 업데이트 할 지 알아야하기때문에 해당 튜플의 id를 가져온다.


            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;            //개별 스톱워치
            UpdateTimeTotal = TimeBuffTotal + MillisecondTime;  //오늘 총 공부시간 스톱워치
            UpdateTimeFocus = MillisecondTime;                  //집중한 시간

            String UpdateTimeString = LongToString(UpdateTime);
            String UpdateTimeTotalString = LongToString(UpdateTimeTotal);
            String UpdateTimeFocusString = LongToString(UpdateTimeFocus);


            Tv_time.setText(UpdateTimeString);

            StopwatchDB.UpdateStudyTime(getId(),UpdateTimeTotalString);

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

    //바뀐거면 true, 안바뀐거면 false
    public boolean ChangeDate() {
        if(StopwatchDB.IsExist(id)==null){return true;}
        else{return false;}
    }

}

*/
