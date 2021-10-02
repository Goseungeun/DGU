package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timetable extends Fragment {

    public static Timetable newInstance() {
        return new Timetable();
    }
    ViewGroup view;
    MainActivity activity;
    Context context;
    String receiveday;

    int tt_year;        //타임테이블 년도
    int tt_month;       //타임테이블 월
    int tt_day;         //타임테이블 일
    TextView Tv_title;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        //캘린더에서 보낸 날짜 수신에 필요한 객체 초기화
        activity = (MainActivity) getActivity();
        receiveday="";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.timetable, container,false);


        // 캘린더에서 날짜 가져오기
        if(activity.dayBundle!=null){
            Bundle daybundle = activity.dayBundle;
            receiveday=daybundle.getString("selectday");

            //날짜를 String->int로 변경
            tt_year=Integer.parseInt(receiveday.substring(0,4));
            tt_month=Integer.parseInt(receiveday.substring(4,6));
            tt_day=Integer.parseInt(receiveday.substring(6,8));

            // 타임테이블 날짜 표시하기
            Tv_title=view.findViewById(R.id.title);
            Tv_title.setText(tt_year+"년\n"+tt_month+"월 "+tt_day+"일");
            activity.dayBundle=null;

            //TODO : 타임테이블의 해당날짜 총 공부시간 표시해야함!


        }

        //gridview 초기화
        GridView timetable=view.findViewById(R.id.timetable_gridview);
        GridView timetable_hour_name=view.findViewById(R.id.timetable_hourname);
        Timetable_hour_Adapter hour_adapter = new Timetable_hour_Adapter();
        Timetable_Adapter adapter= new Timetable_Adapter();


        //gridview 내용 채워넣기 시작



        //타임테이블 옆 시간 채워넣기
        //타임테이블에 들어있는 뷰마다 시간 지정해주기
        for (int hour=0;hour<24;hour++){
            hour_adapter.addItem(new timetable_hour_Item(hour));
            for(int min=0;min<60;min++){
                adapter.addItem(new Timetable_Item(hour,min));
            }
        }

        //디비에 저장된 공부한시간을 체크해서 각 Timetable_Item의 stdudy(boolean값, default: false)을 변경해줘야한다.


        //타임테이블 옆 시간 보이기
        timetable_hour_name.setAdapter(hour_adapter);
        //타임테이블 보이기
        timetable.setAdapter(adapter);

        ///Start remove scrolling function of timetable_hour_name & timetable
        timetable.setEnabled(false);
        timetable_hour_name.setEnabled(false);
            //scrollbar remove
        timetable.setVerticalScrollBarEnabled(false);
        timetable_hour_name.setVerticalScrollBarEnabled(false);
        ///End remove scrolling function of timetable_hour_name & timetable


        //BackButton을 누르면 실행되는 함수
        ImageButton backbutton = (ImageButton)view.findViewById(R.id.ttBackButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(new Home());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        return view;

    }
}
