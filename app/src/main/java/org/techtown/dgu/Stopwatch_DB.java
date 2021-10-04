package org.techtown.dgu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Stopwatch_DB extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String DB_NAME = "Stopwatch_DB";

    public Stopwatch_DB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터 베이스가 생성 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값

        String DefaultTime = "00:00:00";
        //일간 총 공부시간 테이블
        db.execSQL("CREATE TABLE IF NOT EXISTS TodayTotalStudyTime (date TEXT NOT NULL,  studytime TEXT default'"+DefaultTime+"' )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //TotalStudyTime Table(하루 총 공부시간 저장용)과 관련된 함수들
    public void InsertTotalStudyTime(String _date, String _studytime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodayTotalStudyTime VALUES('"+_date+"','"+_studytime+"');");
    }

    public void UpdateTotalStudyTime(String _date, String _studytime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodayTotalStudyTime SET studytime='"+_studytime+"' WHERE date = '"+_date+"'" );
    }

    public void DeleteTotalStudyTime(String _date, String _studytime){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM TodayTotalStudyTime WHERE date = '"+_date+"'");
    }

    //이미 존재하는 행인지 아닌지 판단. (존재하는 행이 있다면 true, 없다면 false)
    public String IsExist(String _date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodayTotalStudyTime where date ='"+_date+"'",null);

        cursor.moveToNext();
        if(cursor.getCount()==0){
            //date ='"+_date+"'인게 없다면,
            cursor.close();
            return null;
        }else{
            String result = cursor.getString(cursor.getColumnIndex("studytime"));
            cursor.close();
            return result;
        }
    }

    //StopwatchToday와 연결
    public String getStudyTime(String _date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodayTotalStudyTime where date ='"+_date+"'",null);
        String result = "00:00:00";
        cursor.moveToNext();
        if(cursor.getCount()!=0){
            result = cursor.getString(cursor.getColumnIndex("studytime"));
        }
        cursor.close();
        return result;
    }

    //DB에 저장된 마지막 날짜와 현재 날짜 비교를 위한 함수/ Stopwatch.java와 연결예정
    public String getLastDate(){
        //DB에 저장된 날짜가 없으면 null, 있ㅇ면 해당 날짜 출력
        String result=null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodayTotalStudyTime",null);
        while(cursor.moveToLast()){
            result = cursor.getString(cursor.getColumnIndex("studytime"));
        }
        cursor.close();
        return result;
    }


}
