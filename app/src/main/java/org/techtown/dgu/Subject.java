package org.techtown.dgu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.techtown.dgu.R;

public class Subject extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subject, container, false); // Fragment로 불러올 xml파일을 view로 가져옵니다.

        Button button1 = (Button) view.findViewById(R.id.button); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
            private void openDialog() {
                DialogFragment InputSchoolSubject = new InputSchoolSubject();
               // InputSchoolSubject.setTargetFragment(Subject.this, 0);
                InputSchoolSubject.show(getFragmentManager(), "Search Filter");
            }
        });

        //  return view;


        return view;
    }
}