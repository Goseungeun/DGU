package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.dgu.member.SettingFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Home extends Fragment {
    private CalendarView mcalendar; //달력
    private  ViewGroup view;
    String selectday; // 타임테이블에서 받을 예정, 달력에서 선택된 날짜
    MainActivity activity;
    Context context;
    DGUDB DB;

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
        DB=new DGUDB(getContext());

        //TODO 원래는 넘기는대로 달이 바뀌어야하는디...
        TextView homeMonthTotalStudytimeTitle = view.findViewById(R.id.homeMonthTotalStudytimeTitle);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        homeMonthTotalStudytimeTitle.setText(formatter.format(Calendar.getInstance().getTime())+"월 총 공부시간");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String date = format.format(Calendar.getInstance().getTime());

        Log.v("cccal",""+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+date);
        TextView homeMonthTotalStudytime= view.findViewById(R.id.homeMonthTotalStudytime);
        homeMonthTotalStudytime.setText(DB.MonthTotalStudyTime
                (date+"-01",
                        date+"-"+cal.getActualMaximum(Calendar.DAY_OF_MONTH)));


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

                ((MainActivity)getActivity()).replaceFragment(Timetable.newInstance());
            }
        });


        //setting button을 누르면 실행되는 함수
        ImageButton settingbutton = (ImageButton)view.findViewById(R.id.SettingButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        settingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(new SettingFragment());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        return view;
    }


}







