package org.techtown.dgu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class GraphTable extends Fragment {
    private ViewGroup view;

    private GridLayout table;                                                       //graph table
    private final static int COLUMN = 3;                                            //graph table column
    private final static int ROW=10;                                                //graph table row
    private final static int SEMESTER_NUM=9;                                        //학기 개수
    private EditText[] subject_name=new EditText[ROW];                              //column 기준 0 = 과목이름
    private EditText[] credit=new EditText[ROW];                                    //column 기준 1 = 학점
    private TextView[] score=new TextView[ROW];                                     //column 기준 2 = 성적
    private String[] scorelist=new String[ROW];                                     //성적에 들어갈 값들
    private TextView Tv_semester;                                                   //학기를 나타내 주는 Textview
    private TextView Tv_semester_score;                                             //학기 평균 학점을 나타내 주는 Textview
    private double[] semester_score_list=new double[SEMESTER_NUM];                  //각 학기별 평균 학점을 저장할 doubldlist
    private TextView Tv_total_score;                                                //전체 평균학점을 나타내 주는 Textview
    private TextView save;                                                          //저장버튼
    private int cur_semester_index;                                                 //현재 표시해야할 semester의 index값이 무엇인지.

    //학기 버튼 (가로스크롤바)
    private Button[] semester = new Button[SEMESTER_NUM];
    Integer[] semesterButtonIDs = {
            R.id.button1_1, R.id.button1_2 , R.id.button2_1, R.id.button2_2,
            R.id.button3_1, R.id.button3_2, R.id.button4_1, R.id.button4_2, R.id.button_etc
    };

    //DB
    GraphTable_DB[] table_dbs=new GraphTable_DB[SEMESTER_NUM];

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.graph_table, container,false);

        table=(GridLayout)view.findViewById(R.id.g_table);
        table.setColumnCount(COLUMN);
        table.setRowCount(ROW+1);

        Tv_semester_score=view.findViewById(R.id.semester_score);
        Tv_total_score=view.findViewById(R.id.total_score);

        //현재 표시해야할 semester의 index값 초기화
        //0: 1-1, 1: 1-2, 2: 2-1, 3: 2-2 , 4:3-1, 5:3-2, 6:4-1, 7:4-2, 8:기타학기
        cur_semester_index=0;
        for (int i=0;i<semesterButtonIDs.length;i++){semester[i]=view.findViewById(semesterButtonIDs[i]);}

        //table_db생성
        createTableDBs();

        //table 초기화
        init_table(table);

        //학기를 나타내 주는 textview 연결
        Tv_semester = view.findViewById(R.id.tv_semester);

        //학기 버튼 연결
        semesterButtonConnection();

        //각 학기별 평균 학점을 저장할 doubldlist 초기화
        init_semester_score_list();

        //저장 버튼 연결 (학기 평균 학점, 전체 평균 학점 계산하기 들어있음)
        saveButtonAction();


        return view;

    }

    //각 학기별 평균 학점을 저장할 doubldlist 초기화
    private void init_semester_score_list() {
        for(int i=0;i<semester_score_list.length;i++){
            semester_score_list[i]=table_dbs[i].CalculateGPA();
        }
    }

    private void saveButtonAction() {
        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            //delete를 했는지 안했는지 표시해주는 상태 값 (0:안함 , 1: 한번이상 함)
            int state_delete = 0;
            @Override
            public void onClick(View v) {

                //delete를 처음한 행의 번호를 반환
                int [] rowindex_delete =new int [ROW];
                for(int i=0;i<ROW;i++){
                    rowindex_delete[i]=-1;
                }

                int j=0;
                for(int i =0;i<ROW;i++){

                    if(!subject_name[i].getText().toString().equals("") && !credit[i].getText().toString().equals("") && !score[i].getText().toString().equals("")){
                        //하나라도 비어있으면 DB에 넣지 않는다.
                        Toast.makeText(getActivity(),""+i, Toast.LENGTH_SHORT).show();
                        //공백 삭제용
                        String _subject_name =subject_name[i].getText().toString();
                        _subject_name = _subject_name.replace(" ", "");

                        String _credit = credit[i].getText().toString().trim();
                        _credit = _credit.replace(" ", "");
                        int integer_credit = Integer.parseInt(_credit);


                        //이미 디비상에서 존재하는 행이 있었다면,
                        if(table_dbs[cur_semester_index].FindAlreadyExistsRowID(i)){
                            Toast.makeText(getActivity(),"update"+i, Toast.LENGTH_SHORT).show();
                            //update
                            table_dbs[cur_semester_index].UpdateGraphTable(i,""+_subject_name,integer_credit+0, ""+score[i].getText().toString());
                        }else{
                            Toast.makeText(getActivity(),"insert"+i, Toast.LENGTH_SHORT).show();
                            //insert
                            table_dbs[cur_semester_index].InsertGraphTable(i,""+_subject_name,integer_credit+0,""+score[i].getText().toString());
                        }

                    }else{
                        //하나라도 비어있으면 DB에서 삭제한다.

                        //이미 디비상에서 존재하는 행이 있었다면,
                        if(table_dbs[cur_semester_index].FindAlreadyExistsRowID(i)){
                            //delete
                            table_dbs[cur_semester_index].DeleteGraphTable(i);
                            rowindex_delete[j]=i;
                            j++;
                            state_delete=1;
                        }
                    }
                }
                //delete를 한번이라도 했다면 RowID를 재정렬해줘야한다.
                if(state_delete==1){
                    table_dbs[cur_semester_index].UpdateGraphTable_RowID(rowindex_delete[0]);
                    state_delete=0;
                }

                //db에 들어간대로 테이블에 업데이트 해주기
                table_dbs[cur_semester_index].ViewGraphTable(subject_name,credit,score);

                //평균학점 계산하기
                CalculateGPA();
            }
        });
    }

    private void CalculateGPA() {

        //학기 평균 학점 계산하기
        semester_score_list[cur_semester_index] = table_dbs[cur_semester_index].CalculateGPA();

        //학기 평균 학점 보여주기
        Tv_semester_score.setText("학기 평균 학점 : "+Math.round(semester_score_list[cur_semester_index]*100)/100.0+"점");

        //전체 평균 학점 계산하기
        double total_score=0.0;

        double sum_of_m_credit_times_m_score=0.0;
        double sum_of_m_credit=0.0;
        for(int i=0;i<table_dbs.length;i++){
            sum_of_m_credit_times_m_score+=table_dbs[i].sum_Of_m_credit_times_m_score();
            sum_of_m_credit+=table_dbs[i].sum_Of_m_credit();
        }
        if(sum_of_m_credit!=0.0&&sum_of_m_credit_times_m_score!=0.0){
            total_score=sum_of_m_credit_times_m_score/sum_of_m_credit;
        }
        //전체 평균 학점 보여주기
        Tv_total_score.setText("전체 평균 학점 : "+Math.round(total_score*100)/100.0+"점");
    }


    //table_dbs 생성하는 함수
    private void createTableDBs() {
        for(int i=0;i<SEMESTER_NUM;i++){
            table_dbs[i]=new GraphTable_DB(getContext(), semester[i].getText().toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init_table(GridLayout table) {
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
                    subject_name[i].setText("");


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
                    credit[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                    credit[i].setText("");

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
                    score[i].setText("");

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

        //table에 내용채워넣기
        if(!table_dbs[cur_semester_index].IsEmpty()) {table_dbs[cur_semester_index].ViewGraphTable(subject_name,credit,score);}

        //평균학점 내용 채워넣기
        CalculateGPA();
    }

    //학기 버튼 연결
    private void semesterButtonConnection() {
        for (int i=0;i<semesterButtonIDs.length;i++){

            //학기 버튼을 누르면 table 채워지도록
            int finalI = i;
            semester[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //현재 표시해야할 semester의 index값
                    //0: 1-1, 1: 1-2, 2: 2-1, 3: 2-2 , 4:3-1, 5:3-2, 6:4-1, 7:4-2, 8:기타학기
                    cur_semester_index=finalI;

                    //학기 버튼에 따라 몇학년 몇학기인지 표시하기
                    String semester_name;
                    if(finalI==8){
                        //기타학기인 경우
                        semester_name="기타 학기";
                    }else{
                        semester_name= (String) ""+semester[finalI].getText().charAt(0)+"학년 "+semester[finalI].getText().charAt(2)+"학기";
                    }
                    Tv_semester.setText(semester_name);

                    //학기 버튼에 따라 디비 불러오기
                    table_dbs[cur_semester_index].ViewGraphTable(subject_name,credit,score);

                    //학기 버튼에 따라 평균학점들 달라지게
                    CalculateGPA();
                }
            });
        }
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
        ListItems.add("값 없음");
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
