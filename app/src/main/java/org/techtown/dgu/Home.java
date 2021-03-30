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

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.adapters.CalendarAdapter;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnExpDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.listeners.OnMonthScrollListener;
import sun.bob.mcalendarview.utils.CurrentCalendar;
import sun.bob.mcalendarview.views.BaseCellView;
import sun.bob.mcalendarview.views.DefaultMarkView;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.views.MonthView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.DayData;
import sun.bob.mcalendarview.vo.MonthData;


public class Home extends Fragment {
    private CalendarView mcalendar; //달력
    private  ViewGroup view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.home, container,false);
        mcalendar=view.findViewById(R.id.calendarView);

        mcalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });

        return view;
    }

}







