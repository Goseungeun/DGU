package org.techtown.dgu;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StopwatchToday extends Fragment {
    private Stopwatch_DB StopwatchDB;
    TextView Tv_time_total;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.total_stopwatch, container, false); // Fragment로 불러올 xml파일을 view로 가져옵니다.

        StopwatchDB = new Stopwatch_DB(getContext());
        //당일 총 공부시간 textview 연결
        Tv_time_total= view.findViewById(R.id.total_stopwatch);

        handler.postDelayed(runnable, 0);

        return view;
    }
    public final Runnable runnable = new Runnable() {

        public void run() {
            Tv_time_total.setText(StopwatchDB.getStudyTime(Today()));
            handler.postDelayed(this, 0);
        }

    };
    public String Today(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }

}
