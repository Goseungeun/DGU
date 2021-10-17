package org.techtown.dgu.studylicense;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;

import java.util.ArrayList;

public class LicenseFragment extends Fragment {

    private RecyclerView rv_license;
    private LicenseAdapter mAdapter;
    private DGUDB mDBHelper;
    private ArrayList<LicenseItem> licensItems;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.license,container,false);
        licensItems=new ArrayList<LicenseItem>();
        mDBHelper= new DGUDB(this.getContext());
        rv_license= (RecyclerView)rootView.findViewById(R.id.license_recycler);
        mAdapter = new LicenseAdapter(licensItems,this.getContext());
        rv_license.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        rv_license.setAdapter(mAdapter);

        //하루가 지나면 초기화되는거
 //       handler.postDelayed(runnable, 0);

        Button button1 = (Button)rootView.findViewById(R.id.button2); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        loadRecentDB();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(LicenseFragment.this.getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.activity_license_input);
                EditText et_name = dialog.findViewById(R.id.licenseNameInput);
                EditText et_licensedday = dialog.findViewById(R.id.editTextDate2);
                Button licensebtn_ok = dialog.findViewById(R.id.licensebtn_ok);

                licensebtn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Insert UI
                        LicenseItem item = new LicenseItem(et_name.getText().toString(),et_licensedday.getText().toString());
                        mAdapter.addItem(item);
                        loadRecentDB();
                        rv_license.smoothScrollToPosition(0);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });


       // Ddaycal();      //DDay 계산 함수
        return rootView;
    }

    private void loadRecentDB(){
        licensItems = mDBHelper.getlicenselist();
        mAdapter = new LicenseAdapter(licensItems,getContext());
        rv_license.setAdapter(mAdapter);
    }

    //TODO : 하루 초기화 용으로 stopwatch에서 썼던거
/*    public final Runnable runnable = new Runnable() {
        public void run() {
            if(stopwatch.ChangeDate()){
                Log.v("TTT4","run");
                mDBHelper.Reset();
                mAdapter.resetItem();
                stopwatch.InsertTotalStudyTime();
                loadRecentDB();
            }
            handler.postDelayed(this, 0);
        }
    };*/

    /*
    private void Ddaycal(){
        licensItems = mDBHelper.getlicenselist();       //DB의 모든 자격증 불러오기
        int testyear;       //시험날짜 (년)
        int testmonth;      //시험날짜 (월)
        int testdate;       //시험날짜 (일)

        //Array List의 크기만큼 for문 돌림
        for (int i = 0; i <licensItems.size();i++){
            study_license item = licensItems.get(i);        //i번째 아이템 가져오기
            String day = item.getTestday();     //사용자가 입력한 시험날짜 가져오기
            String[] testdayarray = day.split("/");     //구분기호 /를 기준으로 문자열을 나눠 줌

            testyear = Integer.parseInt(testdayarray[0]);
            testmonth = Integer.parseInt(testdayarray[1]);
            testdate = Integer.parseInt(testdayarray[2]);

            //오늘 날짜 받기
            Calendar today = Calendar.getInstance();
            //시험 날짜
            Calendar test = Calendar.getInstance();
            test.set(testyear,testmonth,testdate);


            //Dday 계산
            long long_test = test.getTimeInMillis()/(24*60*60*1000);
            long long_today = today.getTimeInMillis()/(24*60*60*1000);

            long dday = long_test - long_today;

        }

    }
    */

}
