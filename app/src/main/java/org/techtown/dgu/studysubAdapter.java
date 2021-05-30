package org.techtown.dgu;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class studysubAdapter extends RecyclerView.Adapter<studysubAdapter.studysubViewHolder>{

    private ArrayList<studysub> subList;
    private Context mContext;
    private Subject_DB mSubject_DB;

    public studysubAdapter(Context context, ArrayList<studysub> subList){
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
        final String subname = subList.get(i).getSubname();
        final String subtime = subList.get(i).getSubtime();

        ArrayList hwList = subList.get(i).getHwList();
        ArrayList testList = subList.get(i).getTestList();
        StudysubviewHolder.subjectname.setText(subname);
        StudysubviewHolder.subjecttime.setText(subtime);
        homeworkAdapter homeworkListDataAdapter = new homeworkAdapter(mContext, hwList);
        subtestAdapter subjecttestListDataAdapter = new subtestAdapter(mContext,testList);
        StudysubviewHolder.hwrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.hwrecycler.setAdapter(homeworkListDataAdapter);
        StudysubviewHolder.testrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.testrecycler.setAdapter(subjecttestListDataAdapter);

    }
    @Override
    public int getItemCount(){
        return subList.size();
    }

    public class studysubViewHolder extends RecyclerView.ViewHolder{
        protected ImageButton image;
        protected TextView subjectname;
        protected TextView subjecttime;
        protected TextView addhw;
        protected RecyclerView hwrecycler;
        protected TextView addtest;
        protected RecyclerView testrecycler;

        public studysubViewHolder(View view){
            super(view);
            this.image = (ImageButton)view.findViewById(R.id.imageButton);
            this.subjectname = (TextView)view.findViewById(R.id.subjectname);
            this.subjecttime = (TextView)view.findViewById(R.id.subjecttime);
            this.addhw = (TextView)view.findViewById(R.id.addhw);
            this.hwrecycler = (RecyclerView)view.findViewById(R.id.hwrecycler);
            this.addtest = (TextView)view.findViewById(R.id.addtest);
            this.testrecycler = (RecyclerView)view.findViewById(R.id.testrecycler);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int curPos=getAdapterPosition(); //현재 리스트 클릭한 아이템 위치
                    studysub studysub=subList.get(curPos);

                    String[] strChoiceItems={"수정하기","삭제하기"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position==0){
                                //수정하기
                                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.activity_subject_input);

                                EditText subjectNameInput=dialog.findViewById(R.id.subjectNameInput);
                                EditText weekInput=dialog.findViewById(R.id.weekInput);
                                EditText weekFrequencyInput=dialog.findViewById(R.id.weekFrequencyInput);

                                Button subjectbtn_ok=dialog.findViewById(R.id.subjectInputButton);
                                subjectbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        String subname=subjectNameInput.getText().toString();
                                        Integer week=parseInt(weekInput.getText().toString());
                                        Integer weekFre=parseInt(weekFrequencyInput.getText().toString());

                                        String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());
                                        mSubject_DB.UpdateTodo(subname,week,weekFre);

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
                            else if(position==1){
                                //delete table
                                String subname=studysub.getSubname();
                                mSubject_DB.DeleteTodo(subname);

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
    }

    // 현재 어댑터에 새로운 아이템을 전달받아 추가하는 목적
    public void addItem(studysub _item){
        subList.add(0,_item);
        notifyItemInserted(0);
    }
}