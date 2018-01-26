package com.example.hei_sir.link;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hei_sir on 2018/1/10.
 */

public abstract class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<Info> mInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView infoUser;
        TextView infoData;

        public ViewHolder(View view){
            super(view);
            infoUser=(TextView) itemView.findViewById(R.id.info_user);
            infoData=(TextView) itemView.findViewById(R.id.info_data);
            

        }
    }

    public  InfoAdapter(List<Info> infoList){
        mInfoList=infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Info info=mInfoList.get(position);
        holder.infoUser.setText(info.getUser());
        holder.infoData.setText(info.getData());
    }


    public int getItemCout(){
        return mInfoList.size();
    }
}
