package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.techtown.dgu.studylicense.LicenseFragment;
import org.techtown.dgu.subject.SubjectFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StopwatchFragment extends Fragment {
    private ViewGroup view;
    private DGUDB DB;

    String subid;
    String licenseid;
    String studytimeid=null;  //TODO : null로 검색해도 잘나오나..?

    TextView FocuseTime, Today, TotalTime,EachCategory, EachName,EachTime;
    ImageView pause;

    long MillisecondTime, StartTime = 0L ;
    long TimeBuff, UpdateTime =0L;
    long TimeBuffTotal, UpdateTimeTotal =0L;
    long TimeBuffFocus, UpdateTimeFocus=0L;

    Handler handler = new Handler();

    public StopwatchFragment(String _subid, String _licenseid){
        this.subid=_subid;
        this.licenseid=_licenseid;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.stopwatch, container, false);

        DB = new DGUDB(getContext());

        //fragment에 있는 요소들과 연결
        FocuseTime = view.findViewById(R.id.stopwatchFocusTime);
        Today = view.findViewById(R.id.stopwatchToday);
        TotalTime = view.findViewById(R.id.stopwatchTotalTime);
        EachCategory = view.findViewById(R.id.stopwatchEachCategory);
        EachName = view.findViewById(R.id.stopwatchEachName);
        EachTime = view.findViewById(R.id.stopwatchEachTime);
        pause = view.findViewById(R.id.stopwatchpause);



        //테이블에 행이 존재할수도 없을수도 있어서
        if(ChangeDate()){
            //존재하는 행이 없다면
            Log.v("ChangeDate판단",""+ChangeDate()+"  "+getStudytimeid());
            setStudytimeid(DB.InsertStudyTime(subid,licenseid));

        }else{
            setStudytimeid(DB.SearchStudytimeID(subid,licenseid));
        }

        this.TimeBuff =  StringToLong(DB.getStudytime(getStudytimeid()));
        this.TimeBuffTotal = StringToLong("00:00:00");
        this.TimeBuffFocus = StringToLong("00:00:00");

        Log.v("Timebuff",""+TimeBuff);

        //Fragment요소들과 연결 (시간 제외)
        Today.setText(DB.give_Today());
        if(subid!=null){
            //과목
            //TODO 과목이랑 db연결해야함.
        }else{
            //자격증
            EachCategory.setText("자격증");
            EachName.setText(DB.getLicenseName(licenseid));
        }

        //TODO 날이 지나면 초기화 되는거 구현해야함
        start();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                Log.v("pauseClick","subid : "+subid+", licenseid : "+licenseid);
                if(subid!=null){
                    //과목으로 돌아가기
                    ((MainActivity)getActivity()).replaceFragment(new SubjectFragment());

                }else{
                    //자격증으로 돌아가기
                    ((MainActivity)getActivity()).replaceFragment(new LicenseFragment());
                }
            }
        });

        return view;
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
                setStudytimeid(DB.InsertStudyTime(subid,licenseid));
                TimeBuff =  StringToLong(DB.getStudytime(getStudytimeid()));
            }

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            //TimeBuffTotal=StringToLong(DB.DateTotalStudyTime(DB.give_Today()));

            UpdateTime = TimeBuff + MillisecondTime;            //개별 스톱워치
            UpdateTimeTotal = TimeBuffTotal + MillisecondTime;  //오늘 총 공부시간 스톱워치
            UpdateTimeFocus = TimeBuffFocus+MillisecondTime;    //집중한 시간

            String UpdateTimeString = LongToString(UpdateTime);
            String UpdateTimeTotalString = LongToString(UpdateTimeTotal);
            String UpdateTimeFocusString = LongToString(UpdateTimeFocus);

            //Fragment요소들과 연결 (시간)
            EachTime.setText(UpdateTimeString);
            TotalTime.setText(UpdateTimeTotalString);
            FocuseTime.setText(UpdateTimeFocusString);

            DB.UpdateStudyTime(getStudytimeid(),UpdateTimeString);

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
        //Log.v("ChangeDate inner",getStudytimeid());
        if(DB.SearchStudytimeID(subid, licenseid)==null){return true;}
        else{return false;}
    }

    public String getStudytimeid() {
        return studytimeid;
    }

    public void setStudytimeid(String studytimeid) {
        this.studytimeid = studytimeid;
    }



}
