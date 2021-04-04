package org.techtown.dgu;

import android.graphics.Color;

public class Timetable_Item {
    int hour;
    int min;
    Boolean study;

    public Timetable_Item(int hour, int min){
        this.hour=hour;
        this.min=min;
        this.study=false;
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
