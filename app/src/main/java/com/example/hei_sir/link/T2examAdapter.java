package com.example.hei_sir.link;

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

import java.util.List;

/**
 * Created by liuzi on 2018/3/14.
 */

public abstract class T2examAdapter extends RecyclerView.Adapter<T2examAdapter.ViewHolder> {
    private List<Yuwen> mYuwenList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView examUser;
        TextView examExam;
        TextView examTime;

        public ViewHolder(View view) {
            super(view);
            examUser = (TextView) itemView.findViewById(R.id.exam_user);
            examExam = (TextView) itemView.findViewById(R.id.exam_exam);
            examTime = (TextView) itemView.findViewById(R.id.exam_time);


        }
    }

    public T2examAdapter(List<Yuwen> yuwenList) {
        mYuwenList = yuwenList;
    }

    @Override
    public T2examAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(T2examAdapter.ViewHolder holder, int position) {
        Yuwen yuwen = mYuwenList.get(position);
        holder.examUser.setText(yuwen.getUser());
        holder.examExam.setText(yuwen.getExam());
        holder.examTime.setText(yuwen.getTime());
    }


    public int getItemCout() {
        return mYuwenList.size();
    }
}
