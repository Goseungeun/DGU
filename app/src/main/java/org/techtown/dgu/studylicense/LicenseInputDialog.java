package org.techtown.dgu.studylicense;

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

import org.techtown.dgu.R;

public class LicenseInputDialog extends DialogFragment {

    private MydialogListener myListener;

    public interface MydialogListener {
        public void myCallback(String subjectName);
    }

    public LicenseInputDialog() {
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

        builder.setView(inflater.inflate(R.layout.activity_license_input, null))

                .setPositiveButton("저장", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
//입력받은 데이터 저장 부분 만들기
                       /* EditText esubjectName = (EditText)getDialog().findViewById(R.id.subjectNameInput);
                        String subjectName = esubjectName.getText().toString();

                        myListener. myCallback(subjectName);*/
                    }
                });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();

    }

}