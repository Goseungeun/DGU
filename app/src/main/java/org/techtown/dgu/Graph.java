package org.techtown.dgu;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class  MyValueFormatter extends ValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;

    public MyValueFormatter(){
        mFormat = new DecimalFormat("##,###,##0.00");
    }

}

public class Graph extends Fragment {
    LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.graph,container,false);

        initUI(rootView);

        return rootView;
    }

    private void initUI(ViewGroup rootView){
        //line graph 기본 설정
        chart = rootView.findViewById(R.id.graph_chart);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setViewPortOffsets(10f,10f,10f,10f);
        chart.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_border));
        chart.setPadding(10,10,10,10);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        //x축 설정
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(9f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        //y축 설정
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(1.5f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setTextColor(Color.rgb(255,192,56));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        setData();
    }

private void setData(){
        //데이터 입력하기
    ArrayList<Entry> values = new ArrayList<>();
    values.add(new Entry(1f, 3.94f));
    values.add(new Entry(2f, 4.04f));
    values.add(new Entry(3f, 4.14f));
    values.add(new Entry(4f, 4.26f));
    values.add(new Entry(5f, 4.5f));

    // create a dataset and give it a type
    LineDataSet set1 = new LineDataSet(values, "DataSet 1");
    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
    set1.setColor(Color.rgb(74,106,97));
    set1.setValueTextColor(Color.BLACK);
    set1.setLineWidth(1.5f);
    set1.setDrawCircles(true);
    set1.setDrawValues(true);
    set1.setFillAlpha(65);
    set1.setFillColor(Color.rgb(74,106,97));
    set1.setCircleColor(Color.rgb(74,106,97));
    set1.setHighLightColor(Color.rgb(74,106,97));
    set1.setDrawCircleHole(false);

    // create a data object with the data sets
    LineData data = new LineData(set1);
    data.setValueTextColor(Color.BLACK);
    data.setValueTextSize(15f);
    data.setValueFormatter(new MyValueFormatter());

    // set data
    chart.setData(data);
    chart.invalidate();
}
}