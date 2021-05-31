package org.techtown.dgu;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import androidx.annotation.Nullable;

        import org.techtown.dgu.studysub;

        import java.util.ArrayList;


public class homework_DB extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String DB_NAME="homework.db";

    public ArrayList<homework> getHomeworkList(){
        ArrayList<homework> homeworkList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        //cursor = 해당 데이터 베이스에서 조건에 맞춘 값들을 저장하는 공간
        //ORDER BY : 정렬
        //DESC : 내림차순 , ASC : 오름차순
        Cursor cursor = db.rawQuery("SELECT * FROM homework",null);

        if(cursor.getCount()!=0){
            //cursor에 담긴 값이 있을 때 내부 수행

            while (cursor.moveToNext()){
                //cursor에 담긴 값이 없을 때 까지 내부 수행

                //cursor에 담긴 값을 옮기자.
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String hwname = cursor.getString(cursor.getColumnIndex("hwname"));
                String hwDday = cursor.getString(cursor.getColumnIndex("hwDday"));


                //새로 만든 subjectItem에 담자
                homework homeworkItem = new homework();
                homeworkItem.setId(id);
                homeworkItem.setHwname(hwname);
                homeworkItem.setHwDday(hwDday);

                //todoItems에 todoItem추가
                homeworkList.add(homeworkItem);
            }
        }
        //cursor 다 쓰면 종료시키기
        cursor.close();

        return homeworkList;
    }

    public homework_DB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //데이터 베이스가 생성 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값

        //테이블을 생성한것같어..!
        db.execSQL("CREATE TABLE IF NOT EXISTS homework(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "hwname TEXT NOT NULL, hwDday TEXT NOT NULL)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //insert문 (과목을 DB에 넣는다.)
    //사용자용 Insert, 처음 과목 등록 시
    public void InsetHomework(String _hwname, String _hwDday){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO homework(hwname, hwDday) VALUES('"+_hwname+"','"+_hwDday+"');");
    }


    //update문 (할일 목록을 수정한다.)
    public void UpdateTodo(Integer _id,String _hwname, String _hwDday){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 업데이트 하고자 하는 행을 찾은 후 입력값을 이용해 수정.
        //id가 기준인 이유 => AUTOINCREMENT를 걸어놨기 때문. (통상적으로 이걸 걸어놓으면 기준으로 사용한다.)
        db.execSQL("UPDATE homework SET  hwname='"+_hwname+"', hwDday='"+_hwDday+"'WHERE id ='"+_id+"'");
    }

    //delete문 (과목을 삭제한다.)
    public void DeleteTodo(Integer _id){
        SQLiteDatabase db = getWritableDatabase();

        //id를 기준으로 삭제하고자 하는 행을 찾은 후 삭제
        db.execSQL("DELETE FROM homework  WHERE id ='"+_id+"'");

    }



}