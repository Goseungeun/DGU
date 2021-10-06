package org.techtown.dgu.mylicense;

public class MyLicenseItem {
    String mylicensename;
    String getyear;
    String getmonth;
    String getdate;
    String endyear;
    String endmonth;
    String enddate;

    public MyLicenseItem(String mylicensename, String getyear, String getmonth, String getdate,
                         String endyear, String endmonth, String enddate) {
        this.mylicensename = mylicensename;
        this.getyear = getyear;
        this.getmonth = getmonth;
        this.getdate = getdate;
        this.endyear = endyear;
        this.endmonth = endmonth;
        this.enddate = enddate;
    }

    public String getMylicensename() {
        return mylicensename;
    }

    public void setMylicensename(String mylicensename) {
        this.mylicensename = mylicensename;
    }

    public String getGetyear() {
        return getyear;
    }

    public void setGetyear(String getyear) {
        this.getyear = getyear;
    }

    public String getGetmonth() {
        return getmonth;
    }

    public void setGetmonth(String getmonth) {
        this.getmonth = getmonth;
    }

    public String getGetdate() {
        return getdate;
    }

    public void setGetdate(String getdate) {
        this.getdate = getdate;
    }

    public String getEndyear() {
        return endyear;
    }

    public void setEndyear(String endyear) {
        this.endyear = endyear;
    }

    public String getEndmonth() {
        return endmonth;
    }

    public void setEndmonth(String endmonth) {
        this.endmonth = endmonth;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
