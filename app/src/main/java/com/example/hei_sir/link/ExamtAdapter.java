package com.example.hei_sir.link;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hei_sir.link.helper.ExamAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

public abstract class ExamtAdapter extends RecyclerView.Adapter<ExamtAdapter.ViewHolder>{
    private List<Exam> mExamList;
    private Context mContext;
    private int i;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView examexam;
        TextView examcount;
        TextView examtime;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            examexam=(TextView)itemView.findViewById(R.id.exam_exam);
            examcount=(TextView) itemView.findViewById(R.id.exam_count);
            examtime=(TextView)itemView.findViewById(R.id.exam_time);
        }
    }

    public  ExamtAdapter(List<Exam> examList){
        mExamList=examList;
    }

    @Override
    public ExamtAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.examt_item,parent,false);
        final ExamtAdapter.ViewHolder holder=new ExamtAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Exam exam=mExamList.get(position);
                Intent intent=new Intent(mContext,Exam2tActivity.class);
                intent.putExtra(Exam2tActivity.EXAMEXAM,exam.getExamId());
                intent.putExtra(Exam2tActivity.EXAMCOUNT,String.valueOf(i));
                intent.putExtra(Exam2tActivity.EXAMNAME,exam.getName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ExamtAdapter.ViewHolder holder, int position){
        Exam exam=mExamList.get(position);
        holder.examexam.setText(exam.getExamId());
        List<Exam> exams = DataSupport.where("examId=?",exam.getExamId()).find(Exam.class);
        i=0;
        for (Exam exam1 : exams) {
            i++;
        }
        holder.examcount.setText(String.valueOf(i));
        holder.examtime.setText(exam.getTime());
    }



    public int getItemCout(){
        return mExamList.size();
    }
}
