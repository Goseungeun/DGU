package org.techtown.dgu;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class AttendanceCheckTable extends Fragment {

    //해당 과목임을 알아볼 수 있는 아이디가 필요!
    //int는 null로 초기화 할 수 없고, Integer은 null로 초기화 가능
    private static final int WEEK = 15;           //몇주차인지
    private static final int DAY_A_WEEK = 2;     //한 주에 몇번 수업하는지

    //0:attendance, 1:late, 2:absent, null : no value
    private Integer[][] checklist = {
            {1,1},{1,1},{1,1},{0,1},{0,1},{1,1},{1,0},{1,2},{1,1},{1,1},{1,1},{2,1},{1,1},
            {1,null},{null,null}
    };
    //checklist.length하면 null도 포함해서 알려준다.

    //여기까지 DB 저장 내용!

    private TextView checkresult;          //Text view for displaying the number of attendance, late, and absent days
    private GridLayout checktable;         //attendance check table
    private int table_column;              //column
    private int table_row;                 //row
    //tablerow id


    private ImageView imgview[][];        //each cell imageview
    private ViewGroup view;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.attendancechecktable_layout, container,false);

        ///Start checkresult write
        //Text view for displaying the number of attendance, late, and absent days
        checkresult = (TextView)view.findViewById(R.id.check_result);
        ViewCheckResult(checkresult);
        ///End checkresult write

        table_row=WEEK;
        table_column=DAY_A_WEEK;

        imgview = new ImageView[table_row][table_column];

        ///Start checktable write
        checktable=(GridLayout)view.findViewById(R.id.check_table); //attendance check table
        checktable.setRowCount(table_row);
        checktable.setColumnCount(table_column);
        checktable.setOrientation(GridLayout.HORIZONTAL);
        for(int i=0;i<table_row;i++){
            //Where to put it (row)
            GridLayout.Spec rowSpec= GridLayout.spec(i);
            for(int j=0;j<table_column;j++){
                //Where to put it (column)
                GridLayout.Spec colSpec = GridLayout.spec(j);

                ///Start imagview basic setting
                imgview[i][j]=new ImageView(getActivity());
                imgview[i][j].setLayoutParams(new ViewGroup.LayoutParams(
                        (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()),
                        (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics())
                ));
                imgview[i][j].setScaleType(ImageView.ScaleType.FIT_CENTER);

                //Change the image displayed in imgview according to the contents of checklist
                //And Change the image color displayed in imgview according to the contents of checklist
                imgview[i][j].setImageResource(
                        imgview_setImageResource(i,j)
                );
                ///End imagview basic setting


                GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);

                //실제 스마트폰 크기 or 애뮬레이터 size를 구해서 배열 수 만큼 나누어줘 layoutparams에 저장해준다.
                int refwidth = getScreenWidthSize(getActivity());
                int refheight = getScreenHeightSize(getActivity());
                gl.width=refwidth/table_column;
                gl.height=refheight/table_row;

                checktable.addView(imgview[i][j],gl);
            }
            ///End checktable write
        }




        return view;
    }


    //출석,지각,결석한 날짜수 표시용 텍스트뷰 채워넣기
    public void ViewCheckResult(TextView Tv){
        int attendNum=0;
        int lateNum=0;
        int absentNum=0;

        //0:attendance, 1:late, 2:absent, null : no value
        for(int i=0;i<WEEK;i++){
            for(int j=0;j<DAY_A_WEEK;j++) {
                if (checklist[i][j] == (Integer) 0) {
                    attendNum++;
                } else if (checklist[i][j] == (Integer) 1) {
                    lateNum++;
                } else if (checklist[i][j] == (Integer) 2) {
                    absentNum++;
                }
            }
        }

        Tv.setText("총 "+WEEK*DAY_A_WEEK+" |   출석 : "+attendNum+" | 지각 : "+lateNum+" | 결석 : "+absentNum);
    }

    //출석,지각,결석에 맞춰서 이미지 표시하기
    public int imgview_setImageResource(int i, int j){
        //0:attendance, 1:late, 2:absent, null : no value
        if(checklist[i][j]==(Integer)0)return(R.drawable.checkmark);
        else if(checklist[i][j]==(Integer)1)return(R.drawable.minus);
        else if(checklist[i][j]==(Integer)2)return(R.drawable.xmark);
        else return(R.drawable.nothing);
    }


    //실제 스마트폰 크기 or 애뮬레이터 width size를 구해주는 함수
    public int getScreenWidthSize(@NonNull Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    //실제 스마트폰 크기 or 애뮬레이터 height size를 구해주는 함수
    public int getScreenHeightSize(@NonNull Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().height() - insets.top - insets.bottom;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }



}
