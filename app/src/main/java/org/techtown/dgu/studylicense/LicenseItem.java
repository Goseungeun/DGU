package org.techtown.dgu.studylicense;

public class LicenseItem {
    String name;
    String studytime;
    String testday;
    Double studyrate;

    public LicenseItem() {
    }

    public LicenseItem(String _name, String _studytime, String _testday, Double _studyrate){
        this.name = _name;
        this.studytime=_studytime;
        this.testday=_testday;
        this.studyrate=_studyrate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudytime() {
        return studytime;
    }

    public void setStudytime(String studytime) {
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
