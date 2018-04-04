package com.example.hei_sir.link;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liuzi on 2018/2/6.
 */

public abstract class Qa1sAdapter extends RecyclerView.Adapter<Qa1sAdapter.ViewHolder> {
    private List<Qa> mQaList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView qaSname;
        TextView qaTname;
        TextView qaAnswer;
        ImageView qaImage;
        TextView qaTime;
        TextView qaContent;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            qaImage=(ImageView)itemView.findViewById(R.id.qa_image);
            qaSname=(TextView) itemView.findViewById(R.id.qa_name);
            qaContent=(TextView)itemView.findViewById(R.id.qa_content);
            qaAnswer=(TextView)itemView.findViewById(R.id.qa_answer);
            qaTime=(TextView)itemView.findViewById(R.id.qa_time);
        }
    }
    public  Qa1sAdapter(List<Qa> qaList){
        mQaList=qaList;
    }

    @Override
    public Qa1sAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_card_item,parent,false);
        final Qa1sAdapter.ViewHolder holder=new Qa1sAdapter.ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Qa1sAdapter.ViewHolder holder, int position){
        Qa qa=mQaList.get(position);
        holder.qaSname.setText(qa.getSname());
        holder.qaTime.setText(qa.getTime());
        holder.qaContent.setText(qa.getContent());
        holder.qaAnswer.setText(qa.getAnswer());
        holder.qaImage.setImageResource(qa.getImageId());
    }


    public int getItemCout(){
        return mQaList.size();
    }
}
