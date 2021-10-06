package org.techtown.dgu.mylicense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.R;

public class MyLicenseFragment extends Fragment {
    RecyclerView recyclerView;
    MyLicenseAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.my_license,container,false);

        initUI(rootView);

        return rootView;
    }

    private void initUI(ViewGroup rootView){

        recyclerView = rootView.findViewById(R.id.getlicenserecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyLicenseAdapter();

        adapter.addItem(new MyLicenseItem("토익","2021","05","04","2023","05","04"));
        adapter.addItem(new MyLicenseItem("정보처리기사","2021","04","09","2025","04","09"));

        recyclerView.setAdapter(adapter);
    }
}
