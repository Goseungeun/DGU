package org.techtown.dgu;

//과제 정보를 표현하는 클래스 정의
public class homework {
    private int id;
    private String hwname;  //과제 이름
    private String hwDday;     //마감 날짜까지 얼마나 남았는지 D-day

    public homework(String hwname,String hwDday){
        this.hwname = hwname;
        this.hwDday = hwDday;
    }

    public homework() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
