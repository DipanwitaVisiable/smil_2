package com.visiabletech.smilmobileapp.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.smilmobileapp.BulletinsActivity;
import com.visiabletech.smilmobileapp.MainActivity;
import com.visiabletech.smilmobileapp.NoticeActivity;
import com.visiabletech.smilmobileapp.Pogo.NotificationModel;
import com.visiabletech.smilmobileapp.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private Context context;
    private ArrayList<NotificationModel> mList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.custom_notification,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final NotificationModel chamberModel=mList.get(position);

        holder.tv_name.setText(chamberModel.getNotification_type());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chamberModel.getNotification_type().equals("Notice")) {
                    context.startActivity(new Intent(context, NoticeActivity.class));
                }
                else if (chamberModel.getNotification_type().equals("bulletin")){
                    context.startActivity(new Intent(context, BulletinsActivity.class));
                }
                else {
                    Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.parseColor("#f12711"),Color.parseColor("#f5af19")});
        gd.setCornerRadius(0f);


        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.parseColor("#ee0979"),Color.parseColor("#ff6a00")});
        gd.setCornerRadius(0f);



        if(position %2 == 1)
        {
            //holder.lin.setBackgroundColor(Color.parseColor("#f12711"));
            holder.lin.setBackgroundDrawable(gd);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
           // holder.lin.setBackgroundColor(Color.parseColor("#f5af19"));
            holder.lin.setBackgroundDrawable(gd2);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }*/

    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv_name;

        CardView card_view;
        LinearLayout lin;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            lin = itemView.findViewById(R.id.lin);




        }
    }
}
