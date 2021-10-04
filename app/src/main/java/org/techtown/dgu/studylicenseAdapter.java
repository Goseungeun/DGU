package org.techtown.dgu;

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

import java.util.ArrayList;

public class studylicenseAdapter extends RecyclerView.Adapter<studylicenseAdapter.ViewHolder>{
    public Object setItem;
    private ArrayList<study_license> items;
    private Context mContext;
    private STLicenseDBHelper mDBHelper;
    private Stopwatch stopwatch;

    public studylicenseAdapter(ArrayList<study_license> items, Context mContext){
        this.items = items;
        this.mContext = mContext;
        mDBHelper = new STLicenseDBHelper(mContext);
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
        study_license item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(study_license item){
        items.add(item);
        notifyItemInserted(0);
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
                    study_license licenseItem = items.get(cusPos);

                    stopwatch.button_click_license(licenseItem.getName(),licenseItem.getStudytime(),startbutton,studytime);

                    licenseItem.setStudytime((String) studytime.getText());
                    //DB에 업데이트 해줌
                    mDBHelper.UpdateLicenseStudyTime(licenseItem.getName(),(String) studytime.getText());
                    notifyItemChanged(cusPos,licenseItem);
                }
            });

            touch_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cusPos = getAdapterPosition();  //현재 리스트 아이템 위치
                    study_license licenseItem = items.get(cusPos);

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
                                et_studyrate.setText(licenseItem.getStudyrate().toString());

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

        public void setItem(study_license item){
            name.setText(item.getName());
            studytime.setText(item.getStudytime());
        }
    }

}
