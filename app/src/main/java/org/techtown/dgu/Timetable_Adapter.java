package org.techtown.dgu;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Timetable_Adapter extends BaseAdapter {
    ArrayList<Timetable_Item> items=new ArrayList<Timetable_Item>();
    Context context;

    public void addItem(Timetable_Item item){
        items.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context=parent.getContext();


        // position에 해당하는 Timetable_Item
        Timetable_Item tt_item = items.get(position);


        //Timetable_item을 timetable_item layout과 연결한다.
        if(convertView==null){

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.timetable_item,parent,false);
        }


        //xml의 view를 참조
        View time=convertView.findViewById(R.id.id_time);
        if(tt_item.study){
            time.setBackgroundColor(context.getResources().getColor(R.color.green));}


        return convertView;
    }


    //BaseAdapter을 사용하기 위해 작성해야하는 코드
    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


}
