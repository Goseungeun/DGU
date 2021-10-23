package org.techtown.dgu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class datepicker  extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int year;
    private int month;
    private int day;
    private TextView textView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // DatePickerDialog THEME_HOLO_LIGHT
        DatePickerDialog theme_holo_light = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);

        // Return the DatePickerDialog
        return theme_holo_light;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month=month+1;
        this.day=day;
        textView.setText(""+year+"년 "+this.month+"월 "+day+"일");
    }

    public void setTextView(TextView _textview){
        this.textView = _textview;
    }

    public String getYear() {
        return ""+year;
    }

    public String getMonth() {
        if(month>9){
            return ""+month;
        }else{
            return "0"+month;
        }
    }

    public String getDay() {
        if(day>9){
            return ""+day;
        }else{
            return "0"+day;
        }
    }
}

