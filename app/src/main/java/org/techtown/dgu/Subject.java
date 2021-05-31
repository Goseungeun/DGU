/*
package org.techtown.dgu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Subject extends Fragment {

    Context ctx;

    private RecyclerView subrecyclerview;
    private Subject_DB mSubject_DB;
    ArrayList<studysub> subDataList;
    private studysubAdapter mAdapter;


    public static Subject newInstance() {
        return new Subject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.subject, container,false); // Fragment로 불러올 xml파일을 view로 가져옵니다.

        Button subjectInput = (Button) view.findViewById(R.id.subjectInputButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        subjectInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
            private void openDialog() {
                DialogFragment InputSchoolSubject = new InputSchoolSubject();
                // InputSchoolSubject.setTargetFragment(Subject.this, 0);
                InputSchoolSubject.show(getFragmentManager(), "Subject Input");
            }
        });



        //load recent DB
        //loadRecentDB();


        Button button1 = (Button) view.findViewById(R.id.subjectInputButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment InputSchoolSubject = new InputSchoolSubject();
                InputSchoolSubject.show(getFragmentManager(), "Subject Input");   }

        });
        ctx=getContext();


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
      */
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

        *//*


        studysub subDataModel1 = new studysub();
        subDataModel1.setSubname("캡스톤디자인");
        subDataModel1.setSubtime("3:00:03");

        ArrayList<homework> hwList1 = new ArrayList<homework>();
        ArrayList<subtest> stList1 = new ArrayList<subtest>();
        hwList1.add(new homework("과제 1","D-2"));     //과제 데이터
        hwList1.add(new homework("과제 2","D-3"));
        stList1.add(new subtest("시험1","D-14"));  //시험 데이터

        subDataModel1.setHwList(hwList1);         //과목에 과제와 시험 넣기
        subDataModel1.setTestList(stList1);

        subDataList.add(subDataModel1);



        mSubject_DB= new Subject_DB(this.getContext());
        subrecyclerview = (RecyclerView)view.findViewById(R.id.subrecycler);
        mAdapter = new studysubAdapter(getContext(),subDataList);
        subrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        subrecyclerview.setAdapter(mAdapter);


      */
/*  //리싸이클러뷰 설정
        RecyclerView subrecyclerview = (RecyclerView)view.findViewById(R.id.subrecycler);
        studysubAdapter adapter = new studysubAdapter(getContext(),subDataList);
        subrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        subrecyclerview.setAdapter(adapter);*//*


        return view;

    }

    private void loadRecentDB() {


        //저장되어 있던 DB를 가져온다
        subDataList=mSubject_DB.getSubList();
        if(mAdapter==null){
            mAdapter=new studysubAdapter(this.getContext(),subDataList);
            subrecyclerview.setHasFixedSize(true);
            subrecyclerview.setAdapter(mAdapter);
        }
    }
}

*/

package org.techtown.dgu;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class Subject extends Fragment {

    private RecyclerView subrecyclerview;
    private Subject_DB mSubject_DB;
    ArrayList<studysub> subDataList;
    private studysubAdapter mAdapter;

    Context ctx;

    public static Subject newInstance() {
        return new Subject();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.subject, container,false); // Fragment로 불러올 xml파일을 view로 가져옵니다.



        mSubject_DB= new Subject_DB(this.getContext());
        subrecyclerview= (RecyclerView)view.findViewById(R.id.subrecycler);
        studysubAdapter adapter = new studysubAdapter(this.getContext(),subDataList);
        subrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        subrecyclerview.setAdapter(adapter);
        subDataList=new ArrayList<>();


        //load recent DB
        loadRecentDB();


        Button subjectInput = (Button)view.findViewById(R.id.subjectInputButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        subjectInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                //팝업창 띄우기
                Dialog dialog=new Dialog(Subject.this.getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.activity_subject_input);

                EditText subjectNameInput=dialog.findViewById(R.id.subjectNameInput);
                EditText weekInput=dialog.findViewById(R.id.weekInput);
                EditText weekFrequencyInput=dialog.findViewById(R.id.weekFrequencyInput);

                Button subjectBtn_ok=dialog.findViewById(R.id.subjectInputButton);
                subjectBtn_ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        //Insert Database
                        String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());
                        mSubject_DB.InsetSubject(subjectNameInput.getText().toString(),parseInt(weekInput.getText().toString()),parseInt(weekFrequencyInput.getText().toString()));

                        //Insert UI
                        studysub item=new studysub();
                        item.setSubname(subjectNameInput.getText().toString());
                        item.setWeek(parseInt(weekInput.getText().toString()));
                        item.setWeekFre(parseInt(weekFrequencyInput.getText().toString()));

                        mAdapter.addItem(item);
                        subrecyclerview.setAdapter(mAdapter);
                        subrecyclerview.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(Subject.this.getContext(),"과목이 추가 되었습니다.",Toast.LENGTH_SHORT).show();


                    }
                });

                dialog.show();
            }

            /*@Override
            public void onClick(View v) {
                openDialog();


            }
            private void openDialog() {
                DialogFragment InputSchoolSubject = new InputSchoolSubject();
                InputSchoolSubject.show(getFragmentManager(), "Subject Input");
            }*/
        });

        /*ctx=getContext();


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
*/


    return view;
    }

    private void loadRecentDB() {

        //저장되어 있던 DB를 가져온다
        subDataList=mSubject_DB.getSubList();
        if(mAdapter==null){
            mAdapter=new studysubAdapter(this.getContext(),subDataList);
            subrecyclerview.setHasFixedSize(true);
            subrecyclerview.setAdapter(mAdapter);
        }
    }



}

