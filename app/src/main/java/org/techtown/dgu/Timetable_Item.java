package org.techtown.dgu;

import android.graphics.Color;

public class Timetable_Item {
    int hour;
    int min;
    boolean study;

    public Timetable_Item(int hour, int min,boolean study){
        this.hour=hour;
        this.min=min;
        this.study=study;
    }

    public Boolean getStudy(){
        return study;
    }
    public void setStudy(boolean study){this.study=study;}

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }


}
