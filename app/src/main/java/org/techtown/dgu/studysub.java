package org.techtown.dgu;

import java.util.ArrayList;

public class studysub {
    private String subname;
    private String subtime;
    private ArrayList<homework> hwList;
    private ArrayList<subtest> testList;

    public studysub(){

    }

    public studysub(String subname,String subtime,ArrayList<homework> hwList, ArrayList<subtest> testList){
        this.subname = subname;
        this.subtime = subtime;
        this.hwList = hwList;
        this.testList = testList;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubtime() {
        return subtime;
    }

    public void setSubtime(String subtime) {
        this.subtime = subtime;
    }

    public ArrayList<homework> getHwList() {
        return hwList;
    }

    public void setHwList(ArrayList<homework> hwList) {
        this.hwList = hwList;
    }

    public ArrayList<subtest> getTestList() {
        return testList;
    }

    public void setTestList(ArrayList<subtest> testList) {
        this.testList = testList;
    }
}
