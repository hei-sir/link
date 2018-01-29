package com.example.hei_sir.link;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by liuzi on 2018/1/29.
 * 这是教师端的qa页面
 */

public abstract class QaAdapter extends RecyclerView.Adapter<QaAdapter.ViewHolder>{
    private List<Qa> mQaList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView qaSname;
        TextView qaTname;
        ImageView qaImage;
        TextView qaTime;
        TextView qaContent;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            qaImage=(ImageView)itemView.findViewById(R.id.qa_image);
            qaSname=(TextView) itemView.findViewById(R.id.qa_name);
            qaContent=(TextView)itemView.findViewById(R.id.qa_content);
            qaTime=(TextView)itemView.findViewById(R.id.qa_time);
        }
    }

    public  QaAdapter(List<Qa> qaList){
        mQaList=qaList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_card_item,parent,false);
        QaAdapter.ViewHolder holder=new QaAdapter.ViewHolder(view);
            return holder;
    }

    @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            Qa qa=mQaList.get(position);
            holder.qaSname.setText(qa.getSname());
            holder.qaTime.setText(qa.getTime());
            holder.qaContent.setText(qa.getContent());
            holder.qaImage.setImageResource(qa.getImageId());
        }


    public int getItemCout(){
        return mQaList.size();
    }
}
