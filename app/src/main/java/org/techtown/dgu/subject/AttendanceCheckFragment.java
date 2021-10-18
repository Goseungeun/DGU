//package org.techtown.dgu.subject;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.widget.ImageViewCompat;
//import androidx.fragment.app.Fragment;
//
//import org.techtown.dgu.MainActivity;
//import org.techtown.dgu.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AttendanceCheckFragment extends Fragment {
//    private  ViewGroup view;
//
//    private String subName;                //과목 이름
//    Subject_DB db;   //DB
//
//    private int WEEK;           //몇주차인지
//    private int DAY_A_WEEK;     //한 주에 몇번 수업하는지
//
//    //0:attendance, 1:late, 2:absent, null : no value
//    public int[][] checklist;
//    //checklist.length하면 null도 포함해서 알려준다.
//
//
//    private TextView checkresult;          //Text view for displaying the number of attendance, late, and absent days
//    private GridLayout checktable;         //attendance check table
//    private int table_column;              //column
//    private int table_row;                 //row
//    //tablerow id
//
//
//    private ImageView imgview[][];        //each cell imageview
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        db = new Subject_DB(getContext());
//
//        view = (ViewGroup) inflater.inflate(R.layout.attendancecheck, container,false);
//        TextView tv = view.findViewById(R.id.Tv_attendancecheck);
//        tv.setText(""+subName+" 출석체크");
//
//        //BackButton1을 누르면 실행되는 함수
//        ImageButton backbutton = (ImageButton)view.findViewById(R.id.BackButton1); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
//
//        backbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
//                ((MainActivity)getActivity()).replaceFragment(new SubjectFragment());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
//            }
//        });
//
//        ///attendancechecktable 내용 옮겨옴.
//        //WEEK , DAY_OF_WEEK, checklist 초기화
//        Init();
//
//        ///Start checkresult write
//        //Text view for displaying the number of attendance, late, and absent days
//        checkresult = (TextView)view.findViewById(R.id.check_result);
//        ViewCheckResult(checkresult);
//        ///End checkresult write
//
//        table_row=WEEK;
//        table_column=DAY_A_WEEK+1;//index가 0번째 column
//
//        imgview = new ImageView[table_row][table_column];
//
//        ///Start checktable write
//        checktable=(GridLayout)view.findViewById(R.id.check_table); //attendance check table
//        checktable.setRowCount(table_row);
//        checktable.setColumnCount(table_column);
//        checktable.setOrientation(GridLayout.HORIZONTAL);
//        for(int i=0;i<table_row;i++){
//            //Where to put it (row)
//            GridLayout.Spec rowSpec= GridLayout.spec(i);
//            for(int j=0;j<table_column;j++){
//                //Where to put it (column)
//                GridLayout.Spec colSpec = GridLayout.spec(j);
//
//                if(j==0){
//                    //index
//                    TextView index = new TextView(getActivity());
//                    ViewGroup.LayoutParams indexparams = new ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT
//                    );
//                    index.setLayoutParams(indexparams);
//                    if(i<9){
//                        index.setText("0"+(i+1)+"주차");
//                    }else{
//                        index.setText(""+(i+1)+"주차");
//                    }
//
//                    index.setBackgroundColor(getResources().getColor(R.color.deepgreen));
//                    index.setTextColor(Color.WHITE);
//                    index.setGravity(Gravity.CENTER);
//
//                    index.setPadding(10,10,10,10);
//
//                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);
//
//                    gl.width= GridLayout.LayoutParams.MATCH_PARENT;
//                    gl.height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
//                    checktable.addView(index,gl);
//                }
//                else{
//                    ///Start imagview basic setting
//                    imgview[i][j]=new ImageView(getActivity());
//                    ViewGroup.LayoutParams imgparams = new ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT
//                    );
//                    imgview[i][j].setLayoutParams(imgparams);
//                    imgview[i][j].setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    imgview[i][j].setPadding(20,20,20,20);
//                    imgview[i][j].setBackgroundColor(Color.WHITE);
//
//
//                    //Change the image displayed in imgview according to the contents of checklist
//                    //And Change the image color displayed in imgview according to the contents of checklist
//                    imgview[i][j].setImageResource(
//                            imgview_setImageResource(i,j)
//                    );
//                    ImageViewCompat.setImageTintList(imgview[i][j], ColorStateList.valueOf(
//                            getResources().getColor(imgview_setTint(i,j))
//                    ));
//
//                    ///End imagview basic setting
//
//                    ///Start dialog of imagview
//                    int finalJ = j;
//                    int finalI = i;
//                    imgview[i][j].setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog_show(finalI,finalJ);
//                        }
//                    });
//                    ///End dialog of imagview
//
//                    GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec,colSpec);
//
//                    gl.width= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
//                    gl.height=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
//
//
//                    int marginsize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
//                    gl.setMargins(
//                            marginsize,marginsize,marginsize,marginsize
//                    );
//
//
//                    checktable.addView(imgview[i][j],gl);
//                }
//            }
//            ///End checktable write
//
//
//        }
//
//
//        for(int i=0;i<table_row;i++){
//            for(int j=0;j<table_column;j++){
//
//            }
//        }
//        return view;
//    }
//
//    public void setSubName(String subName) {
//        this.subName=subName;
//    }
//
//    private void Init() {
//        WEEK=db.Output_week(subName);
//        DAY_A_WEEK=db.Output_weekFre(subName);
//        checklist = db.Output_AttendanceCheck_subname(subName);
//    }
//
//
//    //출석,지각,결석한 날짜수 표시용 텍스트뷰 채워넣기
//    public void ViewCheckResult(TextView Tv){
//        int attendNum=0;
//        int lateNum=0;
//        int absentNum=0;
//
//        //0:attendance, 1:late, 2:absent, -1 : no value
//        for(int i=0;i<WEEK;i++){
//            for(int j=0;j<DAY_A_WEEK;j++) {
//                if (checklist[i][j] == 0) {
//                    attendNum++;
//                } else if (checklist[i][j] == 1) {
//                    lateNum++;
//                } else if (checklist[i][j] == 2) {
//                    absentNum++;
//                }
//            }
//        }
//
//        Tv.setText("총 "+WEEK*DAY_A_WEEK+" |   출석 : "+attendNum+" | 지각 : "+lateNum+" | 결석 : "+absentNum);
//    }
//
//    //출석,지각,결석에 맞춰서 이미지 표시하기
//    public int imgview_setImageResource(int i, int k){
//        int j=k-1;
//        //0:attendance, 1:late, 2:absent, -1 : no value
//        if(checklist[i][j]==-1)return(R.drawable.nothing);
//        else if(checklist[i][j]==1)return(R.drawable.minus);
//        else if(checklist[i][j]==2)return(R.drawable.xmark);
//        else return(R.drawable.checkmark);
//    }
//
//    //출석,지각,결석에 맞춰서 이미지 색칠하기
//    public int imgview_setTint(int i,int k){
//        int j=k-1;
//        //0:attendance, 1:late, 2:absent, -1 : no value
//        if(checklist[i][j]==0)return(R.color.checkmark);
//        else if(checklist[i][j]==1)return(R.color.minus);
//        else return(R.color.xmark);
//    }
//
//    //dialog
//    public void dialog_show(int i,int k){
//        int j=k-1;      //i,j : checklist의 위치 파악을 위한 값
//        //table의 0번째 column이 index라서 이렇게 계산함.
//
//
//        //list에 들어갈 값 설정
//        final List<String> ListItems = new ArrayList<>();
//        ListItems.add("출석");
//        ListItems.add("지각");
//        ListItems.add("결석");
//        ListItems.add("아직 안들음");
//        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
//
//        //선택한 값 표시용 리스트
//        final List selectedItems = new ArrayList();
//
//        //default값 정하기
//        int defaultItem;
//        //0:attendance, 1:late, 2:absent, -1 : no value
//        if(checklist[i][j]==0)defaultItem=0;
//        else if(checklist[i][j]==1)defaultItem=1;
//        else if(checklist[i][j]==2)defaultItem=2;
//        else defaultItem=3;
//        selectedItems.add(defaultItem);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setSingleChoiceItems(items, defaultItem,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedItems.clear();
//                        selectedItems.add(which);   //선택한 항목을 selectedItem에 집어넣는다.
//                    }
//                });
//        builder.setPositiveButton("Save",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //seletedItems.get(0) = 선택한 항목
//                        //checklist 값 바꾸기 //0:attendance, 1:late, 2:absent, -1 : no value
//                        int result = (int)selectedItems.get(0);
//                        if(result==0)checklist[i][j]=0;
//                        else if(result==1)checklist[i][j]=1;
//                        else if(result==2)checklist[i][j]=2;
//                        else checklist[i][j]=-1;
//
//
//                        ///Start UPDATE
//                        //checkresult update
//                        ViewCheckResult(checkresult);
//                        //imgview update
//                        imgview[i][k].setImageResource(
//                                imgview_setImageResource(i,k)
//                        );
//                        ImageViewCompat.setImageTintList(imgview[i][k], ColorStateList.valueOf(
//                                getResources().getColor(imgview_setTint(i,k))
//                        ));
//
//                        ///여기 id계산하는거 헷갈림
//                        db.UpdateAttendanceCheck(subName,i*DAY_A_WEEK+j+1, checklist[i][j]);
//                        ///End UPDATE
//                    }
//                });
//        builder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        builder.show();
//    }
//
//
//
//}
//
//
