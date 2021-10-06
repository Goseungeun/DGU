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
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.R;
import org.techtown.dgu.Stopwatch;

import java.util.ArrayList;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder>{

    private ArrayList<LicenseItem> items;
    private Context mContext;
    private LicenseDB mDBHelper;
    private Stopwatch stopwatch;

    public LicenseAdapter(ArrayList<LicenseItem> items, Context mContext){
        this.items = items;
        this.mContext = mContext;
        mDBHelper = new LicenseDB(mContext);
        stopwatch = new Stopwatch(mContext);
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
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(LicenseItem item){
        items.add(item);
        notifyItemInserted(0);
    }

    public void resetItem(){
        for(int i=0;i<items.size();i++){
            items.get(i).setStudytime("00:00:00");
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView studytime;
        //public TextView dday;
        private ImageView startbutton;
        private SeekBar progress;
        private LinearLayout touch_area;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.licensename);
            studytime = itemView.findViewById(R.id.licensetime);
            startbutton = itemView.findViewById(R.id.startbutton_lic);
            progress = itemView.findViewById(R.id.progress);
            //dday = itemView.findViewById(R.id.dday);
            touch_area=itemView.findViewById(R.id.touch_area_lic);

            startbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cusPos = getAdapterPosition();  //현재 리스트 아이템 위치
                    LicenseItem item = items.get(cusPos);
                    items.set(cusPos,stopwatch.button_click_license(item, startbutton, studytime));
                    Log.v("OutsideStopwatch","Name : "+items.get(cusPos).getName()+" Studytime :"+items.get(cusPos).getStudytime());
                    //DB에 업데이트 해줌
                    mDBHelper.UpdateLicenseStudyTime(item.getName(),item.getStudytime());
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
                                EditText et_testday = dialog.findViewById(R.id.editTextDate2);
                                EditText et_studyrate = dialog.findViewById(R.id.progressRateInput);
                                Button licensebtn_ok = dialog.findViewById(R.id.licensebtn_ok);

                                et_name.setText(licenseItem.getName());
                                et_testday.setText(licenseItem.getTestday());
                                et_studyrate.setText(Double.toString(licenseItem.getStudyrate()));

                                et_name.setSelection(et_name.getText().length());

                                licensebtn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //UPDATE table
                                        String name = et_name.getText().toString();
                                        String testday = et_testday.getText().toString();
                                        Double studyrate = Double.parseDouble(et_studyrate.getText().toString());       //진도율 스트링으로 받아와 실수형으로 변환
                                        String beforename = licenseItem.getName();
                                        mDBHelper.UpdateLicense(name,testday,studyrate,beforename);

                                        //update UI
                                        licenseItem.setName(name);
                                        licenseItem.setTestday(testday);
                                        licenseItem.setStudyrate(studyrate);
                                        notifyItemChanged(cusPos,licenseItem);
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }

                            else if (position == 1) {
                                //delete table
                                String beforename = licenseItem.getName();
                                mDBHelper.deleteLicense(beforename);
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
            name.setText(item.getName());
            studytime.setText(item.getStudytime());
        }



    }


}


