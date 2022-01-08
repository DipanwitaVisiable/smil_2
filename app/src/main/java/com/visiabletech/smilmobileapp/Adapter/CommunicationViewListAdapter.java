package com.visiabletech.smilmobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.smilmobileapp.Pogo.ViewRequestInfo;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Helper;

import java.util.ArrayList;

public class CommunicationViewListAdapter extends RecyclerView.Adapter<CommunicationViewListAdapter.DaysViewHolder> {

    Context context;
    ArrayList<ViewRequestInfo> receiveCommunicationList;

    public CommunicationViewListAdapter(Context context, ArrayList<ViewRequestInfo> receiveCommunicationList){
        this.context = context;
        this.receiveCommunicationList = receiveCommunicationList;

    }

    @NonNull
    @Override
    public CommunicationViewListAdapter.DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_communication_view_adapter, parent, false);
        return new DaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunicationViewListAdapter.DaysViewHolder holder, final int position) {

        ViewRequestInfo mList=receiveCommunicationList.get(position);
        String status="";
        if (mList.getStatus().equalsIgnoreCase("1")){
            status = "Approved";
        }else {
            status = "Not Approved";
        }

        String from_date= Helper.parseDateToddMMyyyy(mList.getStart_date());
        String to_date= Helper.parseDateToddMMyyyy(mList.getEnd_date());


        holder.tv_subject.setText(mList.getSubject());
        holder.tv_type.setText(mList.getType());
        holder.tv_message.setText(mList.getMessage());
        holder.tv_from_date.setText(from_date);
        holder.tv_to_date.setText(to_date);
        holder.tv_approved.setText(status);



    }

    @Override
    public int getItemCount() {
        return receiveCommunicationList.size();
    }

    class DaysViewHolder extends RecyclerView.ViewHolder{
//        TextView textView;
        TextView tv_subject, tv_type, tv_message, tv_from_date, tv_to_date, tv_approved;

        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.textView);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_from_date = itemView.findViewById(R.id.tv_from_date);
            tv_to_date = itemView.findViewById(R.id.tv_to_date);
            tv_approved = itemView.findViewById(R.id.tv_approved);

        }
    }
}
