package org.techtown.dgu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.techtown.dgu.studysub;

import java.util.ArrayList;


public class Subject_DB extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String DB_NAME="subject.db";

    public ArrayList<studysub> getSubList(){
        ArrayList<studysub> subList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        //cursor = 해당 데이터 베이스에서 조건에 맞춘 값들을 저장하는 공간
        //ORDER BY : 정렬
        //DESC : 내림차순 , ASC : 오름차순
        Cursor cursor = db.rawQuery("SELECT * FROM Subject ",null);

        if(cursor.getCount()!=0){
            //cursor에 담긴 값이 있을 때 내부 수행

            while (cursor.moveToNext()){
                //cursor에 담긴 값이 없을 때 까지 내부 수행

                //cursor에 담긴 값을 옮기자.
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String subname = cursor.getString(cursor.getColumnIndex("subname"));
                String subtime = cursor.getString(cursor.getColumnIndex("subtime"));
                int week = cursor.getInt(cursor.getColumnIndex("week"));
                int weekFre = cursor.getInt(cursor.getColumnIndex("weekFre"));


                //새로 만든 subjectItem에 담자
                studysub subjectItem = new studysub();
                subjectItem.setId(id);
                subjectItem.setSubname(subname);
                subjectItem.setWeek(week);
                subjectItem.setWeekFre(weekFre);
                subjectItem.setSubtime(subtime);

                //todoItems에 todoItem추가
                subList.add(subjectItem);
            }
        }
        //cursor 다 쓰면 종료시키기
        cursor.close();

        return subList;
    }

    public Subject_DB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //데이터 베이스가 생성 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값

        //테이블을 생성한것같어..!
        db.execSQL("CREATE TABLE IF NOT EXISTS Subject(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subname TEXT NOT NULL,week INTEGER NOT NULL, weekFre INTEGER NOT NULL, subtime TEXT)");
    }

    public String[] getSubjectNameList(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select subname from Subject" ,null);
        String[] result = new String[cursor.getCount()];
        int i=0;
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                result[i]=cursor.getString(cursor.getColumnIndex("subname"));
                i++;
            }
        }
        cursor.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //insert문 (과목을 DB에 넣는다.)
    //사용자용 Insert, 처음 과목 등록 시
    public void InsetSubject(String _subname, Integer _week, Integer _weekFre){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Subject(subname, week, weekFre) VALUES('"+_subname+"','"+_week+"','"+_weekFre+"');");
    }

    //과목 공부 시간 추가
    public void InputSubjectTime(String _subname, String _subtime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Subject WHERE subname=_subname SET subtime='"+_subtime+"');");
    }

    //update문 (할일 목록을 수정한다.)
    public void UpdateTodo(String _subname, Integer _week, Integer _weekFre){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 업데이트 하고자 하는 행을 찾은 후 입력값을 이용해 수정.
        //id가 기준인 이유 => AUTOINCREMENT를 걸어놨기 때문. (통상적으로 이걸 걸어놓으면 기준으로 사용한다.)
        db.execSQL("UPDATE Subject SET  subname='"+_subname+"', week='"+_week+"',weekFre='"+_weekFre+"'");
    }

    //delete문 (과목을 삭제한다.)
    public void DeleteTodo(String _subname){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM Subject  WHERE subname ='"+_subname+"'");

    }



}