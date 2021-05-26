package org.techtown.dgu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class studysubAdapter extends RecyclerView.Adapter<studysubAdapter.studysubViewHolder>{

    private ArrayList<studysub> subList;
    private Context mContext;

    public studysubAdapter(Context context, ArrayList<studysub> subList){
        this.subList = subList;
        this.mContext = context;
    }

    @Override
    public studysubViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_item,viewGroup,false);
        studysubViewHolder sh = new studysubViewHolder(v);
        return sh;
    }

    @Override
    public void onBindViewHolder(studysubViewHolder StudysubviewHolder, int i){
        final String subname = subList.get(i).getSubname();
        final String subtime = subList.get(i).getSubtime();
        ArrayList hwList = subList.get(i).getHwList();
        ArrayList testList = subList.get(i).getTestList();
        StudysubviewHolder.subjectname.setText(subname);
        StudysubviewHolder.subjecttime.setText(subtime);
        homeworkAdapter homeworkListDataAdapter = new homeworkAdapter(mContext, hwList);
        subtestAdapter subjecttestListDataAdapter = new subtestAdapter(mContext,testList);
        StudysubviewHolder.hwrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.hwrecycler.setAdapter(homeworkListDataAdapter);
        StudysubviewHolder.testrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.testrecycler.setAdapter(subjecttestListDataAdapter);
    }
    @Override
    public int getItemCount(){
        return (null!=subList?subList.size():0);
    }

    public class studysubViewHolder extends RecyclerView.ViewHolder{
        protected ImageButton image;
        protected TextView subjectname;
        protected TextView subjecttime;
        protected TextView addhw;
        protected RecyclerView hwrecycler;
        protected TextView addtest;
        protected RecyclerView testrecycler;

        public studysubViewHolder(View view){
            super(view);
            this.image = (ImageButton)view.findViewById(R.id.imageButton);
            this.subjectname = (TextView)view.findViewById(R.id.subjectname);
            this.subjecttime = (TextView)view.findViewById(R.id.subjecttime);
            this.addhw = (TextView)view.findViewById(R.id.addhw);
            this.hwrecycler = (RecyclerView)view.findViewById(R.id.hwrecycler);
            this.addtest = (TextView)view.findViewById(R.id.addtest);
            this.testrecycler = (RecyclerView)view.findViewById(R.id.testrecycler);
        }
    }
}