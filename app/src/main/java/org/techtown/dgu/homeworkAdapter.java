package org.techtown.dgu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class homeworkAdapter extends RecyclerView.Adapter<homeworkAdapter.homeworkViewHolder> {

    private ArrayList<homework> hwList;
    private Context mContext;

    public homeworkAdapter(Context context,ArrayList<homework> hw){
        this.hwList = hw;
        this.mContext = context;
    }

    public class homeworkViewHolder extends RecyclerView.ViewHolder{
        protected CheckBox homeworkname;
        protected TextView homeworkdday;

        public homeworkViewHolder(View view)
        {
            super(view);
            homeworkname = view.findViewById(R.id.homeworkname);
            homeworkdday = view.findViewById(R.id.homeworkdday);
        }
    }
    @NonNull
    @Override
    public homeworkViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hw_item,viewGroup,false);

        return new homeworkAdapter.homeworkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(homeworkViewHolder HomeworkviewHolder, int position) {
        HomeworkviewHolder.homeworkname.setText(hwList.get(position).getHwname());
        HomeworkviewHolder.homeworkdday.setText(hwList.get(position).getHwDday());

    }

    @Override
    public int getItemCount() {
        return hwList.size();
    }

}