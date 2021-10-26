package org.techtown.dgu;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.utils.Easing;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.techtown.dgu.member.SettingFragment;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class StatsFragment extends Fragment {
    public final static int ONEDAYTIME = 1440;     //하루의 시간 상수로 정의
    private LineChart lineChart;
    private DGUDB mDBHelper;

    TextView gold;
    TextView silver;
    TextView bronze;
    TextView gold2;
    TextView silver2;
    TextView bronze2;
    ProgressBar goldprogress;
    ProgressBar silverprogress;
    ProgressBar bronzeprogress;

    int BackgroundColor,MainColor;
    PieChart pieChart;

    public static final int[] DGU_COLORS = {
        Color.rgb(82,151,131),
            Color.rgb(86,179,62),
            Color.rgb(207,191,8),
            Color.rgb(57,179,154),
            Color.rgb(200,125,11),
            Color.rgb(239,189,41),
            Color.rgb(9,156,92)
    };

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.statsfragment, container, false);
        mDBHelper = new DGUDB(getContext());


        TextView year=view.findViewById(R.id.sta_year);
        TextView month=view.findViewById(R.id.sta_month);

        //현재날짜
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat forma = new SimpleDateFormat("yyyy");
        year.setText(forma.format(cal.getTime()));

        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        month.setText(formatter.format(cal.getTime()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat tt = new SimpleDateFormat("yyyy-MM-DD  HH-MM-SS");

        String date = format.format(cal.getTime());
        Log.d("날짜","date"+date);


        //가장 많이한 공부
        lineChart = view.findViewById(R.id.moststudytime);
        gold = view.findViewById(R.id.gold);
        silver = view.findViewById(R.id.silver);
        bronze = view.findViewById(R.id.bronze);
        gold2 = view.findViewById(R.id.gold2);
        silver2 = view.findViewById(R.id.silver2);
        bronze2 = view.findViewById(R.id.bronze2);
        goldprogress=view.findViewById(R.id.progressBargold);
        silverprogress=view.findViewById(R.id.progressBarsilver);
        bronzeprogress=view.findViewById(R.id.progressBarbronze);

        moststudy(date);

        ImageButton leftbutton = (ImageButton)view.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(cal.MONTH,-1);
                year.setText(forma.format(cal.getTime()));
                month.setText(formatter.format(cal.getTime()));
                Log.d("date","month: "+format.format(cal.getTime()));
                moststudy(format.format(cal.getTime()));

            }
        });

        ImageButton rightbutton = (ImageButton)view.findViewById(R.id.rightbutton);
        rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(cal.MONTH,+1);
                year.setText(forma.format(cal.getTime()));
                month.setText(formatter.format(cal.getTime()));
                moststudy(format.format(cal.getTime()));
            }
        });


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
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();


        String dow[] = mDBHelper.DayOfWeek(date);;

        for (int i = 0; i < dow.length; i++) {
            Log.v("StringIds", "i:" + i + ", dow[i]:" + dow[i]);

            String day[]=dow[i].split(",");

            String newday[]=day[1].split(":");


            Integer dd=parseInt(newday[0])*60*60+parseInt(newday[1])*60+parseInt(newday[2]);
            Log.d("새로운 날","new: "+dd);

            if(parseInt(day[0])==0)
                yValues.add(new PieEntry(dd,"일요일"));
            else if(parseInt(day[0])==1)
                yValues.add(new PieEntry(dd,"월요일"));
            else if(parseInt(day[0])==2)
                yValues.add(new PieEntry(dd,"화요일"));
            else if(parseInt(day[0])==3)
                yValues.add(new PieEntry(dd,"수요일"));
            else if(parseInt(day[0])==4)
                yValues.add(new PieEntry(dd,"목요일"));
            else if(parseInt(day[0])==5)
                yValues.add(new PieEntry(dd,"금요일"));
            else if(parseInt(day[0])==6)
                yValues.add(new PieEntry(dd,"토요일"));

        }

        pieChart.animateY(1000); //이션


        PieDataSet dataSet = new PieDataSet(yValues, "요일");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(DGU_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setData(data);


        return view;

    }

    public void moststudy(String date){

        int mname[] = mDBHelper.getMostStudytimeIdArray(date);

        for (int i = 0; i < mname.length; i++) {
            Log.v("StringIds", "i:" + i + ", name[i]:" + mname[i]);

            String studytime = mDBHelper.getStudytime(mname[i]);


            String str[] = mDBHelper.getSubjectnameOrLicensename(mname[i]).split(",");
            if(str[0]==null){ str[0]=""; str[1]="-";}
            if(i==0)
            {
                gold.setText(str[1]);
                gold2.setText(studytime);
            }
            else if(i==1){
                silver.setText(str[1]);
                silver2.setText(studytime);
            }
            else if(i==2) {
                bronze.setText(str[1]);
                bronze2.setText(studytime);
            }


        }

    }

    public float[] getTimeTableData(String date){
        int[] sum = new int[ONEDAYTIME];      //한달치 타임테이블 더한 값
        int[] hour_sum = new int[24];
        float[] total_sum = new float[24];
        ArrayList<String[]> tableContList = mDBHelper.getMonthlyTimeTable(date);           //스트링 배열의 리스트 리턴
        int size = tableContList.size();

        for(int i = 0 ; i < size ; i++){
            String [] t_content = tableContList.get(i);     //List의 i번째 스트링 배열 받아옴
            int [] t_content_int = new int[ONEDAYTIME];
            for(int j = 0; j < ONEDAYTIME ; j++){
                t_content_int[j] = Integer.parseInt(t_content[j]);      //스트링 배열을 int 배열로 바꿔줌
                sum[j] += t_content_int[j];     //sum에 계속 합계를 넣어줌
            }
        }
        for (int z = 0; z < 24 ; z++){
            for(int x = (60*z); x < (60*(z+1)) ; x++){
                hour_sum[z] += sum[x];
            }
            Log.d("hour","z"+z);
            Log.d("hour","hour"+hour_sum[z]);
            total_sum[z] = ((float)hour_sum[z]/(size * 60))*100;
            Log.d("hour","total : "+total_sum[z]);
        }
        return total_sum;
    }

    public void setTimeTableGraph(float[] data){
        List<Entry> contents = new ArrayList<>();
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
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(MainColor);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(MainColor);
        xAxis.setAxisLineWidth(1.75f);
        xAxis.setTextSize(10f);
        xAxis.setLabelCount(24);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextColor(MainColor);
        yAxis.setGridColor(MainColor);
        yAxis.setAxisMinimum(0.0f);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(true);
        yAxis.setAxisLineColor(MainColor);
        yAxis.setAxisLineWidth(1.75f);
        yAxis.setGranularityEnabled(true);
        yAxis.setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);

    }




}
