package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

public class Getlicense extends Fragment {
    RecyclerView recyclerView;
    mylicenseAdapter adapter;


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

        adapter = new mylicenseAdapter();

        adapter.addItem(new mylicense("토익","2021","05","04","2023","05","04"));
        adapter.addItem(new mylicense("정보처리기사","2021","04","09","2025","04","09"));

        recyclerView.setAdapter(adapter);
    }
}
