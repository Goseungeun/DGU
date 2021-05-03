package org.techtown.dgu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AttendanceCheck extends Fragment {

    private  ViewGroup view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.attendancecheck, container,false);

        //BackButton1을 누르면 실행되는 함수
        ImageButton backbutton = (ImageButton)view.findViewById(R.id.BackButton1); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(new Subject());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        return view;
    }
}
