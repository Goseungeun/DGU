package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Subject extends Fragment {
    ArrayList<studysub> subDataList;
    Context ctx;

    public static Subject newInstance() {
        return new Subject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.subject, container,false); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        Button button1 = (Button) view.findViewById(R.id.button); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        ctx=getContext();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity) getActivity()).replaceFragment(InputSchoolSubject.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        subDataList = new ArrayList<studysub>();

        //과제, 시험 데이터 입력
        studysub subDataModel = new studysub();
        subDataModel.setSubname("캡스톤디자인");
        subDataModel.setSubtime("3:00:03");
        ArrayList<homework> hwList = new ArrayList<homework>();
        ArrayList<subtest> stList = new ArrayList<subtest>();
        hwList.add(new homework("과제 1","D-2"));     //과제 데이터
        stList.add(new subtest("시험1","D-14"));  //시험 데이터

        subDataModel.setHwList(hwList);         //과목에 과제와 시험 넣기
        subDataModel.setTestList(stList);

        subDataList.add(subDataModel);      //과목들 추가
      /*
                openDialog();
            }
            private void openDialog() {
                DialogFragment InputSchoolSubject = new InputSchoolSubject();
               // InputSchoolSubject.setTargetFragment(Subject.this, 0);
                InputSchoolSubject.show(getFragmentManager(), "Subject Input");
            }
        });

        //  return view;
        
        */

        studysub subDataModel1 = new studysub();
        subDataModel1.setSubname("캡스톤디자인");
        subDataModel1.setSubtime("3:00:03");
        ArrayList<homework> hwList1 = new ArrayList<homework>();
        ArrayList<subtest> stList1 = new ArrayList<subtest>();
        hwList1.add(new homework("과제 1","D-2"));     //과제 데이터
        stList1.add(new subtest("시험1","D-14"));  //시험 데이터

        subDataModel1.setHwList(hwList);         //과목에 과제와 시험 넣기
        subDataModel1.setTestList(stList);

        subDataList.add(subDataModel1);

        //리싸이클러뷰 설정
        RecyclerView subrecyclerview = (RecyclerView)view.findViewById(R.id.subrecycler);
        studysubAdapter adapter = new studysubAdapter(getContext(),subDataList);
        subrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        subrecyclerview.setAdapter(adapter);

        return view;

    }
}

