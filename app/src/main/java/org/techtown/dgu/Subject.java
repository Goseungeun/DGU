package org.techtown.dgu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.dgu.R;

public class Subject extends Fragment {
    public static Subject newInstance() {
        return new Subject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subject, null); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        Button button1 = (Button)view.findViewById(R.id.button); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(InputSchoolSubject.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        return view;

         }
}
