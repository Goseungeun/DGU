package org.techtown.dgu.subject;

import org.techtown.dgu.homework.homework;
import org.techtown.dgu.test.SubTestItem;

import java.util.ArrayList;

public class SubjectItem {

    private String subid;

    private String subname;
    private String subtime;
    private int week;
    private int weekFre;
    private ArrayList<homework> hwList;
    private ArrayList<SubTestItem> testList;

    public SubjectItem(){
    }

    public SubjectItem(String subname, int week, int weekFre){
        this.subname = subname;
        this.week = week;
        this.weekFre = weekFre;
    }

    public SubjectItem(String subid, String subname, int week, int weekFre, ArrayList<homework> hwList, ArrayList<SubTestItem> testList){
        this.subid = subid;
        this.subname = subname;
        this.week=week;
        this.weekFre=weekFre;
        this.hwList = hwList;
        this.testList = testList;
    }

    public String getId() {
        return subid;
    }

    public void setId(String subid) {
        this.subid = subid;
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

    public ArrayList<SubTestItem> getTestList() {
        return testList;
    }

    public void setTestList(ArrayList<SubTestItem> testList) {
        this.testList = testList;
    }
}
