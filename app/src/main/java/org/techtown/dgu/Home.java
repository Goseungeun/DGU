package org.techtown.dgu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;




public class Home extends Fragment {
    private CalendarView mcalendar; //달력
    private  ViewGroup view;
    String selectday; // 타임테이블에서 받을 예정, 달력에서 선택된 날짜
    MainActivity activity;
    Context context;

    @Override
    public  void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context=context;

        //달력에서 선택한 날짜 수신에 필요한 객체들 초기화
        activity=(MainActivity)getActivity();
        selectday="";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.home, container,false);
        mcalendar=view.findViewById(R.id.calendarView);

        //날짜를 누르면 실행되는 함수
        mcalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month+=1; // 0~11월이라서 1을 더해줘야함.

                //형식 맞춰주기
                if(month<10){
                    selectday =""+ year + "0"+month;
                }else{
                    selectday =""+ year + month;
                }
                if(dayOfMonth<10){
                    selectday+="0"+dayOfMonth;
                }else{
                    selectday+=dayOfMonth;
                }


                Bundle selectday_bundle=new Bundle();
                selectday_bundle.putString("selectday",selectday);
                activity.setDayBundle(selectday_bundle);

                ((MainActivity)getActivity()).replaceFragment_addtobackstack(Timetable.newInstance());
            }
        });

        return view;
    }


}







