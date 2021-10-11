package org.techtown.dgu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import org.techtown.dgu.studylicense.LicenseItem;

import java.util.ArrayList;

public class DGUDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DGU.db";

    public DGUDB(@NonNull Context context) { super(context, DB_NAME,null,DB_VERSION);}

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS subject (subid TEXT PRIMARY KEY, subname TEXT NOT NULL, week INTEGER NOT NULL, weekfre INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS hw (hwid INTEGER PRIMARY KEY AUTOINCREMENT, subid TEXT NOT NULL, hwname TEXT NOT NULL, hwdday TEXT NOT NULL,"
                + "CONSTRAINT hw_fk_id FOREIGN KEY (subid) REFERENCES subject(subid))");

        db.execSQL("CREATE TABLE IF NOT EXISTS test (testid INTEGER PRIMARY KEY AUTOINCREMENT, subid TEXT NOT NULL, testname TEXT NOT NULL, testdday TEXT NOT NULL,"
                + "CONSTRAINT test_fk_id FOREIGN KEY (subid) REFERENCES subject(subid))");

        db.execSQL("CREATE TABLE IF NOT EXISTS subgraph (subgraphid INTEGER PRIMARY KEY AUTOINCREMENT, subsemester TEXT NOT NULL, subname TEXT NOT NULL,"
                + "subcredit INTEGER NOT NULL, subscore FLOAT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS license (licenseid TEXT PRIMARY KEY, licensename TEXT NOT NULL, licensedday TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS studytime (studytimeid INTEGER PRIMARY KEY AUTOINCREMENT, subid TEXT , licenseid TEXT, date TEXT NOT NULL, studytime TEXT NOT NULL," +
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
}
