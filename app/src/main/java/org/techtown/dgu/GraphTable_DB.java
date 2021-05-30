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
    private static final String DB_NAME = "GraphTable_DB";
    private String[] tbName;

    public GraphTable_DB(@Nullable Context context, @Nullable String[] _tbName) {
        super(context, DB_NAME, null, DB_VERSION);
        this.tbName=_tbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터 베이스가 생성 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값
        //name: 과목이름 , credit : 학점 , score : 성적,
        for(int i=0 ; i<tbName.length;i++){
            db.execSQL("CREATE TABLE IF NOT EXISTS '"+tbName[i]+"'(RowID INTEGER NOT NULL, name TEXT NOT NULL, credit INTEGER NOT NULL, score TEXT NOT NULL)");
        }
    }


    //기존에 생성되어 있던 디비 오픈


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //insert문 (graphtable 한줄을 입력한다.)
    public void InsertGraphTable(String _tbname,int _RowID,String _name ,int _credit,String _score){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO '"+_tbname+"' VALUES('"+_RowID+"','"+_name+"','"+_credit+"','"+_score+"');");
    }

    //update문 (graphtable 한줄을 수정한다.)
    public void UpdateGraphTable(String _tbname,int _RowID, String _name ,int _credit,String _score){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE '"+_tbname+"' SET name='"+_name+"', credit='"+_credit+"',score='"+_score+"' WHERE RowID = '"+_RowID+"'");

    }

    //delete문 (graphtable 한줄을 삭제한다.)
    public void DeleteGraphTable(String _tbname,int _RowID){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM '"+_tbname+"' WHERE RowID ='"+_RowID+"'");
    }

    //delete로 인한 재정렬문
    public void UpdateGraphTable_RowID(String _tbname,int deletedRowID){
        SQLiteDatabase db = getWritableDatabase();
        int NewRowID = deletedRowID;
        int OldRowID = deletedRowID+1;

        Cursor cursor = db.rawQuery("select RowID from '"+_tbname+"' where RowID > '"+NewRowID+"'",null);
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                    UpdateGraphTable_RowIDChange(_tbname,OldRowID,NewRowID);
                    OldRowID++;
                    NewRowID++;
           }
        }
        cursor.close();
    }


    //delete로 인한 재정렬문 안에 들어가는 함수 (graphtabledml RowID를 수정한다.)
    public void UpdateGraphTable_RowIDChange(String _tbname,int OldRowID, int NewRowID){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE '"+_tbname+"' SET RowID='"+NewRowID+"' WHERE RowID = '"+OldRowID+"'");
    }

    //이미 존재하는 행인지 아닌지 판단. (존재하는 행이 있다면 true, 없다면 false)
    public boolean FindAlreadyExistsRowID(String _tbname,int _RowID){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '"+_tbname+"' where RowID ='"+_RowID+"'",null);

        if(cursor.getCount()==0){
            //RowID ='"+_RowID+"'인게 없다면,
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }
    }

    //table 내용 보여주기
    public void ViewGraphTable(String _tbname,EditText[] subject_name,EditText[] credit,TextView[] score){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '"+_tbname+"'",null);

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
        cursor.close();
    }

    //graphtable이 비어있는지 판단 ( true : 비어있음 , false : 비어있지 않음)
    public boolean IsEmpty(String _tbname){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '"+_tbname+"'",null);

        if(cursor.getCount()!=0){cursor.close();return false;}
        else{cursor.close();return true;}
    }

    //학기 평균 학점계산하기
    public double CalculateGPA(String _tbname){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT credit,score FROM '"+_tbname+"' where score !='NP' and score !='P' and score !='F'",null);

        double GPA=0.00;
        if(cursor.getCount()!=0){
            //table안에 내용이 들어있다면 내부수행
            //m_credit과 m_score을 곱한값들을 다 더해서 sum_credit으로 나눠준다.
            GPA=sum_Of_m_credit_times_m_score(_tbname)/sum_Of_m_credit(_tbname);
        }

        cursor.close();
        return GPA;
    }

    //m_credit과 m_score을 곱한값들을 다 더해서
    public double sum_Of_m_credit(String _tbname) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT credit,score FROM '"+_tbname+"' where score !='NP' and score !='P' and score !='F'",null);
        double m_credit;
        double result=0.0;
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                m_credit= cursor.getInt(cursor.getColumnIndex("credit"));
                result += m_credit;
            }
        }
        cursor.close();
        return result;
    }

    //sum_credit으로 나눠준다.
    public double sum_Of_m_credit_times_m_score(String _tbname) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT credit,score FROM '"+_tbname+"' where score !='NP' and score !='P' and score !='F'",null);
        double m_credit, m_score;
        double result=0.0;
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                m_credit= cursor.getInt(cursor.getColumnIndex("credit"));
                m_score= m_score_Calculate(cursor.getString(cursor.getColumnIndex("score")));
                result += m_credit*m_score;
            }
        }
        cursor.close();
        return result;
    }

    private double m_score_Calculate(String score) {
        //score의 NP, P, F는 0.0점으로 계산.
        switch(score){
            case "A+" :
                return 4.5;
            case "A0" :
                return 4.0;
            case "B+" :
                return 3.5;
            case "B0" :
                return 3.0;
            case "C+" :
                return 2.5;
            case "C0" :
                return 2.0;
            case "D+" :
                return 1.5;
            default :
                return 1.0;     //D0
        }
    }
}
