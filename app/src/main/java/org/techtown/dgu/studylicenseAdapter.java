package org.techtown.dgu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class studylicenseAdapter extends RecyclerView.Adapter<studylicenseAdapter.ViewHolder>{
    ArrayList<study_license> items = new ArrayList<study_license>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.license_item,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        study_license item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(study_license item){
        items.add(item);
    }

    public void setItems(ArrayList<study_license> items){
        this.items = items;
    }

    public study_license getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, study_license item){
        items.set(position,item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        ImageView startbutton;
        SeekBar progress;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.licensename);
            time = itemView.findViewById(R.id.licensetime);
            startbutton = itemView.findViewById(R.id.startbutton);
            progress = itemView.findViewById(R.id.progress);
        }

        public void setItem(study_license item){
            name.setText(item.getName());
            time.setText(item.getTime());
        }
    }

}
