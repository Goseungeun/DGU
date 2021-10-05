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

                        mAdapter.addSubItem(item);
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

