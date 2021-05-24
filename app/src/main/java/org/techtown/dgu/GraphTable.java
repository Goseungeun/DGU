package org.techtown.dgu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GraphTable extends Fragment {
    private ViewGroup view;

    private GridLayout table;                                   //graph table
    private final static int COLUMN = 3;                        //graph table column
    private final static int ROW=10;                            //graph table row
    private EditText[] subject_name=new EditText[ROW];          //column 기준 0 = 과목이름
    private EditText[] credit=new EditText[ROW];                //column 기준 1 = 학점
    private TextView[] score=new TextView[ROW];                 //column 기준 2 = 성적

    //DB에서 받아야 할 값
    private String[] scorelist={
            "A+","A0","B+","B0","C+","C0","D+","D0","F","P"
    };//"A+","A0","B+","B0","C+","C0","D+","D0","F","P","NP","" 이렇게 12개의 값이 있음

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.graph_table, container,false);

        table=(GridLayout)view.findViewById(R.id.g_table);
        table.setColumnCount(COLUMN);
        table.setRowCount(ROW+1);

        //column기준 0:과목이름, 1: 학점, 2:성적
        //0과 1은 edittext , 2는 textview로 구현할꺼임

        //index 먼저
        for(int j=0;j<COLUMN;j++){
            TextView tv = new TextView(getActivity());
            GridLayout.Spec rowSpec1 = GridLayout.spec(0);
            GridLayout.Spec colSpec1= GridLayout.spec(j,GridLayout.FILL,1f);

            ViewGroup.LayoutParams tparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            tv.setLayoutParams(tparams);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(getResources().getColor(R.color.deepgreen));

            //column기준 0:과목이름, 1: 학점, 2:성적
            if(j==0)tv.setText("과목 이름");
            else if(j==1)tv.setText("학점");
            else tv.setText("성적");

            GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec1,colSpec1);
            gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
            gl.setMargins(2,2,2,2);

            table.addView(tv,gl);

        }

        //table 내용
        for(int j=0;j<COLUMN;j++){
            //Where to put it (column)
            GridLayout.Spec colSpec= GridLayout.spec(j,GridLayout.FILL,1f);
            ViewGroup.LayoutParams sparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            if(j==0){
                //column기준 0:과목이름
                for(int i =0;i<ROW;i++){
                    //Where to put it (row)
                    int k=i+1;  //첫 행에는 index들어가 있어서
                    GridLayout.Spec rowSpec= GridLayout.spec(k,GridLayout.FILL);
                    subject_name[i]=new EditText(getActivity());

                    subject_name[i].setLayoutParams(sparams);
                    subject_name[i].setTextColor(getResources().getColor(R.color.deepgreen));
                    subject_name[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    subject_name[i].setBackgroundColor(Color.WHITE);
                    subject_name[i].setPadding(10,0,0,10);

                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);

                    gl.width= GridLayout.LayoutParams.WRAP_CONTENT;
                    gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                    gl.setMargins(2,2,2,2);
                    table.addView(subject_name[i],gl);
                }

            }
            else if(j==1){
                //column기준 1: 학점
                for(int i =0;i<ROW;i++){
                    //Where to put it (row)
                    int k=i+1;  //첫 행에는 index들어가 있어서
                    GridLayout.Spec rowSpec= GridLayout.spec(k,GridLayout.FILL);
                    credit[i]=new EditText(getActivity());

                    credit[i].setLayoutParams(sparams);
                    credit[i].setTextColor(getResources().getColor(R.color.deepgreen));
                    credit[i].setBackgroundColor(Color.WHITE);
                    credit[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    credit[i].setGravity(Gravity.CENTER);

                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);

                    gl.width= GridLayout.LayoutParams.WRAP_CONTENT;
                    gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                    gl.setMargins(2,2,2,2);
                    table.addView(credit[i],gl);


                }
            }
            else{
                //column기준 2: 성적
                for(int i =0;i<ROW;i++){
                    //Where to put it (row)
                    int k=i+1;  //첫 행에는 index들어가 있어서
                    GridLayout.Spec rowSpec= GridLayout.spec(k,GridLayout.FILL);
                    score[i]=new TextView(getActivity());

                    score[i].setLayoutParams(sparams);
                    score[i].setTextColor(getResources().getColor(R.color.deepgreen));
                    score[i].setBackgroundColor(Color.WHITE);
                    score[i].setText(scorelist[i]);
                    score[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    score[i].setGravity(Gravity.CENTER);

                    ///Start dialog of score[i]
                    int finalI = i;
                    score[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_show(finalI);
                        }
                    });
                    ///End dialog of score[i]

                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);

                    gl.width= GridLayout.LayoutParams.WRAP_CONTENT;
                    gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                    gl.setMargins(2,2,2,2);
                    table.addView(score[i],gl);



                }
            }

        }

        return view;

    }
    //dialog
    public void dialog_show(int i){
        //i : row값

        //list에 들어갈 값 설정
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("A+");
        ListItems.add("A0");
        ListItems.add("B+");
        ListItems.add("B0");
        ListItems.add("C+");
        ListItems.add("C0");
        ListItems.add("D+");
        ListItems.add("D0");
        ListItems.add("F");
        ListItems.add("P");
        ListItems.add("NP");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        //선택한 값 표시용 리스트
        final List selectedItems = new ArrayList();

        //default값 정하기
        int defaultItem;
        //"A+":0,"A0":1,"B+":2,"B0":3,"C+":4,"C0":5,"D+":6,"D0":7,"F":8,"P":9,"NP":10,"":11
        if(scorelist[i]=="A+")defaultItem=0;
        else if(scorelist[i]=="A0")defaultItem=1;
        else if(scorelist[i]=="B+")defaultItem=2;
        else if(scorelist[i]=="B0")defaultItem=3;
        else if(scorelist[i]=="C+")defaultItem=4;
        else if(scorelist[i]=="C0")defaultItem=5;
        else if(scorelist[i]=="D+")defaultItem=6;
        else if(scorelist[i]=="D0")defaultItem=7;
        else if(scorelist[i]=="F")defaultItem=8;
        else if(scorelist[i]=="P")defaultItem=9;
        else if(scorelist[i]=="NP")defaultItem=10;
        else defaultItem=11;
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
                        //scorelist 값 바꾸기 //"A+":0,"A0":1,"B+":2,"B0":3,"C+":4,"C0":5,"D+":6,"D0":7,"F":8,"P":9,"NP":10,"":11
                        int result = (int)selectedItems.get(0);
                        if(result==0)scorelist[i]="A+";
                        else if(result==1)scorelist[i]="A0";
                        else if(result==2)scorelist[i]="B+";
                        else if(result==3)scorelist[i]="B0";
                        else if(result==4)scorelist[i]="C+";
                        else if(result==5)scorelist[i]="C0";
                        else if(result==6)scorelist[i]="D+";
                        else if(result==7)scorelist[i]="D0";
                        else if(result==8)scorelist[i]="F";
                        else if(result==9)scorelist[i]="P";
                        else if(result==10)scorelist[i]="NP";
                        else scorelist[i]="";

                        score[i].setText(scorelist[i]);

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
