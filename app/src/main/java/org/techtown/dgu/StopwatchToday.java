package org.techtown.dgu;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StopwatchToday extends Fragment {
    private DGUDB DB;
    TextView Tv_time_total;
    TextView Tv_Ment;
    TextView Tv_Time;
    TextView Tv_Change;
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.total_stopwatch, container, false); // Fragment로 불러올 xml파일을 view로 가져옵니다.

        DB = new DGUDB(getContext());
        //당일 총 공부시간 textview 연결
        Tv_time_total= view.findViewById(R.id.total_stopwatch);
        Tv_Ment=view.findViewById(R.id.stopwatchComparisonMent);
        Tv_Time=view.findViewById(R.id.stopwatchComparisonTime);
        Tv_Change=view.findViewById(R.id.stopwatchComparisonChange);

        String TodayStudytime=DB.DateTotalStudyTime(DB.give_Today());
        String YesterdayStudytime=DB.DateTotalStudyTime(DB.give_Yesterday());

        if(TodayStudytime!=null){
            Tv_time_total.setText(TodayStudytime);
            if(YesterdayStudytime!=null){
                //오늘도 공부하고 어제도 공부한 경우
                Log.v("stopwatchtoday",TodayStudytime + "  "+YesterdayStudytime);
                long TodayStudytimeLong = StringToLong(TodayStudytime);
                long YesterdayStudytimeLong = StringToLong(YesterdayStudytime);

                Log.v("kk",""+(TodayStudytimeLong-YesterdayStudytimeLong)+
                        ""+LongToString(TodayStudytimeLong-YesterdayStudytimeLong));
                if(TodayStudytimeLong>YesterdayStudytimeLong){
                    //어제보다 오늘 더 공부한 경우
                    Tv_Time.setText(LongToString((TodayStudytimeLong-YesterdayStudytimeLong)));

                }else if(TodayStudytimeLong==YesterdayStudytimeLong){
                    //어제와 오늘 공부한 시간이 같을 경우
                    Tv_Ment.setText("공부한 시간이 어제와 동일해요!");
                    Tv_Time.setText("");
                    Tv_Change.setText("");

                }else{
                    //어제보다 오늘 덜 공부한 경우
                    Log.v("stopwatchtoday",LongToString(TodayStudytimeLong));
                    Tv_Time.setText(LongToString((YesterdayStudytimeLong-TodayStudytimeLong)));
                    Tv_Change.setText("만큼 덜 공부했어요 ✍");
                }

            }else{
                //오늘은 공부했는데 어제는 안했을 경우
                Tv_Time.setText(TodayStudytime);
            }
        }else{
            //아직 오늘 공부를 하지 않은 경우
            Tv_time_total.setText("00:00:00");

            Tv_Ment.setText("아직 공부를 시작하지 않았어요!");
            Tv_Time.setText("");
            Tv_Change.setText("");
        }

        return view;
    }

    //String을 long으로 전환
    public long StringToLong(String time){

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

        //자꾸 9시간이 더해져서 나와서
        time = time - 32400000;
        Date date = new Date(time);
        String result;
        result = formatter.format(date);

        return result;
    }

}

