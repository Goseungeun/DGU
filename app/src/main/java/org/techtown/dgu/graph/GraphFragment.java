package org.techtown.dgu.graph;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment{
    private LineChart chart;

    //학기 이름 담을 리스트
    private String[] semesterName={"1-1", "1-2", "2-1", "2-2" , "3-1", "3-2", "4-1", "4-2","기타"};

    private DGUDB DB;
    private int BackgrounColor, MainColor;
    private TextView Tv_total_score;
    private TextView Tv_total_grades;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.graph,container,false);

        DB=new DGUDB(getContext());
        BackgrounColor= getResources().getColor(R.color.deepgreen);
        MainColor= getResources().getColor(R.color.background);
        chart = view.findViewById(R.id.graph_chart);
        Tv_total_score=view.findViewById(R.id.total_score1);
        Tv_total_grades=view.findViewById(R.id.total_grades1);

        //차트 꾸미기
        setupChart();

        //차트 데이터 넣어주기
        setData();

        //평점, 학점 계산
        CalculateGPA();

        return view;
    }

    private void CalculateGPA() {

        //전체 평점 보여주기
        Tv_total_score.setText(""+(float)(Math.round(DB.CalculatetotalGPA()*100)/100.0));

        //전체 학점 보여주기
        Tv_total_grades.setText(""+DB.getTotalGrades());
    }

    private void setData() {
        ArrayList<Entry> values = new ArrayList<>();

        float score[] = DB.getGraph_gpa();

        //점수넣는곳
        for (int i = 0; i < score.length; i++) {
            values.add(new Entry((float)i, score[i]));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {

            set1 = new LineDataSet(values, "DataSet 1");
            // set1.setFillAlpha(110);
            // set1.setFillColor(Color.RED);

            set1.setLineWidth(1.75f);
            set1.setCircleRadius(5f);
            set1.setCircleHoleRadius(2.5f);
            set1.setColor(MainColor);
            set1.setCircleColor(MainColor);
            set1.setHighLightColor(MainColor);
            set1.setCircleHoleColor(BackgrounColor);
            set1.setDrawValues(false);


            // set color of filled area
            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_background);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }


    private void setupChart() {

        // no description text
        //아래에 조만하게 쓰인 글씨(설명)
        chart.getDescription().setEnabled(false);

        // enable / disable grid background
        chart.setDrawGridBackground(false);


        // enable touch gestures
        //touch가 먹는지 안먹는지
        chart.setTouchEnabled(false);


        //범례표시 ( ex) 흰색은 dataset1임 같은거 표시하는거 )
        // !!!get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        //X축 꾸미기
        {
            XAxis xAxis = chart.getXAxis();
            xAxis.setTextSize(11f);
            xAxis.setTextColor(MainColor);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);

            //X축에 넣을 말 정하기
            final ArrayList<String> xEntrys = new ArrayList<>();
            for(int i=0;i<semesterName.length;i++) {
                xEntrys.add(semesterName[i]);
            }
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xEntrys));

        }

        //Y축 꾸미기
        {
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTextColor(MainColor);
            leftAxis.setGridColor(MainColor);
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setAxisMaximum(4.5f);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setDrawTopYLabelEntry(true);
            leftAxis.setDrawGridLines(true);
            leftAxis.setDrawAxisLine(false);

            leftAxis.setGranularityEnabled(true);

            leftAxis.setEnabled(true);
            chart.getAxisRight().setEnabled(false);
        }

        // animate calls invalidate()...
        chart.animateX(1500);

    }

}

