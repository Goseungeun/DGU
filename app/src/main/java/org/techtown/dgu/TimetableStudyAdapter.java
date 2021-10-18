package org.techtown.dgu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.studylicense.LicenseAdapter;
import org.techtown.dgu.studylicense.LicenseItem;

import java.util.ArrayList;

public class TimetableStudyAdapter extends RecyclerView.Adapter<TimetableStudyAdapter.ViewHolder>{

    private ArrayList<TimetableStudyitem> items;
    private Context mContext;

    public TimetableStudyAdapter(ArrayList<TimetableStudyitem> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TimetableStudyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.timetable_study_item,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimetableStudyitem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() { return items.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_category;
        TextView Tv_name;
        TextView Tv_studytime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Tv_category = itemView.findViewById(R.id.TimetableStudyItemCategory);
            Tv_name = itemView.findViewById(R.id.TimetableStudyItemName);
            Tv_studytime= itemView.findViewById(R.id.TimetableStudyItemStudyTime);

        }

        public void setItem(TimetableStudyitem item){
            Tv_category.setText(item.getCategory());
            Tv_name.setText(item.getName());
            Tv_studytime.setText(item.getStudytime());
        }
    }
}
