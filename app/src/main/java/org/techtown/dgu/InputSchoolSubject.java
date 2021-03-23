package org.techtown.dgu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/*
public class InputSchoolSubject extends AppCompatActivity {
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




public class InputSchoolSubject extends Fragment {
    public static InputSchoolSubject newInstance() {
        return new InputSchoolSubject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_subject_input, container, false);

    }

}