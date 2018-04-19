package com.example.hei_sir.link;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public abstract class Exam2tAdapter extends RecyclerView.Adapter<Exam2tAdapter.ViewHolder>{
    private List<Exam> mExamList;
    private Context mContext;
    private int i;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name,score,rank,chinese,math,english,politics,physics,chemical;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            name=(TextView)itemView.findViewById(R.id.exam_name);
            score=(TextView) itemView.findViewById(R.id.exam_score);
            math=(TextView)itemView.findViewById(R.id.exam_math);
            english=(TextView)itemView.findViewById(R.id.exam_english);
            politics=(TextView)itemView.findViewById(R.id.exam_politics);
            physics=(TextView)itemView.findViewById(R.id.exam_physics);
            chinese=(TextView)itemView.findViewById(R.id.exam_chinese);
            chemical=(TextView)itemView.findViewById(R.id.exam_chemical);
            rank=(TextView)itemView.findViewById(R.id.exam_rank);
        }
    }

    public  Exam2tAdapter(List<Exam> examList){
        mExamList=examList;
    }

    @Override
    public Exam2tAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_class_item,parent,false);
        final Exam2tAdapter.ViewHolder holder=new Exam2tAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Exam exam=mExamList.get(position);
//                Intent intent=new Intent(mContext,Exam2tActivity.class);
//                intent.putExtra(Exam2tActivity.EXAMEXAM,exam.getExamId());
//                intent.putExtra(Exam2tActivity.EXAMCOUNT,String.valueOf(i));
//                intent.putExtra(Exam2tActivity.EXAMNAME,exam.getName());
//                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Exam2tAdapter.ViewHolder holder, int position){
        Exam exam=mExamList.get(position);
        holder.name.setText(exam.getName());
        holder.score.setText(String.valueOf(exam.getScore()));
        holder.chinese.setText(String.valueOf(exam.getChinese()));
        holder.math.setText(String.valueOf(exam.getMath()));
        holder.english.setText(String.valueOf(exam.getEnglish()));
        holder.politics.setText(String.valueOf(exam.getPolitics()));
        holder.physics.setText(String.valueOf(exam.getPhysics()));
        holder.chemical.setText(String.valueOf(exam.getChemical()));
        holder.rank.setText(exam.getRank());

    }



    public int getItemCout(){
        return mExamList.size();
    }
}
