package org.techtown.dgu.studylicense;

public class LicenseItem {
    String licenseid;
    String licensename;
    String licensedday;
    String licensestudytime;

    public String getViewdday() {
        return Viewdday;
    }

    public void setViewdday(String viewdday) {
        Viewdday = viewdday;
    }

    String Viewdday;

    public String getLicensestudytime() {
        return licensestudytime;
    }

    public void setLicensestudytime(String licensestudytime) {
        this.licensestudytime = licensestudytime;
    }

    public LicenseItem() {
    }

    public LicenseItem( String _licensename, String _licensedday){
        this.licensename=_licensename;
        this.licensedday=_licensedday;
    }

    public LicenseItem(String _licenseid, String _licensename, String _licensedday){
        this.licenseid=_licenseid;
        this.licensename=_licensename;
        this.licensedday=_licensedday;
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid;
    }

    public String getLicensename() {
        return licensename;
    }

    public void setLicensename(String licensename) {
        this.licensename = licensename;
    }

    public String getLicensedday() {
        return licensedday;
    }

    public void setLicensedday(String licensedday) {
        this.licensedday = licensedday;
    }
}


