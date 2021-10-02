package org.techtown.dgu;

public class study_license {
    String name;
    long studytime;
    String testday;
    Double studyrate;

    public study_license() {
    }

    public study_license(String _name, long _stdytime, String _testday, Double _studyrate ){
        this.name=_name;
        this.studytime=_stdytime;
        this.testday=_testday;
        this.studyrate=_studyrate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStudytime() {
        return studytime;
    }

    public void setStudytime(long studytime) {
        this.studytime = studytime;
    }

    public String getTestday() {
        return testday;
    }

    public void setTestday(String testday) {
        this.testday = testday;
    }

    public Double getStudyrate() {
        return studyrate;
    }

    public void setStudyrate(Double studyrate) {
        this.studyrate = studyrate;
    }
}
