package org.techtown.dgu;

//과목의 시험에 대한 정보를 포함하고 있는 클래스 정의
public class subtest {
    private String subtestname;     //시험 이름 ex)중간고사
    private String  testDday;       //시험까지 남은 날짜 D-day

    public subtest(String subtestname, String testDday){
        this.subtestname = subtestname;
        this.testDday = testDday;
    }

    public String getSubtestname() {
        return subtestname;
    }

    public void setSubtestname(String subtestname) {
        this.subtestname = subtestname;
    }

    public String getTestDday() {
        return testDday;
    }

    public void setTestDday(String testDday) {
        this.testDday = testDday;
    }
}
