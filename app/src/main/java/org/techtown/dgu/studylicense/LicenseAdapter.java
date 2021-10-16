package org.techtown.dgu.studylicense;

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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;
import org.techtown.dgu.StopwatchFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder>{

    private ArrayList<LicenseItem> items;
    private Context mContext;
    private DGUDB mDBHelper;

    public LicenseAdapter(ArrayList<LicenseItem> items, Context mContext){
        this.items = items;
        this.mContext = mContext;
        mDBHelper = new DGUDB(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.license_item,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        LicenseItem item = items.get(position);

        if(mDBHelper.SearchStudytimeID(null,item.getLicenseid())!=0 && item.getLicenseid()!=null){
            item.setLicensestudytime(mDBHelper.getStudytime(mDBHelper.SearchStudytimeID(null,item.getLicenseid())));
        }else{item.setLicensestudytime("00:00:00");}

        try {
            item.setViewdday(ddayCacultation(item.getLicensedday()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.v("Licenseitem",""+item.getLicensestudytime());
        viewHolder.setItem(item);
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


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(LicenseItem item){
        //DB에 insert해주면서 item에 id를 settting해준다.
        item.setLicenseid(mDBHelper.InsertLicense(item.getLicensename(),item.getLicensedday()));
        items.add(item);
        notifyItemInserted(0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView studytime;
        private TextView dday;
        private ImageView startbutton;
        private LinearLayout touch_area;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.licensename);
            studytime = itemView.findViewById(R.id.licensetime);
            startbutton = itemView.findViewById(R.id.startbutton_lic);
            dday = itemView.findViewById(R.id.dday);
            touch_area=itemView.findViewById(R.id.touch_area_lic);
            studytime=itemView.findViewById(R.id.licensetime);

            startbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int cusPos = getAdapterPosition();  //현재 리스트 아이템 위치
                    LicenseItem item = items.get(cusPos);
                    //TODO ? 아래줄이 왜 있는지 모르겠음
                    //items.set(cusPos,stopwatch.button_click(item, startbutton, studytime));

                    AppCompatActivity activity = (AppCompatActivity)itemView.getContext();
                    StopwatchFragment fragment = new StopwatchFragment(null,item.getLicenseid());
                    activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_main_frame,fragment).commit();

                    notifyItemChanged(cusPos,item);
                }
            });

            touch_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cusPos = getAdapterPosition();  //현재 리스트 아이템 위치
                    LicenseItem licenseItem = items.get(cusPos);

                    String[] strChoiceItems = {"수정하기","삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택하세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position == 0){
                                //수정하기
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.activity_license_input);
                                EditText et_name = dialog.findViewById(R.id.licenseNameInput);
                                EditText et_dday = dialog.findViewById(R.id.editTextDate2);
                                Button licensebtn_ok = dialog.findViewById(R.id.licensebtn_ok);

                                et_name.setText(licenseItem.getLicensename());
                                et_dday.setText(licenseItem.getLicensedday());

                                et_name.setSelection(et_name.getText().length());

                                licensebtn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //UPDATE table
                                        String name = et_name.getText().toString();
                                        String licensedday = et_dday.getText().toString();
                                        String id = licenseItem.getLicenseid();
                                        mDBHelper.UpdateLicense(id,name,licensedday);

                                        //update UI
                                        licenseItem.setLicensename(name);
                                        licenseItem.setLicensedday(licensedday);
                                        notifyItemChanged(cusPos,licenseItem);
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }

                            else if (position == 1) {
                                //delete table
                                String id = licenseItem.getLicenseid();
                                mDBHelper.deleteLicense(id);
                                //delete UI
                                items.remove(cusPos);
                                notifyItemRemoved(cusPos);
                            }
                        }
                    });
                    builder.show();
                }
            });
        }

        public void setItem(LicenseItem item){
            name.setText(item.getLicensename());
            studytime.setText(item.getLicensestudytime());
            dday.setText(item.getViewdday());
        }



    }


}


