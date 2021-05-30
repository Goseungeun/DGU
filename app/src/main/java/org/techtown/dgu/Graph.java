package org.techtown.dgu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Graph extends Fragment {
    private LineChart chart;
    private ArrayList<Entry> values = new ArrayList<>();    //그래프에 표시할 값을 가지고 있는 arrayList
    private final static int SEMESTER_NUM=9;                                        //학기 개수

    //학기 버튼 (가로스크롤바)
    private Button[] semester = new Button[SEMESTER_NUM];
    Integer[] semesterButtonIDs = {
            R.id.button1_1, R.id.button1_2 , R.id.button2_1, R.id.button2_2,
            R.id.button3_1, R.id.button3_2, R.id.button4_1, R.id.button4_2, R.id.button_etc
    };
    String[] semesterName={"1-1", "1-2", "2-1", "2-2" , "3-1", "3-2", "4-1", "4-2", "기타"};          //학기 이름 담을 리스트
    float[] semester_score_list= new float[SEMESTER_NUM];    //학기별 평점 담을 리스트



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.graph,container,false);

        initUI(view);

        return view;
    }

    private void initUI(ViewGroup rootView){
        //line graph 기본 설정
        chart = rootView.findViewById(R.id.graph_chart);

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setViewPortOffsets(10f,10f,10f,10f);
        chart.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.calendar_border));
        chart.setPadding(15,15,15,15);

        ///
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        ///

        Legend legend = chart.getLegend();
        legend.setEnabled(false);


        //y축 설정
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0.5f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setTextColor(Color.rgb(255,192,56));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


        //x축 설정
        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(getResources().getColor(R.color.green));
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(10f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.5f);

        //x축 이름 설정
        final ArrayList<String> xEntrys = new ArrayList<>();
        for(int i=0;i<semesterName.length;i++) {
            xEntrys.add(semesterName[i]);
        }
        chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xEntrys));

        //데이터 설정
        setData();
    }

    private void setData(){
        //데이터 입력하기
        InputValues();


        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getResources().getColor(R.color.green));
        set1.setValueTextColor(Color.BLACK);
        set1.setLineWidth(2.5f);
        set1.setDrawCircles(true);
        set1.setDrawValues(true);
        set1.setFillAlpha(65);
        set1.setFillColor(getResources().getColor(R.color.green));
        set1.setCircleColor(getResources().getColor(R.color.green));
        set1.setHighLightColor(getResources().getColor(R.color.green));
        set1.setDrawCircleHole(true);
        set1.setCircleHoleRadius(3f);
        set1.setCircleRadius(5f);


        ///
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        ///
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(15f);

        // set data
        chart.setData(data);
        //chart.invalidate();
    }




    ///의심스러워!
    //그래프에 들어갈 값을 입력한다.
    private void InputValues() {

        CalculateGPA();

        for(int i=0;i<SEMESTER_NUM;i++){
            int x_values = i+1;
            if(semester_score_list[i]!=0f){
                values.add(new Entry(x_values,semester_score_list[i]));
            }
        }
    }

    private void CalculateGPA() {
        GraphTable_DB G_db = new GraphTable_DB(getContext());
        //학기 평균 학점 계산하기
        for(int i=0;i<SEMESTER_NUM;i++){
            semester_score_list[i] =(float) G_db.CalculateGPA(semesterName[i]);
            if(semester_score_list[i]!=0f){
                semester_score_list[i]= (Math.round(semester_score_list[i]*100)/100f);
            }
        }
    }


}

