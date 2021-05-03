package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


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

                ((MainActivity)getActivity()).replaceFragment(Timetable.newInstance());
            }
        });


        //setting button을 누르면 실행되는 함수
        ImageButton settingbutton = (ImageButton)view.findViewById(R.id.SettingButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        settingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(new Setting_Fragment());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        //AttendanceCheck TESTbutton
        ImageButton testbutton = (ImageButton)view.findViewById(R.id.TESTButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(new AttendanceCheck());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });
        //
        return view;
    }


}







