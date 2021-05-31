package org.techtown.dgu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;


/*public class InputSchoolSubject extends Fragment {
    EditText subjectNameInput;
    EditText weekInput;
    EditText weekFrequencyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_input);

        subjectNameInput = findViewById(R.id.subjectNameInput);
        weekInput = findViewById(R.id.weekInput);
        weekFrequencyInput = findViewById(R.id.weekFrequencyInput);


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String subjectName = subjectNameInput.getText().toString();
                Integer week = Integer.parseInt(weekInput.getText().toString());
                Integer weekFrequency = Integer.parseInt(weekFrequencyInput.getText().toString());

                Toast.makeText(getApplicationContext(), "과목명 : " + subjectName + ", 주차 : " + week + ", 횟수 : " + weekFrequency, Toast.LENGTH_SHORT).show();

                finish();
                overridePendingTransition(R.anim.entry_back, R.anim.exit_back);
            }
        });
    }
}*/



public class InputSchoolSubject extends DialogFragment {

    private RecyclerView subrecyclerview;
    private Subject_DB mSubject_DB;
    ArrayList<studysub> subDataList;
    private studysubAdapter mAdapter;

    private MydialogListener myListener;

    public interface MydialogListener {
        public void myCallback(String subjectName);
    }

    public InputSchoolSubject() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myListener = (MydialogListener) getTargetFragment();

        } catch (ClassCastException e) {
            throw new ClassCastException();
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.activity_subject_input, null))

                .setPositiveButton("저장", new DialogInterface.OnClickListener() {

                    private Object EditText;


                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        //입력받은 데이터 저장 부분 만들기
                    /*    EditText esubjectName = (EditText)getDialog().findViewById(R.id.subjectNameInput);
                        String subjectName = esubjectName.getText().toString();

                        myListener. myCallback(subjectName);*/

                        EditText subjectNameInput=(EditText)getDialog().findViewById(R.id.subjectNameInput);
                        EditText weekInput=(EditText)getDialog().findViewById(R.id.weekInput);
                        EditText weekFrequencyInput=(EditText)getDialog().findViewById(R.id.weekFrequencyInput);


                        //Insert Database
                        String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());
                        mSubject_DB.InsetSubject(subjectNameInput.getText().toString(),parseInt(weekInput.getText().toString()),parseInt(weekFrequencyInput.getText().toString()));

                        //Insert UI
                        studysub item=new studysub();
                        item.setSubname(subjectNameInput.getText().toString());
                        item.setWeek(parseInt(weekInput.getText().toString()));
                        item.setWeekFre(parseInt(weekFrequencyInput.getText().toString()));

                        mAdapter.addItem(item);

                        subrecyclerview.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(InputSchoolSubject.this.getContext(),"과목이 추가 되었습니다.",Toast.LENGTH_SHORT).show();


                    }
                });
                 builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }










    /*public static InputSchoolSubject newInstance() {
        return new InputSchoolSubject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_subject_input, container, false);

    }*/

}