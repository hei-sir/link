package com.example.hei_sir.link;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
 * Created by liuzi on 2018/2/22.
 */

public abstract class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {
    private List<Zone> mZoneList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView zoneName;
        ImageView zoneImage;
        TextView zoneTime;
        TextView zoneContent;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            zoneImage=(ImageView)itemView.findViewById(R.id.zone_image2);
            zoneName=(TextView) itemView.findViewById(R.id.zone_name);
            zoneContent=(TextView)itemView.findViewById(R.id.zone_content);
            zoneTime=(TextView)itemView.findViewById(R.id.zone_time);
        }
    }
    public ZoneAdapter(List<Zone> zoneList) {mZoneList=zoneList;}

    @Override
    public ZoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_card_item,parent,false);
        final ZoneAdapter.ViewHolder holder=new ZoneAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Zone zone=mZoneList.get(position);
                /*Intent intent=new Intent(mContext,Qa2tActivity.class);
                intent.putExtra(Qa2tActivity.QA_NAME,qa.getSname());
                intent.putExtra(Qa2tActivity.QA_CONTENT,qa.getContent());
                intent.putExtra(Qa2tActivity.QA_ANSWER,qa.getAnswer());
                Log.d("QaAdapter",qa.getSname());
                Log.d("QaAdapter",qa.getContent());
                mContext.startActivity(intent);*/
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Zone zone=mZoneList.get(position);
        holder.zoneName.setText(zone.getName());
        holder.zoneTime.setText(zone.getTime());
        holder.zoneContent.setText(zone.getContent());
        if (zone.getImagePath().equals("one")){
        }else {
            holder.zoneImage.setImageBitmap(BitmapFactory.decodeFile(zone.getImagePath()));
        }
    }


    @Override
    public int getItemCount() {
     return   mZoneList.size();
    }
}
