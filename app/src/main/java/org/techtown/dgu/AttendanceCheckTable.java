package org.techtown.dgu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class AttendanceCheckTable extends Fragment {

    //해당 과목임을 알아볼 수 있는 아이디가 필요!
    //int는 null로 초기화 할 수 없고, Integer은 null로 초기화 가능
    private static final int WEEK = 15;           //몇주차인지
    private static final int DAY_A_WEEK = 7;     //한 주에 몇번 수업하는지

    //0:attendance, 1:late, 2:absent, null : no value
    public Integer[][] checklist = {
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

                    ///End imagview basic setting

                    ///Start dialog of imagview
                    int finalJ = j;
                    int finalI = i;
                    imgview[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_show(finalI,finalJ);
                        }
                    });
                    ///End dialog of imagview

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


        for(int i=0;i<table_row;i++){
            for(int j=0;j<table_column;j++){

            }
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

    //dialog
    public void dialog_show(int i,int k){
        int j=k-1;      //i,j : checklist의 위치 파악을 위한 값
        //table의 0번째 column이 index라서 이렇게 계산함.


        //list에 들어갈 값 설정
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("출석");
        ListItems.add("지각");
        ListItems.add("결석");
        ListItems.add("아직 안들음");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        //선택한 값 표시용 리스트
        final List selectedItems = new ArrayList();

        //default값 정하기
        int defaultItem;
            //0:attendance, 1:late, 2:absent, null : no value
        if(checklist[i][j]==(Integer)0)defaultItem=0;
        else if(checklist[i][j]==(Integer)1)defaultItem=1;
        else if(checklist[i][j]==(Integer)2)defaultItem=2;
        else defaultItem=3;
        selectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItems.clear();
                        selectedItems.add(which);   //선택한 항목을 selectedItem에 집어넣는다.
                    }
                });
        builder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //seletedItems.get(0) = 선택한 항목
                        //checklist 값 바꾸기 //0:attendance, 1:late, 2:absent, null : no value
                        int result = (int)selectedItems.get(0);
                        if(result==0)checklist[i][j]=(Integer)0;
                        else if(result==1)checklist[i][j]=(Integer)1;
                        else if(result==2)checklist[i][j]=(Integer)2;
                        else checklist[i][j]=(Integer)null;


                        ///Start UPDATE
                        //checkresult update
                        ViewCheckResult(checkresult);
                        //imgview update
                        imgview[i][k].setImageResource(
                                imgview_setImageResource(i,k)
                        );
                        ImageViewCompat.setImageTintList(imgview[i][k], ColorStateList.valueOf(
                                getResources().getColor(imgview_setTint(i,k))
                        ));
                        ///End UPDATE
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }



}
