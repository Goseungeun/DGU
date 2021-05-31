package org.techtown.dgu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class homeworkAdapter extends RecyclerView.Adapter<homeworkAdapter.homeworkViewHolder> {

    private ArrayList<homework> homeworkList;
    private Context mContext;
    private homework_DB mHomework_DB;

    public homeworkAdapter(Context context,ArrayList<homework> hw){
        this.homeworkList = hw;
        this.mContext = context;
        mHomework_DB=new homework_DB(context);
    }

    public class homeworkViewHolder extends RecyclerView.ViewHolder{
        protected CheckBox homeworkname;
        protected TextView homeworkdday;

        public homeworkViewHolder(View view)
        {
            super(view);
            homeworkname = view.findViewById(R.id.homeworkname);
            homeworkdday = view.findViewById(R.id.homeworkdday);
///
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int curPos=getAdapterPosition(); //현재 리스트 클릭한 아이템 위치
                    homework homework=homeworkList.get(curPos);

                    String[] strChoiceItems={"과제 수정하기","과제 삭제하기"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position==0){
                                //수정하기
                                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.homework_input);

                                EditText homeworkNameInput=dialog.findViewById(R.id.homeworkNameInput);
                                EditText homeworkDdayInput=dialog.findViewById(R.id.homeworkDdayInput);


                                Button homeworkbtn_ok=dialog.findViewById(R.id.homeworkInputButton);
                                homeworkbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        String hwname=homeworkNameInput.getText().toString();
                                        String hwDday=homeworkDdayInput.getText().toString();

                                        String currentTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        mHomework_DB.UpdateTodo(hwname,hwDday);

                                        //update UI
                                        homework.setHwname(hwname);
                                        homework.setHwDday(hwDday);

                                        notifyItemChanged(curPos,homework);
                                        dialog.dismiss();
                                        Toast.makeText(mContext,"과제 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.show();
                            }
                            else if(position==1){
                                //delete table
                                String hwname=homework.getHwname();
                                mHomework_DB.DeleteTodo(hwname);

                                //delete UI
                                homeworkList.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext,"과제 삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();

                }
            });
            ///
        }
    }
    @NonNull
    @Override
    public homeworkViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hw_item,viewGroup,false);

        return new homeworkAdapter.homeworkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(homeworkViewHolder HomeworkviewHolder, int position) {
        HomeworkviewHolder.homeworkname.setText(homeworkList.get(position).getHwname());
        HomeworkviewHolder.homeworkdday.setText(homeworkList.get(position).getHwDday());

    }

    @Override
    public int getItemCount() {
        if(homeworkList!=null)
         return homeworkList.size();
        else return 0;
    }

}