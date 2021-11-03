package org.techtown.dgu.studylicense;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;

import java.util.ArrayList;
import java.util.Calendar;

public class LicenseFragment extends Fragment {

    private RecyclerView rv_license;
    private LicenseAdapter mAdapter;
    private DGUDB mDBHelper;
    private ArrayList<LicenseItem> licensItems;
    String licenseDDAY="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.license,container,false);
        licensItems=new ArrayList<LicenseItem>();
        mDBHelper= new DGUDB(this.getContext());
        rv_license= (RecyclerView)rootView.findViewById(R.id.license_recycler);
        mAdapter = new LicenseAdapter(licensItems,this.getContext());
        rv_license.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        rv_license.setAdapter(mAdapter);


        Button button1 = (Button)rootView.findViewById(R.id.button2); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        loadRecentDB();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(LicenseFragment.this.getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.activity_license_input);
                EditText et_name = dialog.findViewById(R.id.licenseNameInput);
                TextView et_licensedday = dialog.findViewById(R.id.editTextDate2);
                Button licensebtn_ok = dialog.findViewById(R.id.licensebtn_ok);

                et_licensedday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog pickerDialog = new DatePickerDialog(getContext(),  new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_licensedday.setText(""+year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일");
                                licenseDDAY = ""+year+String.format("%02d", monthOfYear+1)+String.format("%02d", dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH)));

                        pickerDialog.show();
                    }
                });

                licensebtn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //자격증명 또는 시험일자가 비어있으면 저장되지 않게
                        String LicenseNameInputString=et_name.getText().toString();
                        if(!(LicenseNameInputString.equals("")||licenseDDAY.equals(""))) {
                            //Insert UI
                            LicenseItem item = new LicenseItem(LicenseNameInputString, licenseDDAY);
                            mAdapter.addItem(item);
                            loadRecentDB();
                            rv_license.smoothScrollToPosition(0);
                            licenseDDAY = "";
                            dialog.dismiss();
                        }else{
                            Toast.makeText(v.getContext(),"정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }

        });

        return rootView;
    }

    private void loadRecentDB(){
        licensItems = mDBHelper.getlicenselist();
        mAdapter = new LicenseAdapter(licensItems,getContext());
        rv_license.setAdapter(mAdapter);
    }
}
