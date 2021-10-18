package org.techtown.dgu;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.utils.Easing;
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
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class StatsFragment extends Fragment {
    private LineChart lineChart;
    private DGUDB mDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.statsfragment, container, false);
        mDBHelper = new DGUDB(getContext());



        TextView month=view.findViewById(R.id.sta_month);

        //가장 많이한 공부
        lineChart = view.findViewById(R.id.moststudytime);
        TextView gold = view.findViewById(R.id.gold);
        TextView silver = view.findViewById(R.id.silver);
        TextView bronze = view.findViewById(R.id.bronze);
        TextView gold2 = view.findViewById(R.id.gold2);
        TextView silver2 = view.findViewById(R.id.silver2);
        TextView bronze2 = view.findViewById(R.id.bronze2);
        ProgressBar goldprogress=view.findViewById(R.id.progressBargold);
        ProgressBar silverprogress=view.findViewById(R.id.progressBarsilver);
        ProgressBar bronzeprogress=view.findViewById(R.id.progressBarbronze);

        int mname[] = mDBHelper.getMostStudytimeIdArray();

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





        //요일별 공부시간
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 5));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 3));
        entries.add(new Entry(5, 0));


        LineDataSet lineDataSet = new LineDataSet(entries, "공부시간");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);     // 곡선형으로 바꾸기
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
        return view;
    }
}
