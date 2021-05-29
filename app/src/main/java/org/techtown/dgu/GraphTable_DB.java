package org.techtown.dgu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GraphTable_DB extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;

    public GraphTable_DB(@Nullable Context context, @Nullable String name) {
        super(context, name, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터 베이스가 생성 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값

        //name: 과목이름 , credit : 학점 , score : 성적,
        db.execSQL("CREATE TABLE IF NOT EXISTS GraphTable(RowID INTEGER NOT NULL, name TEXT NOT NULL, credit INTEGER NOT NULL, score TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //insert문 (graphtable 한줄을 입력한다.)
    public void InsertGraphTable(int _RowID,String _name ,int _credit,String _score){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO GraphTable VALUES('"+_RowID+"','"+_name+"','"+_credit+"','"+_score+"');");
    }

    //update문 (graphtable 한줄을 수정한다.)
    public void UpdateGraphTable(int _RowID, String _name ,int _credit,String _score){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE GraphTable SET name='"+_name+"', credit='"+_credit+"',score='"+_score+"' WHERE RowID = '"+_RowID+"'");

    }

    //delete문 (graphtable 한줄을 삭제한다.)
    public void DeleteGraphTable(int _RowID){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM GraphTable WHERE RowID ='"+_RowID+"'");
    }

    //delete로 인한 재정렬문
    public void UpdateGraphTable_RowID(){
        SQLiteDatabase db = getWritableDatabase();
        int NewRowID = 0;
        int OldRowID = 1;

        Cursor cursor = db.rawQuery("select count(*) from GraphTable",null);
        if(cursor.getCount()!=0){

            while (cursor.moveToNext()){
                UpdateGraphTable_RowIDChange(OldRowID,NewRowID);
                OldRowID++;
                NewRowID++;
            }
        }
    }

    //delete로 인한 재정렬문 안에 들어가는 함수 (graphtabledml RowID를 수정한다.)
    public void UpdateGraphTable_RowIDChange(int OldRowID, int NewRowID){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE GraphTable SET RowID='"+NewRowID+"' WHERE RowID = '"+OldRowID+"'");

    }

    //이미 존재하는 행인지 아닌지 판단. (존재하는 행이 있다면 true, 없다면 false)
    public boolean FindAlreadyExistsRowID(int _RowID){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GraphTable where RowID ='"+_RowID+"'",null);

        if(cursor.getCount()==0){
            //RowID ='"+_RowID+"'인게 없다면,
            return false;
        }else{return true;}
    }

    //table 내용 보여주기
    public void ViewGraphTable(EditText[] subject_name,EditText[] credit,TextView[] score){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GraphTable",null);

        //table 초기화
        for(int i=0;i<subject_name.length;i++){
            subject_name[i].setText("");
            credit[i].setText("");
            score[i].setText("");
        }

        int i=0;
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                subject_name[i].setText(cursor.getString(cursor.getColumnIndex("name")));
                credit[i].setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("credit"))));
                score[i].setText(cursor.getString(cursor.getColumnIndex("score")));
                i++;
            }

        }
    }

    //graphtable이 비어있는지 판단 ( true : 비어있음 , false : 비어있지 않음)
    public boolean IsEmpty(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GraphTable",null);

        if(cursor.getCount()!=0){return false;}
        else{return true;}
    }
}
