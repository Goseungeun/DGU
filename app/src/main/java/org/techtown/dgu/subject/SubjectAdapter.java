package org.techtown.dgu.subject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.MainActivity;
import org.techtown.dgu.R;
import org.techtown.dgu.homework.homework;
import org.techtown.dgu.homework.homeworkAdapter;
import org.techtown.dgu.homework.homework_DB;
import org.techtown.dgu.test.SubTestItem;
import org.techtown.dgu.test.SubTestAdapter;
import org.techtown.dgu.test.SubTest_DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.studysubViewHolder>{

    private ArrayList<SubjectItem> subList;
    private Context mContext;

    private Subject_DB mSubject_DB;

    protected RecyclerView hwrecycler;
    private homework_DB mHomework_DB;
    ArrayList<homework> homeworkList;
    private homeworkAdapter mHomeworkAdapter;


    protected RecyclerView testrecycler;
    private SubTest_DB mSubTest_DB;
    ArrayList<SubTestItem> subTestList;
    private SubTestAdapter msubtestAdapter;


    
    public SubjectAdapter(Context context, ArrayList<SubjectItem> subList){
        this.subList = subList;
        this.mContext = context;
        mSubject_DB=new Subject_DB(context);
    }

    @Override
    public studysubViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_item,viewGroup,false);
        studysubViewHolder sh = new studysubViewHolder(v);
        return sh;
    }

    @Override
    public void onBindViewHolder(studysubViewHolder StudysubviewHolder, int i){
        final int id=subList.get(i).getId();
        final String subname = subList.get(i).getSubname();

        //과목 시간설정하는 방법 알 수 있는곳
        final String subtime = subList.get(i).getSubtime();

        ArrayList homeworkList = subList.get(i).getHwList();
        ArrayList subTestList = subList.get(i).getTestList();
        StudysubviewHolder.subjectname.setText(subname);
        StudysubviewHolder.subjecttime.setText(subtime);

       // homeworkAdapter homeworkListDataAdapter = new homeworkAdapter(mContext, homeworkList);
      //  subtestAdapter subjecttestListDataAdapter = new subtestAdapter(mContext,subTestList);

        StudysubviewHolder.hwrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.hwrecycler.setAdapter(mHomeworkAdapter);
        StudysubviewHolder.testrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.testrecycler.setAdapter(msubtestAdapter);

    }

    @Override
    public int getItemCount(){
        return subList.size();
    }

    // 현재 어댑터에 새로운 아이템을 전달받아 추가하는 목적
    public void addSubItem(SubjectItem _item) {
            subList.add(0,_item);
            notifyItemInserted(0);
        }



    public class studysubViewHolder extends RecyclerView.ViewHolder{
        protected ImageView image;
        protected TextView subjectname;
        protected TextView subjecttime;
        protected TextView addhw;
        protected RecyclerView hwrecycler;
        protected TextView addtest;
        protected RecyclerView testrecycler;

        public studysubViewHolder(View view){
            super(view);
            this.image = (ImageView) view.findViewById(R.id.startbutton_sub);
            this.subjectname = (TextView)view.findViewById(R.id.subjectname);
            this.subjecttime = (TextView)view.findViewById(R.id.subjecttime);

            this.addhw = (TextView)view.findViewById(R.id.addhw);
            this.hwrecycler = (RecyclerView)view.findViewById(R.id.hwrecycler);
            this.addtest = (TextView)view.findViewById(R.id.addtest);
            this.testrecycler = (RecyclerView)view.findViewById(R.id.testrecycler);


            mHomework_DB=new homework_DB((this.hwrecycler.getContext()));
            homeworkAdapter homeworkListDataAdapter = new homeworkAdapter(mContext, homeworkList);
            hwrecycler.setLayoutManager(new LinearLayoutManager(this.hwrecycler.getContext(),LinearLayoutManager.VERTICAL,false));
            hwrecycler.setAdapter(homeworkListDataAdapter);
            homeworkList=new ArrayList<>();


            mSubTest_DB=new SubTest_DB((this.testrecycler.getContext()));
            SubTestAdapter subjecttestListDataAdapter = new SubTestAdapter(mContext,subTestList);
            testrecycler.setLayoutManager(new LinearLayoutManager(this.testrecycler.getContext(),LinearLayoutManager.VERTICAL,false));
            testrecycler.setAdapter(subjecttestListDataAdapter);
            subTestList=new ArrayList<>();



            //load recent DB
            loadhwRecentDB();
            loadtestRecentDB();


////과제추가 다이얼로그
            addhw.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v){
                        //팝업창 띄우기
                        Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                        dialog.setContentView(R.layout.homework_input);

                        EditText homeworkNameInput=dialog.findViewById(R.id.homeworkNameInput);
                        EditText homeworkDdayInput=dialog.findViewById(R.id.homeworkDdayInput);
                       

                        Button homeworkInputBtn_ok=dialog.findViewById(R.id.homeworkInputButton);
                        homeworkInputBtn_ok.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){

                                //Insert Database
                                String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());


                                mHomework_DB.InsetHomework(homeworkNameInput.getText().toString(),homeworkDdayInput.getText().toString());
                                
                                //Insert UI
                                homework hwitem=new homework();
                                hwitem.setHwname(homeworkNameInput.getText().toString());
                                hwitem.setHwDday(homeworkDdayInput.getText().toString());

                                mHomeworkAdapter.addhwItem(hwitem);
                                hwrecycler.setAdapter(mHomeworkAdapter);
                                hwrecycler.smoothScrollToPosition(0);


                                dialog.dismiss();
                                Toast.makeText(mContext,"과목이 추가 되었습니다.",Toast.LENGTH_SHORT).show();


                            }
                        });

                        dialog.show();
                    
                    
                }
            });

////시험추가 다이얼로그
            ///
            addtest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    //팝업창 띄우기
                    Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                    dialog.setContentView(R.layout.test_input);

                    EditText subtestNameInput=dialog.findViewById(R.id.subtestNameInput);
                    EditText subtestDdayInput=dialog.findViewById(R.id.subtestDdayInput);


                    Button testInputBtn_ok=dialog.findViewById(R.id.subtestInputButton);
                    testInputBtn_ok.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){

                            //Insert Database
                            String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());


                            mSubTest_DB.InsetSubtest(subtestNameInput.getText().toString(),subtestDdayInput.getText().toString());

                            //Insert UI
                            SubTestItem testitem=new SubTestItem();
                            testitem.setSubtestname(subtestNameInput.getText().toString());
                            testitem.setTestDday(subtestDdayInput.getText().toString());

                            msubtestAdapter.addtestItem(testitem);
                            testrecycler.setAdapter(msubtestAdapter);
                            testrecycler.smoothScrollToPosition(0);

                            dialog.dismiss();
                            Toast.makeText(mContext,"시험이 추가 되었습니다.",Toast.LENGTH_SHORT).show();


                        }
                    });

                    dialog.show();


                }
            });
            ///

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
                                AttendanceCheckFragment attendanceCheck = new AttendanceCheckFragment();

                                String name = studysub.getSubname();
                                attendanceCheck.setSubName(name);
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
                                        String subname=subjectNameInput.getText().toString();
                                        Integer week=parseInt(weekInput.getText().toString());
                                        Integer weekFre=parseInt(weekFrequencyInput.getText().toString());


                                        String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());
                                        int id = studysub.getId();
                                        mSubject_DB.UpdateTodo(id,subname,week,weekFre);

                                        //update UI
                                        studysub.setSubname(subname);
                                        studysub.setWeek(week);
                                        studysub.setWeekFre(weekFre);
                                        notifyItemChanged(curPos,studysub);
                                        dialog.dismiss();
                                        Toast.makeText(mContext,"과목 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.show();
                            }
                            else if(position==2){
                                //delete table
                                String subname=studysub.getSubname();
                                int id = studysub.getId();
                                mSubject_DB.DeleteTodo(id);

                                //delete UI
                                subList.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext,"과목 삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();

                }
            });


        }

        private void loadhwRecentDB() {
            //저장되어 있던 DB를 가져온다
            homeworkList=mHomework_DB.getHomeworkList();
            if(mHomeworkAdapter==null){
                mHomeworkAdapter=new homeworkAdapter(mContext,homeworkList);
                hwrecycler.setHasFixedSize(true);
                hwrecycler.setAdapter(mHomeworkAdapter);
            }
        }

        private void loadtestRecentDB() {
            //저장되어 있던 DB를 가져온다
            subTestList=mSubTest_DB.getSubTestList();
            if(msubtestAdapter==null){
                msubtestAdapter=new SubTestAdapter(mContext,subTestList);
                testrecycler.setHasFixedSize(true);
                testrecycler.setAdapter(msubtestAdapter);
            }
        }

    }
}
