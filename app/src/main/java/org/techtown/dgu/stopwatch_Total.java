package org.techtown.dgu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class stopwatch_Total extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.total_stopwatch, container, false); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        
        //subject 관련 시간 더하기
        
        //license 관련 시간 더하기
        
        return view;
    }

}
