package org.techtown.dgu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class InputLicense extends Fragment {
    public static InputLicense newInstance() {
        return new InputLicense();
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_license_input, container, false);

        TextView vDate = vDate.findViewById(R.id.editTextDate);

        vDate.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(InputLicense.this, datePickerActivity.class);

                startActivityForResult(intent, ACT_SET_BIRTH);

            }

        });

    }*/


}






