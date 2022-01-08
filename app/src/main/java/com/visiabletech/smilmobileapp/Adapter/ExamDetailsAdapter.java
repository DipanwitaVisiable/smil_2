package com.visiabletech.smilmobileapp.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.smilmobileapp.Pogo.ExamDetailsModel;
import com.visiabletech.smilmobileapp.R;

import java.util.List;

public class ExamDetailsAdapter extends RecyclerView.Adapter<ExamDetailsAdapter.ViewHolder> {


    private Context context;
    private List<ExamDetailsModel> mList;

    public ExamDetailsAdapter(Context context, List<ExamDetailsModel> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.custom_exam_details,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder rowViewHolder = (ViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0)
        {
            // Header Cells. Main Headings appear here
           /*rowViewHolder.tv_periods.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.tv_mon.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.tv_tue.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.tv_wed.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.tv_thu.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.tv_fri.setBackgroundResource(R.drawable.table_header_cell_bg);*/


            rowViewHolder.tv_subject.setBackgroundColor(Color.parseColor("#EC407A"));
            rowViewHolder.tv_exam.setBackgroundColor(Color.parseColor("#E91E63"));
            rowViewHolder.tv_time.setBackgroundColor(Color.parseColor("#D81B60"));
            rowViewHolder.tv_date.setBackgroundColor(Color.parseColor("#C2185B"));



            rowViewHolder.tv_subject.setText("SUBJECT");
            rowViewHolder.tv_exam.setText("EXAM");
            rowViewHolder.tv_time.setText("TIME");
            rowViewHolder.tv_date.setText("DATE");



            rowViewHolder.tv_subject.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_exam.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_time.setTextColor(Color.parseColor("#FFFFFF"));
            rowViewHolder.tv_date.setTextColor(Color.parseColor("#FFFFFF"));



        }

        else
        {

            ExamDetailsModel timeTableModel = mList.get(rowPos -1);


            rowViewHolder.tv_subject.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_exam.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_time.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.tv_date.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.tv_subject.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_exam.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_time.setTextColor(Color.parseColor("#F06292"));
            rowViewHolder.tv_date.setTextColor(Color.parseColor("#F06292"));


            rowViewHolder.tv_subject.setText(timeTableModel.getSubject());
            rowViewHolder.tv_exam.setText(timeTableModel.getExam());
            rowViewHolder.tv_time.setText(timeTableModel.getTime());
            rowViewHolder.tv_date.setText(timeTableModel.getDate());



        }



    }

    @Override
    public int getItemCount() {

        return mList.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_subject,tv_exam,tv_time,tv_date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_exam = itemView.findViewById(R.id.tv_exam);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_date = itemView.findViewById(R.id.tv_date);



        }
    }
}
