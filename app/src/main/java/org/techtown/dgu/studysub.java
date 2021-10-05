package org.techtown.dgu;

import java.util.ArrayList;

public class studysub {

    private int id;

    private String subname;
    private String subtime;
    private int week;
    private int weekFre;
    private ArrayList<homework> hwList;
    private ArrayList<subtest> testList;

    public studysub(){

    }

    public studysub(String subname,String subtime,int week, int weekFre,ArrayList<homework> hwList, ArrayList<subtest> testList){
        this.subname = subname;
        this.subtime = subtime;
        this.week=week;
        this.weekFre=weekFre;
        this.hwList = hwList;
        this.testList = testList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeekFre() {
        return weekFre;
    }

    public void setWeekFre(int weekFre) {
        this.weekFre = weekFre;
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
