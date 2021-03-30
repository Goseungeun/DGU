package org.techtown.dgu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Timetable_hour_Adapter extends BaseAdapter {

    ArrayList<timetable_hour_Item> items=new ArrayList<timetable_hour_Item>();
    Context context;

    public void addItem(timetable_hour_Item item){
        items.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context=parent.getContext();

        // position에 해당하는 Timetable_hour_Item
        timetable_hour_Item tt_item = items.get(position);

        //timetable_hour_Item을 inflate하고 convertView를 참조한다.
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.timetable_hour_item,parent,false);
        }

        //xml의 textview를 참조
        TextView hourtext=convertView.findViewById(R.id.id_hourname);

        //!!!디비 연결하면 여기 수정해야함
        hourtext.setText(""+tt_item.getHour()+"시");

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