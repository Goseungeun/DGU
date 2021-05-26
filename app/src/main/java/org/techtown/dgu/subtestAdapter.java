package org.techtown.dgu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class subtestAdapter extends RecyclerView.Adapter<subtestAdapter.subtestViewHolder>{

    private ArrayList<subtest> stList;
    private Context mContext;

    public subtestAdapter(Context context,ArrayList<subtest> st)
    {
        this.stList = st;
        this.mContext = context;
    }

    public class subtestViewHolder extends RecyclerView.ViewHolder{
        protected TextView subtestname;
        protected TextView subtestdday;

        public subtestViewHolder(View view){
            super(view);
            subtestname = view.findViewById(R.id.subtestname);
            subtestdday = view.findViewById(R.id.subtestdday);
        }
    }

    @NonNull
    @Override
    public subtestViewHolder onCreateViewHolder(ViewGroup viewGroup,int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_item,viewGroup, false);

        return new subtestAdapter.subtestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(subtestViewHolder SubtestviewHolder,int position)
    {
        SubtestviewHolder.subtestname.setText(stList.get(position).getSubtestname());
        SubtestviewHolder.subtestdday.setText(stList.get(position).getTestDday());
    }

    @Override
    public int getItemCount() {
        return stList.size();
    }
}
