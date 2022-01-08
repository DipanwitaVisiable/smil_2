package com.visiabletech.smilmobileapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.smilmobileapp.Pogo.ClassWorkModel;
import com.visiabletech.smilmobileapp.R;

import java.util.ArrayList;

public class ClassWorkAdapter extends RecyclerView.Adapter<ClassWorkAdapter.ViewHolder> {


    private Context context;
    private ArrayList<ClassWorkModel> mList;

    public ClassWorkAdapter(Context context, ArrayList<ClassWorkModel> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.custom_class_work,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ClassWorkModel classWorkModel=mList.get(position);

        holder.tv_subject.setText(classWorkModel.getNotice_name());

    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_subject;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_subject = itemView.findViewById(R.id.tv_subject);


        }
    }
}
