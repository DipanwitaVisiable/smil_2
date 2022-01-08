package com.visiabletech.smilmobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.visiabletech.smilmobileapp.EditProfileActivity;
import com.visiabletech.smilmobileapp.Pogo.CustomSpinnerSubjectGetSet;
import com.visiabletech.smilmobileapp.R;

import java.util.ArrayList;

public class CustomSpinnerSubjectAdapter extends ArrayAdapter<CustomSpinnerSubjectGetSet> {
    /*private ArrayList<CustomSpinnerSubjectGetSet> arrayList=new ArrayList<>();
    private ArrayList<CustomSpinnerSubjectGetSet> receiveList;*/

    public CustomSpinnerSubjectAdapter(@NonNull Context context, ArrayList<CustomSpinnerSubjectGetSet> customSpinnerItems) {
        super(context, 0, customSpinnerItems);
//        this.receiveList=customSpinnerItems;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout,parent,false);
        }

        CustomSpinnerSubjectGetSet item=getItem(position);

        TextView spinnerTV= convertView.findViewById(R.id.tv_spinner_layout);

        if (item !=null) {
            spinnerTV.setText(item.getSubject());
        }

        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
        {

            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);
        }

        CustomSpinnerSubjectGetSet item=getItem(position);

        TextView dropDawnTV= convertView.findViewById(R.id.tv_drop_dawn);

        if (item !=null) {
            dropDawnTV.setText(item.getSubject());
        }

        return convertView;
    }
}
