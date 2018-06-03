package com.example.hei_sir.link;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Random;

public abstract class ContacttAdapter extends RecyclerView.Adapter<ContacttAdapter.ViewHolder> {
    private List<User> mUserList;
    private Context mContext;
    private int red,green,blue;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView phone;
        ImageView phonename;
        TextView name;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)itemView.findViewById(R.id.card_view);
            phonename=(ImageView)itemView.findViewById(R.id.phone_name);
            name=(TextView) itemView.findViewById(R.id.name);
            phone=(TextView)itemView.findViewById(R.id.phone);
        }
    }
    public ContacttAdapter(List<User> userList) {mUserList=userList;}

    @Override
    public ContacttAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item,parent,false);
        final ContacttAdapter.ViewHolder holder=new ContacttAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                User user=mUserList.get(position);
                Intent intent=new Intent(mContext,Contact2Activity.class);
                intent.putExtra(Contact2Activity.PHONE,user.getPhone());
                intent.putExtra(Contact2Activity.NAME,user.getName());
                intent.putExtra(Contact2Activity.RED,red);
                intent.putExtra(Contact2Activity.GREEN,green);
                intent.putExtra(Contact2Activity.BLUE,blue);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        User user=mUserList.get(position);
        Cursor cursor=DataSupport.findBySQL("select * from User where name = ? and phone = ?",user.getName(),user.getPhone());
        cursor.moveToFirst();
        if (cursor.getString(cursor.getColumnIndex("identity")).equals("学生")) {
            holder.name.setText(user.getName() + "  的家长");
        }else {
            holder.name.setText(user.getName() + "  老师");
        }
        holder.phone.setText(user.getPhone());
        Random x= new Random();
        Random y= new Random();
        Random z= new Random();
        red=x.nextInt(256);
        green=y.nextInt(256);
        blue=z.nextInt(256);
        holder.phonename.setBackgroundColor(Color.rgb(red,green,blue));
    }


    @Override
    public int getItemCount() {
        return   mUserList.size();
    }
}
