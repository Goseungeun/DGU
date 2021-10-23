package org.techtown.dgu.test;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;
import org.techtown.dgu.studylicense.LicenseItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubTestAdapter extends RecyclerView.Adapter<SubTestAdapter.subtestViewHolder>{

    private ArrayList<SubTestItem> subTestList;
    private Context mContext;
    private DGUDB db;
    String testdday = "";

    public SubTestAdapter(Context context, ArrayList<SubTestItem> st) {
        this.mContext = context;
        this.subTestList = st;
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

    public class subtestViewHolder extends RecyclerView.ViewHolder{
        protected TextView subtestname;
        protected TextView subtestdday;

        public subtestViewHolder(View view){
            super(view);
            subtestname = view.findViewById(R.id.subtestname);
            subtestdday = view.findViewById(R.id.subtestdday);
            ///
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int curPos=getAdapterPosition(); //현재 리스트 클릭한 아이템 위치
                    SubTestItem subtest=subTestList.get(curPos);

                    String[] strChoiceItems={"시험 수정하기","시험 삭제하기"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position==0){
                                //수정하기
                                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.test_input);
                                EditText subtestNameInput=dialog.findViewById(R.id.subtestNameInput);
                                TextView subtestDdayInput=dialog.findViewById(R.id.subtestDdayInput);
                                subtestNameInput.setText(subtest.getSubtestname());

                                String getTestdday=subtest.getTestDday();
                                String setTextTestdday = getTestdday.substring(0,4)+"년 "
                                        +getTestdday.substring(4,6)+"월 "
                                        +getTestdday.substring(6,8)+"일";

                                subtestDdayInput.setText(setTextTestdday);
                                Button subtestbtn_ok=dialog.findViewById(R.id.subtestInputButton);


                                subtestDdayInput.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Calendar calendar = Calendar.getInstance();
                                        DatePickerDialog pickerDialog = new DatePickerDialog(mContext,  new DatePickerDialog.OnDateSetListener() {
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                subtestDdayInput.setText(""+year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일");
                                                testdday = ""+year+String.format("%02d", monthOfYear+1)+String.format("%02d", dayOfMonth);
                                            }
                                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH)));

                                        pickerDialog.show();
                                    }
                                });

                                subtestbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        //시험명 또는 시험일자가 비어있으면 저장되지 않게
                                        String subtestNameInputString=subtestNameInput.getText().toString();
                                        if(!(subtestNameInputString.equals("")||testdday.equals(""))) {
                                            String id = subtest.getId();
                                            db.UpdateTest(id, subtestNameInputString, testdday);
                                            //update UI
                                            subtest.setSubtestname(subtestNameInputString);
                                            subtest.setTestDday(testdday);
                                            notifyItemChanged(curPos, subtest);
                                            dialog.dismiss();
                                        }else{
                                            Toast.makeText(v.getContext(),"정보를 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.show();
                            }
                            else if(position==1){
                                //delete table
                                String id = subtest.getId();
                                db.deleteTest(id);
                                //delete UI
                                subTestList.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext,"시험 삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
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
    public subtestViewHolder onCreateViewHolder(ViewGroup viewGroup,int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_item,viewGroup, false);

        return new SubTestAdapter.subtestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(subtestViewHolder SubtestviewHolder,int position)
    {
        SubTestItem item = subTestList.get(position);

        try {
            item.setViewdday(ddayCacultation(item.getTestDday()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SubtestviewHolder.subtestname.setText(subTestList.get(position).getSubtestname());
        SubtestviewHolder.subtestdday.setText(subTestList.get(position).getViewdday());


    }

    @Override
    public int getItemCount() {
        if(subTestList!=null)
        return subTestList.size();
        else return 0;
    }

    // 현재 어댑터에 새로운 아이템을 전달받아 추가하는 목적
    public void addtestItem(String subid,SubTestItem _item){
        int addpos = subTestList.size();
        _item.setId(db.insertTest(subid,_item.getSubtestname(),_item.getTestDday()));
        subTestList.add(_item);
        notifyItemInserted(addpos);
    }
}
