package org.techtown.dgu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class STLicenseDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "StudyLicense.db";

    public STLicenseDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS licenselist (name TEXT NOT NULL, studytime TEXT NOT NULL, testday TEXT NOT NULL, studyrate DOUBLE NOT NULL, showitem BOOLEN NOT NULL DEFAULT 'TRUE')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //SELECT 문
    public ArrayList<study_license> getlicenselist(){
        ArrayList<study_license> study_licenses = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM licenselist",null);
        if(cursor.getCount() != 0 ){
            //조회할 데이터가 있을 경우 진행
            while (cursor.moveToNext()){
                //데이터 베이스 끝까지 진행
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String studytime = cursor.getString(cursor.getColumnIndex("studytime"));
                String testday = cursor.getString(cursor.getColumnIndex("testday"));
                Double studyrate = cursor.getDouble(cursor.getColumnIndex("studyrate"));

                study_license item = new study_license();
                item.setName(name);
                item.setStudytime(studytime);
                item.setTestday(testday);
                item.setStudyrate(studyrate);
                study_licenses.add(item);

            }
        }
        cursor.close();

        return study_licenses;
    }

    //INSERT문
    public void InsertLicense (String _name,String _studytime, String _testday, Double _studyrate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO licenselist (name, studytime, testday, studyrate) VALUES('" +_name + "','" +_studytime + "','" +_testday + "','"+_studyrate+"');");
    }

    //UPDATE 문 (수정하기 Dialog를 통해 자격증 이름,시험날짜, 총진도율 바꿀 때)
    public void UpdateLicense(String _name, String _testday, Double _studyrate,String _beforename){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE licenselist SET name ='"+_name+"',testday ='"+_testday+"',studyrate ='"+_studyrate+"' WHERE name = '"+_beforename+"'");
    }

    //UPDATE 문 (공부 시간이 바뀌어 업데이트 하는 경우)
    public void UpdateLicenseStudyTime(String _name, String _studytime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE licenselist SET studytime ='"+_studytime+"' WHERE name = '"+_name+"'");
    }

    //DELETE 문
    public void deleteLicense(String _beforename){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM licenselist WHERE name ='" + _beforename+"'");
    }
}
