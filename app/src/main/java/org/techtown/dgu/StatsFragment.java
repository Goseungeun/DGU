package org.techtown.dgu;

import android.graphics.Color;
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

import org.techtown.dgu.member.SettingFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

public class StatsFragment extends Fragment {
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


}
