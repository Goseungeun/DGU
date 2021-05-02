package org.techtown.dgu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
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
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

public class AttendanceCheckTable extends Fragment {

    //해당 과목임을 알아볼 수 있는 아이디가 필요!
    //int는 null로 초기화 할 수 없고, Integer은 null로 초기화 가능
    private static final int WEEK = 15;           //몇주차인지
    private static final int DAY_A_WEEK = 7;     //한 주에 몇번 수업하는지

    //0:attendance, 1:late, 2:absent, null : no value
    private Integer[][] checklist = {
            {0,1,0,1,0,1,2},{1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{0,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},{1,0,1,1,1,1,1},{1,2,1,1,1,1,1},{1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{2,1,1,1,1,1,1},{1,1,1,1,1,1,1},
            {0,1,1,1,1,1,1},{1,null,1,1,1,1,1},{null,null,1,1,1,1,1}
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
        table_column=DAY_A_WEEK+1;//index가 0번째 column

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

                if(j==0){
                    //index
                    TextView index = new TextView(getActivity());
                    ViewGroup.LayoutParams indexparams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    index.setLayoutParams(indexparams);
                    if(i<9){
                        index.setText("0"+(i+1)+"주차");
                    }else{
                        index.setText(""+(i+1)+"주차");
                    }

                    index.setBackgroundColor(getResources().getColor(R.color.deepgreen));
                    index.setTextColor(Color.WHITE);
                    index.setGravity(Gravity.CENTER);

                    index.setPadding(10,10,10,10);

                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);

                    gl.width= GridLayout.LayoutParams.MATCH_PARENT;
                    gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

                    checktable.addView(index,gl);
                }
                else{
                    ///Start imagview basic setting
                    imgview[i][j]=new ImageView(getActivity());
                    ViewGroup.LayoutParams imgparams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    imgview[i][j].setLayoutParams(imgparams);
                    imgview[i][j].setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imgview[i][j].setPadding(20,20,20,20);
                    imgview[i][j].setBackgroundColor(Color.WHITE);


                    //Change the image displayed in imgview according to the contents of checklist
                    //And Change the image color displayed in imgview according to the contents of checklist
                    imgview[i][j].setImageResource(
                            imgview_setImageResource(i,j)
                    );
                    ImageViewCompat.setImageTintList(imgview[i][j], ColorStateList.valueOf(
                            getResources().getColor(imgview_setTint(i,j))
                    ));
                    //버튼마다 다이어로그 띄우기
                    imgview[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            builder.setTitle("출석 체크").setMessage(R.string.attendance_string);
                            AlertDialog alertDialog = builder.create();

                            alertDialog.show();

                        }
                    });

                    ///End imagview basic setting

                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);

                    gl.width= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());


                    int marginsize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                    gl.setMargins(
                            marginsize,marginsize,marginsize,marginsize
                    );


                    checktable.addView(imgview[i][j],gl);
                }
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
    public int imgview_setImageResource(int i, int k){
        int j=k-1;
        //0:attendance, 1:late, 2:absent, null : no value
        if(checklist[i][j]==(Integer)0)return(R.drawable.checkmark);
        else if(checklist[i][j]==(Integer)1)return(R.drawable.minus);
        else if(checklist[i][j]==(Integer)2)return(R.drawable.xmark);
        else return(R.drawable.nothing);
    }

    //출석,지각,결석에 맞춰서 이미지 색칠하기
    public int imgview_setTint(int i,int k){
        int j=k-1;
        //0:attendance, 1:late, 2:absent, null : no value
        if(checklist[i][j]==(Integer)0)return(R.color.checkmark);
        else if(checklist[i][j]==(Integer)1)return(R.color.minus);
        else  return(R.color.xmark);
    }




}
