package org.techtown.dgu;

import android.util.Log;

public class TimetableStudyitem {
    int studytimeid;
    String name;
    String studytime;
    String category;

    TimetableStudyitem(int studytimeid, String name, String studytime, String category){
        this.studytimeid = studytimeid;
        this.name = name;
        this.studytime = studytime;
        this.category = category;
    }

    public int getStudytimeid() {
        return studytimeid;
    }

    public String getName() {
        return name;
    }

    public String getStudytime() {
        return studytime;
    }

    public String getCategory() {
        return category;
    }

}
