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

        return rootView;
    }

    private void loadRecentDB(){
        licensItems = mDBHelper.getlicenselist();

        mAdapter = new LicenseAdapter(licensItems,getContext());
        rv_license.setHasFixedSize(true);
        rv_license.setAdapter(mAdapter);
    }
}
