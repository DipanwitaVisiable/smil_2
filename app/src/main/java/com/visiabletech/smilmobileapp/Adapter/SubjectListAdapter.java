package com.visiabletech.smilmobileapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.visiabletech.smilmobileapp.Pogo.SubjectListInfo;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.activity.ChapterListActivity;

import java.util.ArrayList;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.PeriodViewHolder> {

    Context context;
    ArrayList<SubjectListInfo> receiveChapterList;
    public SubjectListAdapter(Context context, ArrayList<SubjectListInfo> receiveChapterList){

        this.context = context;
        this.receiveChapterList = receiveChapterList;

    }

    @NonNull
    @Override
    public SubjectListAdapter.PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_period_list_adapter, parent, false);
        return new PeriodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListAdapter.PeriodViewHolder holder, final int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(position);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(receiveChapterList.get(position).getSubject_name().charAt(0)), color);
        holder.iv_period_image.setImageDrawable(drawable);

//        holder.textView.setTextColor(Color.parseColor("#074f8f"));
        holder.tv_period_name.setText(receiveChapterList.get(position).getSubject_name());

        holder.cd_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Const.SUBJECT_ID=receiveChapterList.get(position).getId();
                Const.SUBJECT_NAME=receiveChapterList.get(position).getSubject_name();
                Intent intent = new Intent(context, ChapterListActivity.class);
                context.startActivity(intent);

                /*Bundle bundle = new Bundle();
                bundle.putString("days_id", receiveDaysList.get(position).getId());
                bundle.putString("period_id", receiveDaysList.get(position).getWeekdays());
                bundle.putString("period_name", receiveDaysList.get(position).getWeekdays());
                Intent intent = new Intent(context, LiveVideoListActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);*/


            }
        });

    }

    @Override
    public int getItemCount() {
        return receiveChapterList.size();
    }

    class PeriodViewHolder extends RecyclerView.ViewHolder{
        TextView tv_period_name;
        CardView cd_period;
        ImageView iv_period_image;

        public PeriodViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_period_name = itemView.findViewById(R.id.tv_period_name);
            iv_period_image = itemView.findViewById(R.id.iv_period_image);
            cd_period = itemView.findViewById(R.id.cd_period);
        }
    }
}
