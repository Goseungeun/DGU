package org.techtown.dgu.homework;

//과제 정보를 표현하는 클래스 정의
public class homework {
    private String id;
    private String hwname;  //과제 이름
    private String hwDday;     //마감 날짜까지 얼마나 남았는지 D-day

    public homework(String id,String hwname,String hwDday){
        this.id = id;
        this.hwname = hwname;
        this.hwDday = hwDday;
    }

    public homework(String hwname, String hwDday) {
        this.hwname = hwname;
        this.hwDday = hwDday;
    }

    public String getViewdday() {
        return Viewdday;
    }

    public void setViewdday(String viewdday) {
        Viewdday = viewdday;
    }

    String Viewdday;


    public homework() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHwname() {
        return hwname;
    }

    public void setHwname(String hwname) {
        this.hwname = hwname;
    }

    public String getHwDday() {
        return hwDday;
    }

    public void setHwDday(String hwDday) {
        this.hwDday = hwDday;
    }

}
