package org.techtown.dgu.homework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;
import org.techtown.dgu.subject.SubjectItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class homeworkAdapter extends RecyclerView.Adapter<homeworkAdapter.homeworkViewHolder> {

    private ArrayList<homework> homeworkList;
    private Context mContext;
    private DGUDB db;

    public homeworkAdapter(Context context, ArrayList<homework> homeworkList){
        this.homeworkList = homeworkList;
        this.mContext = context;
        this.db = new DGUDB(mContext);
    }

    public String ddayCacultation(String testday) throws ParseException {

        // Millisecond 형태의 하루(24 시간)
        final int ONE_DAY = 24 * 60 * 60 * 1000;

        int year= Integer.parseInt(testday.substring(0,4));
        int month= Integer.parseInt(testday.substring(4,6));
        int day= Integer.parseInt(testday.substring(6,8));

        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(year, month-1, day);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "D-Day";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        String strCount = (String.format(strFormat, result));
        return strCount;
    }

    public class homeworkViewHolder extends RecyclerView.ViewHolder{
        protected CheckBox homeworkname;
        protected TextView homeworkdday;

        public homeworkViewHolder(View view)
        {
            super(view);
            homeworkname = view.findViewById(R.id.homeworkname);
            homeworkdday = view.findViewById(R.id.homeworkdday);
            homeworkdday.setOnClickListener(new View.OnClickListener(){
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
                                homeworkNameInput.setText(homework.getHwname());
                                homeworkDdayInput.setText(homework.getHwDday());
                                Button homeworkbtn_ok=dialog.findViewById(R.id.homeworkInputButton);
                                homeworkbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        String hwname=homeworkNameInput.getText().toString();
                                        String hwDday=homeworkDdayInput.getText().toString();
                                        String id = homework.getId();
                                        db.UpdateHw(id,hwname,hwDday);
                                        //update UI
                                        homework.setHwname(hwname);
                                        homework.setHwDday(hwDday);
                                        notifyItemChanged(curPos,homework);
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                            else if(position==1){
                                //delete table
                                String id = homework.getId();
                                db.deleteHW(id);
                                //delete UI
                                homeworkList.remove(curPos);
                                notifyItemRemoved(curPos);
                            }
                        }
                    });
                    builder.show();

                }
            });
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
        homework item = homeworkList.get(position);


        try {
            item.setViewdday(ddayCacultation(item.getHwDday()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HomeworkviewHolder.homeworkname.setText(homeworkList.get(position).getHwname());
        HomeworkviewHolder.homeworkdday.setText(homeworkList.get(position).getViewdday());
    }

    @Override
    public int getItemCount() {
        if(homeworkList!=null)
         return homeworkList.size();
        else return 0;
    }
    // 현재 어댑터에 새로운 아이템을 전달받아 추가하는 목적
    public void addhwItem(String subid,homework _item){
        int addpos = homeworkList.size();
        _item.setId(db.insertHw(subid,_item.getHwname(),_item.getHwDday()));
        homeworkList.add(_item);
        notifyItemInserted(addpos);
    }
}
