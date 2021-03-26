package org.techtown.dgu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnExpDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthScrollListener;
import sun.bob.mcalendarview.views.BaseCellView;
import sun.bob.mcalendarview.views.DefaultMarkView;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.DayData;



public class Home extends Fragment {
    private ExpCalendarView mcalendar;
    private DateData selectedDate;
    private TextView YearMonthTv; //calendar title


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, null);

        //mcalendar을 선언한다.
        mcalendar=(ExpCalendarView)view.findViewById(R.id.mcalendar1);
        //YearMontTv의 초기값을 선언한다.
        YearMonthTv = (TextView)view.findViewById(R.id.main_YYMM_Tv);
        YearMonthTv.setText(Calendar.getInstance().get(Calendar.YEAR) + "년 " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월");
        //calendar에서 달이 바뀔때 YearMonthTv값도 바뀐다.
        mcalendar.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(String.format("%d년 %d월", year, month));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
//                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        //==달력 내에서 날짜 선택시 백그라운드 변경 코드 ==
        // 현재시간을 받아온다. (년, 월, 일)
        int curyear = Transform_SimpleDateFormat_to_Int(new SimpleDateFormat("yyyy"));
        int curmonth = Transform_SimpleDateFormat_to_Int(new SimpleDateFormat("MM"));
        int curday = Transform_SimpleDateFormat_to_Int(new SimpleDateFormat("dd"));
        //오늘을 나타내주는 백그라운드 색을 변경해준다. (default값)
        mcalendar.markDate(
                new DateData(curyear, curmonth, curday)
                        .setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                                ContextCompat.getColor(getContext(),R.color.deepgreen))
                        ));

        mcalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                //지금까지 선택했던 백그라운드 색을 모두 삭제한다.
                mcalendar.getMarkedDates().removeAdd();

                //오늘을 나타내주는 백그라운드 색을 다시 변경해준다.
                mcalendar.markDate(
                        new DateData(curyear, curmonth, curday)
                                .setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                                        ContextCompat.getColor(getContext(),R.color.deepgreen))
                                ));
                //선택한 백그라운드 색을 변경해준다.
                mcalendar.markDate(date.setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                        ContextCompat.getColor(getContext(),R.color.green))));
                selectedDate = date;
            }
        });









        return view;
    }

    //SimpleDateFormat을 int로 바꿔주는 함수
    public int Transform_SimpleDateFormat_to_Int(SimpleDateFormat sdf){
        //현재날짜 선언하기
        Date date=new Date();

        //현재날짜를 string으로 변경
        String time=sdf.format(date);

        //string을 int로 변경한 후 return
        return Integer.parseInt(time);
    }

}




