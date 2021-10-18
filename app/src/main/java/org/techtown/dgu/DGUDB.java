package org.techtown.dgu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import org.techtown.dgu.homework.homework;
import org.techtown.dgu.studylicense.LicenseItem;
import org.techtown.dgu.subject.SubjectItem;
import org.techtown.dgu.test.SubTestItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DGUDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DGU.db";

    public DGUDB(@NonNull Context context) { super(context, DB_NAME,null,DB_VERSION);}

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS subject (subid TEXT PRIMARY KEY, subname TEXT NOT NULL, week INTEGER NOT NULL, weekfre INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS hw (hwid TEXT PRIMARY KEY, subid TEXT NOT NULL, hwname TEXT NOT NULL, hwdday TEXT NOT NULL,"
                + "CONSTRAINT hw_fk_id FOREIGN KEY (subid) REFERENCES subject(subid) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS test (testid TEXT PRIMARY KEY, subid TEXT NOT NULL, testname TEXT NOT NULL, testdday TEXT NOT NULL,"
                + "CONSTRAINT test_fk_id FOREIGN KEY (subid) REFERENCES subject(subid) ON DELETE CASCADE )");

        db.execSQL("CREATE TABLE IF NOT EXISTS subgraph (subgraphid INTEGER PRIMARY KEY AUTOINCREMENT, subsemester TEXT NOT NULL, subname TEXT NOT NULL,"
                + "subcredit INTEGER NOT NULL, subscore FLOAT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS license (licenseid TEXT PRIMARY KEY, licensename TEXT NOT NULL, licensedday TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS studytime (studytimeid INTEGER PRIMARY KEY AUTOINCREMENT, subid TEXT , licenseid TEXT, date TEXT NOT NULL, studytime TEXT default'"+"00:00:00"+"'," +
                "CONSTRAINT studytime_fk_id_subject FOREIGN KEY (subid) REFERENCES subject(subid) ON DELETE CASCADE," +
                "CONSTRAINT studytime_fk_id_license FOREIGN KEY (licenseid) REFERENCES license(licenseid) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS timetable (timetableid TEXT PRIMARY KEY, timetablecontent TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS graph (semester TEXT PRIMARY KEY, gpa INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS attendancecheck (subid TEXT PRIMARY KEY, attendancecheckcontent TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //여기부터 attendancecheck와 관련된 함수

    public void InsertAttendancecheck(String _subid, String _attendancecheckcontent){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO attendancecheck VALUES('"+ _subid+"','" +_attendancecheckcontent +"');'");
    }

    public void UpdateAttendancecheck(String _subid, String _attendancecheckcontent){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE attendancecheck SET attendancecheckcontent = '"+_attendancecheckcontent+"' WHERE subid ='"+_subid+"';");
    }

    public void deleteAttendancecheck(String _subid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM attendancecheck WHERE subid = '"+_subid+"';");
    }

    //subid를 입력하면 존재하는지 알아보기, 존재하면 true, 존재하지 않으면 false
    public boolean isExistAttendancecheck(String _subid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM attendancecheck WHERE subid = '"+_subid+"'",null);

        int count = cursor.getCount();
        cursor.close();
        if(count==0){ return false; }
        else{return true;}
    }

    public String getAttendanceCheckcontent(String _subid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM attendancecheck " +
                "WHERE subid ='"+_subid+"';",null);

        String result="";
        while(cursor.moveToNext()){
            result = cursor.getString(cursor.getColumnIndex("attendancecheckcontent"));
        }
        cursor.close();
        return result;
    }

    public String getSubjectInfo(String _subid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM subject WHERE subid ='"+_subid+"';",null);
        String result="";
        if (cursor.getCount() != 0){
            cursor.moveToNext();

            String subname = cursor.getString(cursor.getColumnIndex("subname"));
            String week = cursor.getString(cursor.getColumnIndex("week"));
            String weekfre = cursor.getString(cursor.getColumnIndex("weekfre"));

            result = subname+","+week+","+weekfre;

        }
        cursor.close();
        return result;
    }



    //subject와 license의 id를 부여하는 함수
    public String give_id(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" select strftime('%Y-%m-%d %H:%M:%S','now','localtime');",null);
        cursor.moveToNext();
        String result = cursor.getString(0);
        cursor.close();
        return result;
    }

    // subject table 조작 함수
    public ArrayList<SubjectItem> getsubjectlist(){
        ArrayList<SubjectItem> subList = new ArrayList<>();
        ArrayList<homework> hwList;
        ArrayList<SubTestItem> testList;

        SQLiteDatabase db = getReadableDatabase();
        Cursor sub_cursor = db.rawQuery("SELECT * FROM subject;",null);
        if(sub_cursor.getCount() != 0){
            //커서 안에 데이터 존재하고, 커서가 첫번째 아이템에 위치하고 있을 때
            while (sub_cursor.moveToNext()){
                //커서의 끝까지 진행
                String sub_id = sub_cursor.getString(sub_cursor.getColumnIndex("subid"));
                String sub_name = sub_cursor.getString(sub_cursor.getColumnIndex("subname"));
                int week = sub_cursor.getInt(sub_cursor.getColumnIndex("week"));
                int weekfre = sub_cursor.getInt(sub_cursor.getColumnIndex("weekfre"));
                hwList = gethwList(sub_id);
                Log.d("확인","hwList = "+ hwList);
                testList = gettestList(sub_id);

                SubjectItem subjectItem = new SubjectItem(sub_id,sub_name,week,weekfre,hwList,testList);
                subList.add(subjectItem);
            }
        }
        sub_cursor.close();
        return subList;
    }

    public String InsertSubject (String _subname,int _week, int _weekfre){
        String _id= give_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO subject VALUES('"+ _id+"','" +_subname + "','" +_week+"','"+_weekfre+"');'");
        return _id;
    }

    public void Updatesubject (String _subid, String _subname, int _week, int _weekfre){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE subject SET subname = '"+_subname+"',week = '"+_week+"',weekfre = '"+_weekfre+"' WHERE subid ='"+_subid+"';");
    }

    public void deleteSubject (String _subid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM subject WHERE subid = '"+_subid+"';");
        db.execSQL("DELETE FROM hw WHERE subid ='"+_subid+"';");
        db.execSQL("DELETE FROM test WHERE subid ='"+_subid+"';");
        deleteAttendancecheck(_subid);
    }

    //homework table 관련 함수
    public ArrayList<homework> gethwList(String sub_id){
        ArrayList<homework> hwList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor hw_cursor = db.rawQuery("SELECT * FROM hw WHERE subid ='"+sub_id+"';",null);
        if (hw_cursor.getCount() != 0){
            while (hw_cursor.moveToNext()){
                String hwId = hw_cursor.getString(hw_cursor.getColumnIndex("hwid"));
                String hwname = hw_cursor.getString(hw_cursor.getColumnIndex("hwname"));
                String hwdday = hw_cursor.getString(hw_cursor.getColumnIndex("hwdday"));
                Log.d("확인","hwid: " + hwId);
                homework hwitem = new homework(hwId,hwname,hwdday);
                hwList.add(hwitem);
            }
        }
        hw_cursor.close();
        return hwList;
    }

    public String getSubjectName(String _subid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM subject WHERE subid = '"+_subid+"'",null);

        if(cursor.getCount()==0){
            //과목이 삭제된 경우
            cursor.close();
            return null;
        }else{
            cursor.moveToNext();
            String subname = cursor.getString(cursor.getColumnIndex("subname"));
            cursor.close();
            return subname;
        }

    }

    public String insertHw (String _subid,String _hwname,String _hwdday){
        String _id = give_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO hw  VALUES('"+ _id+"','" +_subid + "','" +_hwname+"','"+_hwdday+"');'");
        return _id;
    }

    public void UpdateHw (String _hwid, String _hwname, String _hwdday){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE hw SET hwname = '"+_hwname+"',hwdday ='"+_hwdday+"' WHERE hwid ='"+_hwid+"';");
    }

    public void deleteHW (String _hwid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM hw WHERE hwid = '"+_hwid+"';");
    }

    //test table 관련 함수
    public ArrayList<SubTestItem> gettestList(String sub_id){
        ArrayList<SubTestItem> testList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor test_cursor = db.rawQuery("SELECT * FROM test WHERE subid ='"+sub_id+"';",null);
        if (test_cursor.getCount() != 0){
            while (test_cursor.moveToNext()){
                String testid = test_cursor.getString(test_cursor.getColumnIndex("testid"));
                String testname = test_cursor.getString(test_cursor.getColumnIndex("testname"));
                String testdday = test_cursor.getString(test_cursor.getColumnIndex("testdday"));
                SubTestItem testitem = new SubTestItem(testid,testname,testdday);
                testList.add(testitem);
            }
        }
        test_cursor.close();
        return testList;
    }

    public String insertTest (String _subid,String _testname,String _testdday){
        String _id = give_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO test VALUES('"+ _id+"','"+ _subid+"','" +_testname + "','" +_testdday+"');");
        return _id;
    }

    public void UpdateTest (String _testid, String _testname, String _testdday){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE test SET testname = '"+_testname+"',testdday = '"+_testdday+"' WHERE testid ='"+_testid+"';");
    }

    public void deleteTest (String _testid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM test WHERE testid ='" + _testid+"';");
    }

    /// 여기부터 license table과 관련된 함수

    //SELECT 문 / LicenseFragment.java와 연결됨.
    public ArrayList<LicenseItem> getlicenselist(){
        ArrayList<LicenseItem> study_licenses = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM license",null);
        if(cursor.getCount() != 0 ){
            //조회할 데이터가 있을 경우 진행
            while (cursor.moveToNext()){
                //데이터 베이스 끝까지 진행
                String licenseid = cursor.getString(cursor.getColumnIndex("licenseid"));
                String licensename = cursor.getString(cursor.getColumnIndex("licensename"));
                String licensedday = cursor.getString(cursor.getColumnIndex("licensedday"));

                LicenseItem item = new LicenseItem(licenseid,licensename,licensedday);
                study_licenses.add(item);

            }
        }
        cursor.close();

        return study_licenses;
    }

    //INSERT문
    public String InsertLicense (String _licensename,String _licensedday){
        String _id= give_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO license (licenseid ,licensename, licensedday) VALUES('"+ _id+"','" +_licensename + "','" +_licensedday+"');");
        return _id;
    }

    //UPDATE 문 (dialog를 통해 수정할 때)
    public void UpdateLicense(String _licenseid, String _licensename, String _licensedday){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE license SET licensename ='"+_licensename+"',licensedday ='"+_licensedday+"' WHERE licenseid = '"+_licenseid+"'");
    }

    //DELETE 문
    public void deleteLicense(String _licenseid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM license WHERE licenseid ='" + _licenseid+"'");
    }

    public String getLicenseName(String _licenseid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM license WHERE licenseid = '"+_licenseid+"'",null);

        if(cursor.getCount()==0){
            //자격증이 삭제된 경우
            cursor.close();
            return null;
        }else{
            cursor.moveToNext();
            String licensename = cursor.getString(cursor.getColumnIndex("licensename"));
            cursor.close();
            return licensename;
        }

    }

    /*    //하루지나면 초기화
    public void Reset(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM license",null);
        if(cursor.getCount() != 0 ){
            //조회할 데이터가 있을 경우 진행
            while (cursor.moveToNext()){
                //데이터 베이스 끝까지 진행
                String name = cursor.getString(cursor.getColumnIndex("name"));
                UpdateLicenseStudyTime(name, "00:00:00");
            }
        }
        cursor.close();
    }*/

    ///여기부터 studytime table과 관련된 함수

    public String give_Today(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT strftime('%Y-%m-%d','now','localtime');",null);
        cursor.moveToNext();
        String result = cursor.getString(0);
        cursor.close();
        return result;
    }

    public String give_Yesterday(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT strftime('%Y-%m-%d','now','localtime','-1 day');",null);
        cursor.moveToNext();
        String result = cursor.getString(0);
        cursor.close();
        return result;
    }

    public int SearchStudytimeID(String _subid, String _licenseid){
        SQLiteDatabase db1 = getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT studytimeid FROM studytime " +
                "where date ='"+give_Today()+"'and subid ='"+_subid+"' and licenseid ='"+_licenseid+"'",null);

        if(cursor.getCount()==0){
            cursor.close();
            return 0;
        }else{
            int id = 0;
            while(cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndex("studytimeid"));
            }
            cursor.close();
            return id;
        }

    }

    public int InsertStudyTime(String _subid, String _licenseid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO studytime (subid ,licenseid, date, studytime) VALUES('"+ _subid+"','" +_licenseid + "','" +give_Today()+"','" +"00:00:00"+"');");

        SQLiteDatabase db1 = getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT * FROM studytime",null);
        int id=0;
        while(cursor.moveToNext()){
             id= cursor.getInt(cursor.getColumnIndex("studytimeid"));
             Log.v("studytime id" , ""+id);
        }
        cursor.close();
        return id;
    }

    public void UpdateStudyTime(int _studytimeid, String _studytime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE studytime SET studytime='"+_studytime+"' WHERE studytimeid = '"+_studytimeid+"'" );
    }

    //TODO : DeleteStudyTime 수정필요
    public void DeleteStudyTime(String _date, String _studytime){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM studytime WHERE date = '"+_date+"'");
    }

    //이미 존재하는 행인지 아닌지 판단.
    public String getStudytime(int _studytimeid){
        Log.v("getstudytime",""+_studytimeid);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM studytime " +
                "where studytimeid ='"+_studytimeid+"'",null);

        cursor.moveToNext();
        if(cursor.getCount()==0){
            //존재하는 행이 없음
            cursor.close();
            return null;
        }else{
            //존재하는 행이면 공부시간 반환.
           String result = cursor.getString(cursor.getColumnIndex("studytime"));
            cursor.close();
            return result;
        }
    }

    public String DateTotalStudyTime(String _date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db. rawQuery("SELECT time(sum(cast(strftime('%s',studytime) AS INTEGER)),'unixepoch') AS total FROM studytime WHERE date = '"+_date+"'",null);
        String result = "00:00:00";
        if(cursor!=null && cursor.moveToFirst()){
            Log.d("확인",""+cursor.getString(cursor.getColumnIndex("total")));
            result = cursor.getString(cursor.getColumnIndex("total"));
        }

        return result;
    }

    public String MonthTotalStudyTime(String _startdate, String _enddate){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db. rawQuery("SELECT time(sum(cast(strftime('%s',studytime) AS INTEGER)),'unixepoch') AS total " +
                "FROM studytime WHERE strftime('%s', date) BETWEEN strftime('%s', '"+_startdate+"') AND strftime('%s', '"+_enddate+"')",null);
        String result = "00:00:00";
        if(cursor!=null && cursor.moveToFirst()){
            Log.d("확인",""+cursor.getString(cursor.getColumnIndex("total"))+_startdate+_enddate);
            result = cursor.getString(cursor.getColumnIndex("total"));
        }

        return result;
    }

    //studytimeid를 입력하면 subjectname이나 licensename을 출력한다.
    public String getSubjectnameOrLicensename(int _studytimeid){
        SQLiteDatabase db = getReadableDatabase();
        String result="";

        //과목인경우
        Cursor cursor1 = db.rawQuery("SELECT * FROM studytime " +
                "where studytimeid ='"+_studytimeid+"'and licenseid = '"+null+"'",null);

        cursor1.moveToNext();
        if(cursor1.getCount()!=0){

            String subID = cursor1.getString(cursor1.getColumnIndex("subid"));

            if(getSubjectName(subID)==null){
                //삭제된 과목일 경우
                result= "과목,삭제됨";
            }else{
                result= "과목,"+getLicenseName(subID);
            }

        }

        //자격증인 경우
        Cursor cursor2 = db.rawQuery("SELECT * FROM studytime " +
                "where studytimeid ='"+_studytimeid+"'and subid = '"+null+"'",null);

        cursor2.moveToNext();
        if(cursor2.getCount()!=0){
            String licenseID = cursor2.getString(cursor2.getColumnIndex("licenseid"));

            if(getLicenseName(licenseID)==null){
                //삭제된 과목일 경우
                result= "자격증,삭제됨";
                Log.v("licenseIDnull",result);
            }else{
                result= "자격증,"+getLicenseName(licenseID);
            }
        }

        cursor1.close();
        cursor2.close();
        return result;
    }

    //date를 입력하면 date와 같은 값을 가지는 행의 공부id string 배열을 출력한다.
    public int[] getStudytimeIdArray(String _date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM studytime " +
                "where date ='"+_date+"'",null);

        int result[] = new int[cursor.getCount()];
        int i=0;
        while(cursor.moveToNext()){
            result[i] = cursor.getInt(cursor.getColumnIndex("studytimeid"));
            i++;
        }

        cursor.close();
        return result;
    }


    //timetable과 관련된 함수 시작
    public void InsertTimeTable(String _timetableid, String _timetablecontent){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO timetable (timetableid , timetablecontent) VALUES('"+ _timetableid+"','" +_timetablecontent+"');");
    }

    public void UpdateTimeTable(String _timetableid, String _timetablecontent){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE timetable SET timetablecontent='"+_timetablecontent+"'" +
                " WHERE timetableid = '"+_timetableid+"'" );
    }

    //TODO deletetimetable 수정필요
    public void DeleteTimeTable(String _timetableid, String _timetablecontent){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM timetable WHERE timetableid = '"+_timetableid+"'");
    }

    public String getTimeTableContent(String _timetableid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM timetable " +
                "where timetableid = '"+_timetableid+"'",null);

        String result="";
        while(cursor.moveToNext()){
            result = cursor.getString(cursor.getColumnIndex("timetablecontent"));
        }
        cursor.close();
        return result;
    }

    //date를 입력하면 date와 같은 행이 존재하는지 알아보기, 존재하면 true, 존재하지 않으면 false
    public boolean isExistTodayTimeTable(String _timetableid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM timetable " +
                "where timetableid = '"+_timetableid+"'",null);

        int count = cursor.getCount();
        cursor.close();
        if(count==0){ return false; }
        else{return true;}
    }





}
