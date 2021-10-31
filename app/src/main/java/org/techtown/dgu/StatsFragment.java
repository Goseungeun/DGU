package org.techtown.dgu;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.techtown.dgu.member.SettingFragment;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

public class StatsFragment extends Fragment {
    public final static int ONEDAYTIME = 1440;     //하루의 시간 상수로 정의
    private LineChart lineChart;
    private DGUDB mDBHelper;

    TextView gold;
    TextView silver;
    TextView bronze;
    TextView goldtime;
    TextView silvertime;
    TextView bronzetime;
    TextView goldcategory;
    TextView silvercategory;
    TextView bronzecategory;
    LinearLayout goldlayout;
    LinearLayout silverlayout;
    LinearLayout bronzelayout;


    int BackgroundColor,MainColor;
    PieChart pieChart;


    public  int[] DGU_COLORS;
    String date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.statsfragment, container, false);
        mDBHelper = new DGUDB(getContext());

        DGU_COLORS =new int[] {

                getResources().getColor(R.color.aa),
                getResources().getColor(R.color.b),
                getResources().getColor(R.color.c),
                getResources().getColor(R.color.d),
                getResources().getColor(R.color.e),
                getResources().getColor(R.color.f),
                getResources().getColor(R.color.g)


        };

        TextView year=view.findViewById(R.id.sta_year);
        TextView month=view.findViewById(R.id.sta_month);

        lineChart = view.findViewById(R.id.moststudytime);
        gold = view.findViewById(R.id.gold);
        silver = view.findViewById(R.id.silver);
        bronze = view.findViewById(R.id.bronze);
        goldtime = view.findViewById(R.id.goldtime);
        silvertime = view.findViewById(R.id.silvertime);
        bronzetime = view.findViewById(R.id.bronzetime);
        goldcategory = view.findViewById(R.id.category_gold);
        silvercategory = view.findViewById(R.id.category_silver);
        bronzecategory = view.findViewById(R.id.category_bronze);
        goldlayout = view.findViewById(R.id.goldlayout);
        silverlayout = view.findViewById(R.id.silverlayout);
        bronzelayout = view.findViewById(R.id.bronzelayout);


        //현재날짜
        Calendar cur_cal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();      //<,>버튼 누를 때마다 바뀌는 값 계산하는 날짜
        //현재날짜(year,month) 화면에 표시
        year.setText(String.valueOf(cur_cal.get(Calendar.YEAR)));
        month.setText(String.valueOf(cur_cal.get(Calendar.MONTH)+1));
        Log.d("날짜","month"+cur_cal.get(Calendar.MONTH));
        //현재날짜를 기준으로 date 설정
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        date = format.format(cur_cal.getTime());
        Log.d("날짜","date"+date);

        moststudy(date);

        //공부 많이 한 시간
        BackgroundColor = getResources().getColor(R.color.deepgreen);
        MainColor = getResources().getColor(R.color.background);

        lineChart.setBackgroundColor(BackgroundColor);

        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);

        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        float[] timeTableData = getTimeTableData(date);
        setTimeTableGraph(timeTableData);



        //요일별 공부시간

        pieChart = view.findViewById(R.id.dayofweekstatics);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,8,5,8);

        pieChart.setDragDecelerationFrictionCoef(0.97f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(0f);

        Legend le = pieChart.getLegend();
        //le.setEnabled(false);
        le.setTextSize(15f);

        piepie(date);

//

        ImageButton leftbutton = (ImageButton)view.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(cal.MONTH,-1);
                year.setText(String.valueOf(cal.get(Calendar.YEAR)));
                month.setText(String.valueOf(cal.get(Calendar.MONTH)+1));
                date = format.format(cal.getTime());
                //통계 자료 다시 출력
                moststudy(date);
                float[] timeTableData = getTimeTableData(date);
                setTimeTableGraph(timeTableData);
                piepie(date);
            }
        });

        ImageButton rightbutton = (ImageButton)view.findViewById(R.id.rightbutton);
        rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(cal.MONTH,+1);
                year.setText(String.valueOf(cal.get(Calendar.YEAR)));
                month.setText(String.valueOf(cal.get(Calendar.MONTH)+1));
                date = format.format(cal.getTime());
                //통계 자료 다시 출력
                moststudy(date);
                float[] timeTableData = getTimeTableData(date);
                setTimeTableGraph(timeTableData);
                piepie(date);

            }
        });


        return view;

    }

    public void moststudy(String date){

        ArrayList<moststudyitem> msList = mDBHelper.getMostStudytimeArray(date);
        for(int i = 0 ; i < msList.size() ; i++)
        {
            String id = msList.get(i).getId();
            String type = msList.get(i).getType();
            if (type.equals("과목")) {
                msList.get(i).setName(mDBHelper.getSubjectName(id));
            }
            else {
                msList.get(i).setName(mDBHelper.getLicenseName(id));
            }
        }
        switch (msList.size()){
            case 0:
                goldlayout.setVisibility(View.GONE);
                silverlayout.setVisibility(View.GONE);
                bronzelayout.setVisibility(View.GONE);
                break;
            case 1:
                goldlayout.setVisibility(View.VISIBLE);
                gold.setText(msList.get(0).getName());
                goldtime.setText(msList.get(0).getStudytime());
                goldcategory.setText(msList.get(0).getType());
                silverlayout.setVisibility(View.GONE);
                bronzelayout.setVisibility(View.GONE);
                break;
            case 2:
                goldlayout.setVisibility(View.VISIBLE);
                gold.setText(msList.get(0).getName());
                goldtime.setText(msList.get(0).getStudytime());
                goldcategory.setText(msList.get(0).getType());
                silverlayout.setVisibility(View.VISIBLE);
                silver.setText(msList.get(1).getName());
                silvertime.setText(msList.get(1).getStudytime());
                silvercategory.setText(msList.get(1).getType());
                bronzelayout.setVisibility(View.GONE);
                break;
            case 3:
                goldlayout.setVisibility(View.VISIBLE);
                gold.setText(msList.get(0).getName());
                goldtime.setText(msList.get(0).getStudytime());
                goldcategory.setText(msList.get(0).getType());
                silverlayout.setVisibility(View.VISIBLE);
                silver.setText(msList.get(1).getName());
                silvertime.setText(msList.get(1).getStudytime());
                silvercategory.setText(msList.get(1).getType());
                bronzelayout.setVisibility(View.VISIBLE);
                bronze.setText(msList.get(2).getName());
                bronzetime.setText(msList.get(2).getStudytime());
                bronzecategory.setText(msList.get(2).getType());
                break;
        }
    }

    public float[] getTimeTableData(String date){
        int[] sum = new int[ONEDAYTIME];      //한달치 타임테이블 더한 값
        int[] hour_sum = new int[24];
        float[] total_sum = new float[24];
        ArrayList<String[]> tableContList = mDBHelper.getMonthlyTimeTable(date);           //스트링 배열의 리스트 리턴
        int size = tableContList.size();
        if (size == 0)
        {
            //값이 하나도 없을 때
            for ( int i = 0 ; i < 24 ; i++)
            {
                total_sum[i] = 0;
            }
        }

        else {
            for (int i = 0; i < size; i++) {
                String[] t_content = tableContList.get(i);     //List의 i번째 스트링 배열 받아옴
                int[] t_content_int = new int[ONEDAYTIME];
                for (int j = 0; j < ONEDAYTIME; j++) {
                    t_content_int[j] = Integer.parseInt(t_content[j]);      //스트링 배열을 int 배열로 바꿔줌
                    sum[j] += t_content_int[j];     //sum에 계속 합계를 넣어줌
                }
            }
            for (int z = 0; z < 24; z++) {
                for (int x = (60 * z); x < (60 * (z + 1)); x++) {
                    hour_sum[z] += sum[x];      //한시간 단위로 값 넣어줌
                }
                total_sum[z] = ((float) hour_sum[z] / (size * 60)) * 100;       //값의 백분율 구하기
            }
        }
        return total_sum;
    }

    public void setTimeTableGraph(float[] data){
        List<Entry> contents = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        for(int i = 0; i < 24; i++){
            contents.add(new Entry(i, data[i]));
        }

        //data set type 정의
        LineDataSet timeTableDataSet = new LineDataSet(contents, "공부시간");
        timeTableDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);     // 곡선형으로 바꾸기
        timeTableDataSet.setCubicIntensity(0.2f);
        timeTableDataSet.setLineWidth(1.75f);
        timeTableDataSet.setColor(MainColor);
        timeTableDataSet.setDrawCircleHole(false);
        timeTableDataSet.setDrawCircles(false);
        timeTableDataSet.setDrawHorizontalHighlightIndicator(false);
        timeTableDataSet.setDrawHighlightIndicators(false);
        timeTableDataSet.setDrawValues(false);
        timeTableDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.timetablegraph_backgroun);
        timeTableDataSet.setFillDrawable(drawable);

        LineData timeTableData = new LineData(timeTableDataSet);

        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(false);
        lineChart.setData(timeTableData);
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(MainColor);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(MainColor);
        xAxis.setAxisLineWidth(1.75f);
        xAxis.setAxisMaximum(23.0f);
        xAxis.setLabelCount(13,true);


        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawLabels(false);
        yAxis.setGridColor(MainColor);
        yAxis.setAxisMinimum(0.0f);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(true);
        yAxis.setAxisLineColor(MainColor);
        yAxis.setAxisLineWidth(1.75f);
        yAxis.setGranularityEnabled(true);
        yAxis.setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateXY(1000,1000);

        lineChart.invalidate();

    }


    public void piepie(String date){
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();


        String dow[] = mDBHelper.DayOfWeek(date);;

        for (int i = 0; i < dow.length; i++) {
            Log.v("StringIds", "i:" + i + ", dow[i]:" + dow[i]);

            String day[]=dow[i].split(",");

            String newday[]=day[1].split(":");


            Integer dd=parseInt(newday[0])*60*60+parseInt(newday[1])*60+parseInt(newday[2]);
            Log.d("새로운 날","new: "+dd);

            if(parseInt(day[0])==0)
                yValues.add(new PieEntry(dd,"일"));
            else if(parseInt(day[0])==1)
                yValues.add(new PieEntry(dd,"월"));
            else if(parseInt(day[0])==2)
                yValues.add(new PieEntry(dd,"화"));
            else if(parseInt(day[0])==3)
                yValues.add(new PieEntry(dd,"수"));
            else if(parseInt(day[0])==4)
                yValues.add(new PieEntry(dd,"목"));
            else if(parseInt(day[0])==5)
                yValues.add(new PieEntry(dd,"금"));
            else if(parseInt(day[0])==6)
                yValues.add(new PieEntry(dd,"토"));

        }

        pieChart.animateY(1000); //애니메이션


        PieDataSet dataSet = new PieDataSet(yValues, "요일");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(DGU_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
       // dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(0.8f);
        dataSet.setValueLinePart2Length(.2f);
        dataSet.setValueLineColor(getResources().getColor(R.color.deepgreen));

        PieData data = new PieData((dataSet));
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources().getColor(R.color.deepgreen));

        pieChart.setData(data);
    }


}
