package org.techtown.dgu.mylicense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.R;

import java.util.ArrayList;

public class MyLicenseAdapter extends RecyclerView.Adapter<MyLicenseAdapter.ViewHolder> {
    ArrayList<MyLicenseItem> items = new ArrayList<MyLicenseItem>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mylicense_item,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MyLicenseItem item = items.get(position);
        viewHolder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MyLicenseItem item){
        items.add(item);
    }

    public void setItems(ArrayList<MyLicenseItem> items){
        this.items = items;
    }

    public MyLicenseItem getItem(int position){
        return items.get(position);
    }

    public ArrayList<MyLicenseItem> getItems() {
        return items;
    }

    public void setItem(int position, MyLicenseItem item){
        items.set(position,item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView licensename;
        TextView getyear;
        TextView getmonth;
        TextView getdate;
        TextView endyear;
        TextView endmonth;
        TextView enddate;

        public ViewHolder(View itemView) {
            super(itemView);

            licensename = itemView.findViewById(R.id.mylicensename);
            getyear = itemView.findViewById(R.id.getyear);
            getmonth = itemView.findViewById(R.id.getmonth);
            getdate = itemView.findViewById(R.id.getdate);
            endyear = itemView.findViewById(R.id.endyear);
            endmonth = itemView.findViewById(R.id.endmonth);
            enddate = itemView.findViewById(R.id.enddate);
        }

        public void setItem(MyLicenseItem item){
            licensename.setText(item.getMylicensename());
            getyear.setText(item.getGetyear());
            getmonth.setText(item.getGetmonth());
            getmonth.setText(item.getGetmonth());
            getdate.setText(item.getGetdate());
            endyear.setText(item.getEndyear());
            endmonth.setText(item.getEndmonth());
            enddate.setText(item.getEnddate());


        }

    }
}
