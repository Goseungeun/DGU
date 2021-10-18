package org.techtown.dgu;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.L;

import org.techtown.dgu.studylicense.LicenseFragment;
import org.techtown.dgu.subject.SubjectFragment;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class StopwatchFragment extends Fragment {
    //TODO 시간을 재는 도중에 뒤로가기 막아야함.
    private ViewGroup view;
    private DGUDB DB;

    String subid;
    String licenseid;
    int studytimeid=0;

    TextView FocuseTime, Today, TotalTime,EachCategory, EachName,EachTime;
    ImageView pause;

    long MillisecondTime, StartTime= 0L ;
    long TimeBuff, UpdateTime =0L;
    long TimeBuffTotal, UpdateTimeTotal =0L;
    long TimeBuffFocus, UpdateTimeFocus=0L;

    long TimetableStartTime, TimetableEndTime=0L;

    String StartTimeString, EndTimeString = null;

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
        this.TimeBuffTotal = StringToLong(DB.DateTotalStudyTime(DB.give_Today()));
        this.TimeBuffFocus = StringToLong("00:00:00");

        Log.v("Timebuff",""+TimeBuff);

        //Fragment요소들과 연결 (시간 제외)
        Today.setText(DB.give_Today());
        if(subid!=null){
            //과목
            EachCategory.setText("과목");
            EachName.setText(DB.getSubjectName(subid));
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
        TimetableStartTime = System.currentTimeMillis();
        StartTimeString = LongToString(TimetableStartTime);
        handler.postDelayed(runnable, 0);
    }

    public void stop(){
        handler.removeCallbacks(runnable);
        TimetableEndTime = System.currentTimeMillis();
        EndTimeString=LongToString(TimetableEndTime);

        String today = DB.give_Today();
        //timetable 내용 업데이트
        FillTimeTable(today);


        //화면전환
        Intent intent = new Intent(getContext(), MainActivity.class);
        if(subid!=null) {
            //과목으로 돌아가야함.
            intent.putExtra("category","subject");
        }else{
            //자격증으로 돌아가야함.
            intent.putExtra("category","license");
        }
        //화면전환이 됐을 때, 핸드폰 상의 뒤로가기 버튼을 누르면 이전 스톱워치화면이 보이는 현상을 막기위한 코드
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public final Runnable runnable = new Runnable() {

        public void run() {
            //돌아가는 와중에 다른날로 넘어갈 시 공부시간을 초기화해준다.
            if(ChangeDate()){
                setStudytimeid(DB.InsertStudyTime(subid,licenseid));
                TimeBuff =  StringToLong(DB.getStudytime(getStudytimeid()));
                TimeBuffTotal = StringToLong(DB.DateTotalStudyTime(DB.give_Today()));
                TimetableEndTime = System.currentTimeMillis();
                EndTimeString=LongToString(TimetableEndTime);

                String yesterday = DB.give_Yesterday();
                FillTimeTable(yesterday);

                StartTimeString = "00:00:00";
            }

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

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
        if(DB.SearchStudytimeID(subid, licenseid)==0){return true;}
        else{return false;}
    }

    public int getStudytimeid() {
        return studytimeid;
    }

    public void setStudytimeid(int studytimeid) {
        this.studytimeid = studytimeid;
    }

    private void FillTimeTable(String date) {

        int timetablecontent[] =new int[24*60];
        if(!DB.isExistTodayTimeTable(date)){
            //timetable에 기존 값이 없는 경우
            for(int i=0;i<timetablecontent.length;i++){
                timetablecontent[i]=0;
            }
            DB.InsertTimeTable(date, Arrays.toString(timetablecontent));
        }else{
            String [] timetablecontentStrings = DB.getTimeTableContent(date).replaceAll("\\[", "")
                    .replaceAll("]", "").replaceAll(" ","").split(",");

            for(int i=0;i<timetablecontentStrings.length;i++){
                timetablecontent[i]=Integer.parseInt(timetablecontentStrings[i]);
            }
        }

        int StartHour = Integer.parseInt(StartTimeString.substring(0,2));
        int Startmin = Integer.parseInt(StartTimeString.substring(3,5));
        int EndHour = Integer.parseInt(EndTimeString.substring(0,2));
        int Endmin = Integer.parseInt(EndTimeString.substring(3,5));
        Log.v("timetableFragment1","StartTimeString : "+StartTimeString+"EndTimeString : "+EndTimeString);
        Log.v("timetableFragmentStrart","Start"+StartHour+":"+Startmin+"End"+EndHour+":"+Endmin);

        int i=0;

        for(int hour=StartHour;hour<=EndHour;hour++){
            for(int min=0;min<60;min++){
                //시작지점까지는 그냥 이동해야함.
                if(i==0){ min=Startmin;}

                i=60*hour+min;
                timetablecontent[i]=1;

                Log.v("timetableFragment","hour : "+hour+" min : "+min+" timetablecontent[i] : "+timetablecontent[i]);

                if(hour==EndHour && min==Endmin){break;}
            }
        }


        DB.UpdateTimeTable(date, Arrays.toString(timetablecontent));
    }

}
