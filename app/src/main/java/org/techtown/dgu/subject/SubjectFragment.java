package org.techtown.dgu.subject;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;
import java.util.ArrayList;
import static java.lang.Integer.parseInt;

public class SubjectFragment extends Fragment {

    private RecyclerView subrecyclerview;
    private DGUDB db;
    ArrayList<SubjectItem> subjectList;
    private SubjectAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.subject, container,false); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        subjectList = new ArrayList<>();
        db= new DGUDB(this.getContext());
        subrecyclerview= (RecyclerView)view.findViewById(R.id.subrecycler);
        mAdapter = new SubjectAdapter(this.getContext(),subjectList);
        subrecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        subrecyclerview.setAdapter(mAdapter);
        // 여기서부터 화면만 보이게 만든거
        loadRecentDB();

        Button subjectInput = (Button)view.findViewById(R.id.subjectInputButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        subjectInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                //팝업창 띄우기
                Dialog dialog=new Dialog(SubjectFragment.this.getContext(), android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.activity_subject_input);

                EditText subjectNameInput=dialog.findViewById(R.id.subjectNameInput);
                EditText weekInput=dialog.findViewById(R.id.weekInput);
                EditText weekFrequencyInput=dialog.findViewById(R.id.weekFrequencyInput);

                Button subjectBtn_ok=dialog.findViewById(R.id.subjectInputButton);
                subjectBtn_ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String subname = subjectNameInput.getText().toString();
                        String week = weekInput.getText().toString();
                        String weekfre = weekFrequencyInput.getText().toString();
                        if(!(subname.equals("")||week.equals("")||weekfre.equals(""))){
                            //Insert UI
                            Integer Week=parseInt(week);
                            Integer WeekFre=parseInt(weekfre);
                            SubjectItem item = new SubjectItem(subname,Week,WeekFre);
                            mAdapter.addSubItem(item);
                            loadRecentDB();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(v.getContext(),"정보를 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
                        }
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
        subjectList=db.getsubjectlist();
        mAdapter=new SubjectAdapter(getContext(),subjectList);
        subrecyclerview.setAdapter(mAdapter);
        }
    }


