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
    private DGUDB DB;
    TextView Tv_time_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.total_stopwatch, container, false); // Fragment로 불러올 xml파일을 view로 가져옵니다.

        DB = new DGUDB(getContext());
        //당일 총 공부시간 textview 연결
        Tv_time_total= view.findViewById(R.id.total_stopwatch);

        if(DB.DateTotalStudyTime(DB.give_Today())!=null){
            Tv_time_total.setText(DB.DateTotalStudyTime(DB.give_Today()));
        }else{Tv_time_total.setText("00:00:00");}


        return view;
    }

}
