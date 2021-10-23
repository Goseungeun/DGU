package org.techtown.dgu.subject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.MainActivity;
import org.techtown.dgu.R;
import org.techtown.dgu.StopwatchFragment;
import org.techtown.dgu.homework.homework;
import org.techtown.dgu.homework.homeworkAdapter;
import org.techtown.dgu.homework.homework_DB;
import org.techtown.dgu.studylicense.LicenseItem;
import org.techtown.dgu.test.SubTestItem;
import org.techtown.dgu.test.SubTestAdapter;
import org.techtown.dgu.test.SubTest_DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class  SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.studysubViewHolder>{

    private ArrayList<SubjectItem> subList;
    private Context mContext;
    ArrayList<homework> homeworkList;
    ArrayList<SubTestItem> subTestList;
    private DGUDB db;

    
    public SubjectAdapter(Context context, ArrayList<SubjectItem> subList){
        this.subList = subList;
        this.mContext = context;
        db = new DGUDB(mContext);
    }

    @Override
    public studysubViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_item,viewGroup,false);
        studysubViewHolder sh = new studysubViewHolder(v);
        return sh;
    }

    @Override
    public void onBindViewHolder(studysubViewHolder StudysubviewHolder, int position){
        SubjectItem subItem = subList.get(position);
        homeworkList = subList.get(position).getHwList();
        subTestList = subList.get(position).getTestList();
        homeworkAdapter hwAdapter = new homeworkAdapter(mContext,homeworkList);
        SubTestAdapter testAdapter = new SubTestAdapter(mContext,subTestList);

        if(db.SearchStudytimeID(subItem.getId(),null)!=0 && subItem.getId()!=null){
            //studytime 테이블에 정보 있으면 그 시간 불러오기
            subItem.setSubtime(db.getStudytime(db.SearchStudytimeID(subItem.getId(),null)));
        }else{
            //데이터 없으면 초기 값 "00:00:00" 입력
            subItem.setSubtime("00:00:00");}

        //subject 아이템 set
        StudysubviewHolder.setSubItem(subItem,hwAdapter,testAdapter);

        //과제 추가 버튼 클릭 시
        StudysubviewHolder.addhw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.homework_input);
                EditText homeworkNameInput=dialog.findViewById(R.id.homeworkNameInput);
                EditText homeworkDdayInput=dialog.findViewById(R.id.homeworkDdayInput);
                Button homeworkInputBtn_ok=dialog.findViewById(R.id.homeworkInputButton);
                homeworkInputBtn_ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String sub_id = subItem.getId();
                        homework hwItem=new homework(homeworkNameInput.getText().toString(),homeworkDdayInput.getText().toString());
                        hwAdapter.addhwItem(sub_id,hwItem);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        //시험 추가 버튼 클릭시
        StudysubviewHolder.addtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.test_input);
                EditText subtestNameInput=dialog.findViewById(R.id.subtestNameInput);
                EditText subtestDdayInput=dialog.findViewById(R.id.subtestDdayInput);
                Button testInputBtn_ok=dialog.findViewById(R.id.subtestInputButton);
                testInputBtn_ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String sub_id = subItem.getId();
                        SubTestItem testItem=new SubTestItem(subtestNameInput.getText().toString(),subtestDdayInput.getText().toString());
                        testAdapter.addtestItem(sub_id,testItem);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount(){
        if(subList == null){
            return 0;
        }
        return subList.size();
    }

    // 현재 어댑터에 새로운 아이템을 전달받아 추가하는 목적
    public void addSubItem(SubjectItem _item) {
        int addpos = subList.size();
        //데이터 베이스에 넣어주면서 id 넣어주기
        _item.setId(db.InsertSubject(_item.getSubname(),_item.getWeek(), _item.getWeekFre()));
        subList.add(_item);
        notifyItemInserted(addpos);
    }



    public class studysubViewHolder extends RecyclerView.ViewHolder{
        protected ImageView startbutton;
        protected TextView subjectname;
        protected TextView subjecttime;
        protected TextView addhw;
        protected RecyclerView hwrecycler;
        protected TextView addtest;
        protected RecyclerView testrecycler;

        public studysubViewHolder(View view){
            super(view);
            this.startbutton = (ImageView) view.findViewById(R.id.startbutton_sub);
            this.subjectname = (TextView)view.findViewById(R.id.subjectname);
            this.subjecttime = (TextView)view.findViewById(R.id.subjecttime);
            this.addhw = (TextView)view.findViewById(R.id.addhw);
            this.hwrecycler = (RecyclerView)view.findViewById(R.id.hwrecycler);
            this.addtest = (TextView)view.findViewById(R.id.addtest);
            this.testrecycler = (RecyclerView)view.findViewById(R.id.testrecycler);
//// startbutton 클릭 시 실행
            startbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cusPos = getAdapterPosition();  //현재 리스트 아이템 위치
                    SubjectItem item = subList.get(cusPos);

                    AppCompatActivity activity = (AppCompatActivity)itemView.getContext();
                    StopwatchFragment fragment = new StopwatchFragment(item.getId(),null);
                    activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_main_frame,fragment,"StopwatchFragment").commit();

                    notifyItemChanged(cusPos,item);
                }
            });

////과목 리사이클러뷰 선택시 실행
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int curPos=getAdapterPosition(); //현재 리스트 클릭한 아이템 위치
                    SubjectItem studysub=subList.get(curPos);

                    String[] strChoiceItems={"출석체크","과목 수정하기","과목 삭제하기"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position==0){
                                Log.v("Att111",studysub.getId());
                                AttendanceCheckFragment attendanceCheck = new AttendanceCheckFragment();
                                attendanceCheck.setSubid(studysub.getId());
                                ((MainActivity)view.getContext()).replaceFragment(attendanceCheck);    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                            }
                            else if(position==1){
                                //수정하기
                                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.activity_subject_input);
                                EditText subjectNameInput=dialog.findViewById(R.id.subjectNameInput);
                                EditText weekInput=dialog.findViewById(R.id.weekInput);
                                EditText weekFrequencyInput=dialog.findViewById(R.id.weekFrequencyInput);

                                subjectNameInput.setText(studysub.getSubname());
                                weekInput.setText(Integer.toString(studysub.getWeek()));
                                weekFrequencyInput.setText(Integer.toString(studysub.getWeekFre()));
                                Button subjectbtn_ok=dialog.findViewById(R.id.subjectInputButton);
                                subjectbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        String subname = subjectNameInput.getText().toString();
                                        Integer week=parseInt(weekInput.getText().toString());
                                        Integer weekFre=parseInt(weekFrequencyInput.getText().toString());
                                        String id = studysub.getId();
                                        db.Updatesubject(id,subname,week,weekFre);

                                        //update UI
                                        studysub.setSubname(subname);
                                        studysub.setWeek(week);
                                        studysub.setWeekFre(weekFre);
                                        notifyItemChanged(curPos,studysub);
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                            else if(position==2){
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                                builder2.setTitle("안내");
                                builder2.setMessage("과목에 해당된 과제와 시험이 삭제됩니다. \n 삭제하시겠습니까?");
                                builder2.setIcon(android.R.drawable.ic_dialog_alert);
                                builder2.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //delete table
                                        String id = studysub.getId();
                                        db.deleteSubject(id);
                                        //delete UI
                                        subList.remove(curPos);
                                        notifyItemRemoved(curPos);
                                    }
                                });

                                builder2.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog alertDialog = builder2.create();
                                alertDialog.show();
                            }
                        }
                    });
                    builder.show();
                }
            });

        }

        public void setSubItem(SubjectItem item, homeworkAdapter hwAdapter, SubTestAdapter testAdapter) {
            subjectname.setText(item.getSubname());
            subjecttime.setText(item.getSubtime());
            hwrecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            hwrecycler.setAdapter(hwAdapter);
            testrecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            testrecycler.setAdapter(testAdapter);
        }
    }
}
