package com.example.hei_sir.link.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hei_sir.link.Exam;
import com.example.hei_sir.link.Exam2Activity;
import com.example.hei_sir.link.R;

import java.util.List;

public abstract class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{
    private List<Exam> mExamList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView examexam;
        TextView examrank;
        TextView examtime;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            examexam=(TextView)itemView.findViewById(R.id.exam_exam);
            examrank=(TextView) itemView.findViewById(R.id.exam_rank);
            examtime=(TextView)itemView.findViewById(R.id.exam_time);
        }
    }

    public  ExamAdapter(List<Exam> examList){
        mExamList=examList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Exam exam=mExamList.get(position);
                Intent intent=new Intent(mContext,Exam2Activity.class);
                intent.putExtra(Exam2Activity.EXAMNAME,exam.getName());
                intent.putExtra(Exam2Activity.EXAMEXAM,exam.getExamId());
                intent.putExtra(Exam2Activity.EXAMRANK,exam.getRank());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Exam exam=mExamList.get(position);
        holder.examexam.setText(exam.getExamId());
        holder.examrank.setText(exam.getRank());
        holder.examtime.setText(exam.getTime());
    }


    public int getItemCout(){
        return mExamList.size();
    }
}
