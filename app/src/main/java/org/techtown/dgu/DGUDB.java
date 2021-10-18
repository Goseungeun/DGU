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
                + "CONSTRAINT hw_fk_id FOREIGN KEY (subid) REFERENCES subject(subid))");

        db.execSQL("CREATE TABLE IF NOT EXISTS test (testid TEXT PRIMARY KEY, subid TEXT NOT NULL, testname TEXT NOT NULL, testdday TEXT NOT NULL,"
                + "CONSTRAINT test_fk_id FOREIGN KEY (subid) REFERENCES subject(subid))");

        db.execSQL("CREATE TABLE IF NOT EXISTS subgraph (subgraphid INTEGER PRIMARY KEY AUTOINCREMENT, subsemester TEXT NOT NULL, subname TEXT NOT NULL,"
                + "subcredit INTEGER NOT NULL, subscore FLOAT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS license (licenseid TEXT PRIMARY KEY, licensename TEXT NOT NULL, licensedday TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS studytime (studytimeid INTEGER PRIMARY KEY AUTOINCREMENT, subid TEXT , licenseid TEXT, date TEXT NOT NULL, studytime TEXT default'"+"00:00:00"+"'," +
                "CONSTRAINT studytime_fk_id_subject FOREIGN KEY (subid) REFERENCES subject(subid)," +
                "CONSTRAINT studytime_fk_id_license FOREIGN KEY (licenseid) REFERENCES license(licenseid))");

        db.execSQL("CREATE TABLE IF NOT EXISTS timetable (timetableid INTEGER PRIMARY KEY AUTOINCREMENT, timetablecontent TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS graph (semester TEXT PRIMARY KEY, gpa INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
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
        db.execSQL("UPDATE subject SET subname = '"+_subname+"',week'"+_week+"',weekfre '"+_weekfre+"' WHERE subid ='"+_subid+"';");
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

    public String insertHw (String _subid,String _hwname,String _hwdday){
        String _id = give_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO hw  VALUES('"+ _id+"','" +_subid + "','" +_hwname+"','"+_hwdday+"');'");
        return _id;
    }

    public void UpdateHw (String _hwid, String _hwname, String _hwdday){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE hw SET hwname = '"+_hwname+"',hwdday'"+_hwdday+"' WHERE hwid ='"+_hwid+"';");
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
        db.execSQL("UPDATE test SET testname = '"+_testname+"',testdday'"+_testdday+"' WHERE testid ='"+_testid+"';");
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
        cursor.moveToNext();

        String licensename = cursor.getString(cursor.getColumnIndex("licensename"));

        cursor.close();
        return licensename;
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

    public String SearchStudytimeID(String _subid, String _licenseid){
        SQLiteDatabase db1 = getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT studytimeid FROM studytime " +
                "where date ='"+give_Today()+"'and subid ='"+_subid+"' and licenseid ='"+_licenseid+"'",null);

        if(cursor.getCount()==0){
            cursor.close();
            return null;
        }else{
            String id=null;
            while(cursor.moveToNext()){
                id = cursor.getString(cursor.getColumnIndex("studytimeid"));
            }
            cursor.close();
            return id;
        }

    }

    public String InsertStudyTime(String _subid, String _licenseid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO studytime (subid ,licenseid, date, studytime) VALUES('"+ _subid+"','" +_licenseid + "','" +give_Today()+"','" +"00:00:00"+"');");

        SQLiteDatabase db1 = getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT * FROM studytime",null);
        String id=null;
        while(cursor.moveToNext()){
             id= cursor.getString(cursor.getColumnIndex("studytimeid"));
             Log.v("studytime id" , id);
        }
        cursor.close();
        return id;
    }

    public void UpdateStudyTime(String _studytimeid, String _studytime){
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
    public String getStudytime(String _studytimeid){
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

//    public String DateTotalStudyTime(String _date){
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT sum(strftime('HH:MM:SS',studytime)) FROM studytime WHERE date ='"+_date+"'",null);
//        System.out.print(cursor);
//        int time=0;
//        String result = null;
//
//        int i=0;
//        while(cursor.moveToNext()){
//
//            time = cursor.getInt(0);
//            Log.v("totalTimeSumming","time : "+time);
//            Cursor cursor1 = db.rawQuery("select strftime('HH:mm:ss','"+result+"') + strftime('HH:mm:ss','"+time+"') ",null);
//            cursor1.moveToNext();
//            result=cursor1.getString(0);
//            Log.v("totalTimeSumming","time : "+time+", result : "+result);
//            cursor1.close();
//            i++;
//        }
//        cursor.close();
//        return result;
//    }

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





    //StopwatchToday와 연결
    public String getStudyTime(String _date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM studytime where date ='"+_date+"'",null);
        String result = "00:00:00";
        cursor.moveToNext();
        if(cursor.getCount()!=0){
            result = cursor.getString(cursor.getColumnIndex("studytime"));
        }
        cursor.close();
        return result;
    }


}
