package com.visiabletech.smilmobileapp.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.smilmobileapp.Pogo.DaysListInfo;
import com.visiabletech.smilmobileapp.Pogo.OnlineTestInfo;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.Helper;
import com.visiabletech.smilmobileapp.activity.OnlineTestActivity;
import com.visiabletech.smilmobileapp.activity.OnlineTestResultActivity;
import com.visiabletech.smilmobileapp.activity.PeriodListActivity;
import com.visiabletech.smilmobileapp.activity.SmallQuizTestActivity;

import java.util.ArrayList;

public class OnlineTestListAdapter extends RecyclerView.Adapter<OnlineTestListAdapter.OnlineTestViewHolder> {

    Context context;
    ArrayList<OnlineTestInfo> receiveTestList;
    public OnlineTestListAdapter(Context context, ArrayList<OnlineTestInfo> receiveTestList){

        this.context = context;
        this.receiveTestList = receiveTestList;

    }

    @NonNull
    @Override
    public OnlineTestListAdapter.OnlineTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_of_small_quiz_list_adapter, parent, false);
        return new OnlineTestListAdapter.OnlineTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OnlineTestListAdapter.OnlineTestViewHolder holder, final int position) {

        //*****Start****//
        holder.tv_quiz_name.setText(receiveTestList.get(position).getExam_name());
        holder.tv_time.setText("Time: " +receiveTestList.get(position).getTot_time()+ " mins");
        holder.tv_total_question.setText("Total Question: " +receiveTestList.get(position).getTot_qus());
        holder.tv_subject_name.setText("Subject Name: " +receiveTestList.get(position).getSubject_name());
        holder.tv_chapter_name.setText("Chapter Name: " +receiveTestList.get(position).getChapter_name());
        holder.btn_start_test.setText("  "+receiveTestList.get(position).getStatus_text()+ "  ");
        String date_of_exam= Helper.parseDateToddMMyyyy(receiveTestList.get(position).getExam_date());
        holder.tv_test_date.setText("Exam Date: " + date_of_exam);

        holder.btn_start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.btn_start_test.getText().toString().trim().equals("Start Test")) {
                    //Start functionality
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.quiz_dialog_view);
                    dialog.setCancelable(true);
                    Button noB = dialog.findViewById(R.id.noButton);
                    Button yesB = dialog.findViewById(R.id.yesButton);
                    TextView textViewTopic = dialog.findViewById(R.id.testTopic);
                    TextView questions = dialog.findViewById(R.id.testQuestions);
                    TextView duration = dialog.findViewById(R.id.duration);
                    textViewTopic.setText(receiveTestList.get(position).getExam_name());
                    questions.setText(receiveTestList.get(position).getTot_qus());
                    duration.setText(receiveTestList.get(position).getTot_time() + " min");
                    noB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    yesB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(context, SmallQuizTestActivity.class);
                            intent.putExtra("quiz_id", receiveTestList.get(position).getExam_id());
                            intent.putExtra("time", receiveTestList.get(position).getTot_time());
                            context.startActivity(intent);
                        }
                    });
                    dialog.show();
                }
                    //End functionality
                else if (holder.btn_start_test.getText().toString().trim().equals("View Result"))
                {
                    Intent intent = new Intent(context, OnlineTestResultActivity.class);
                    intent.putExtra("exam_taken_id", receiveTestList.get(position).getExam_taken_id());
                    intent.putExtra("exam_id", receiveTestList.get(position).getExam_id());
                    intent.putExtra("tot_qus", receiveTestList.get(position).getTot_qus());
                    context.startActivity(intent);
                }

                /*Intent intent = new Intent(context, OnlineTestResultActivity.class);
                intent.putExtra("exam_taken_id", receiveTestList.get(position).getExam_taken_id());
                intent.putExtra("tot_qus", receiveTestList.get(position).getTot_qus());
                context.startActivity(intent);*/
            }
        });


        //*****End****//

    }

    @Override
    public int getItemCount() {
        return receiveTestList.size();
    }

    class OnlineTestViewHolder extends RecyclerView.ViewHolder{
        TextView tv_quiz_name, tv_time, tv_total_question, tv_subject_name, tv_chapter_name, tv_test_date;
        Button btn_view_score, btn_start_test;

        public OnlineTestViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_quiz_name = itemView.findViewById(R.id.tv_quiz_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_total_question = itemView.findViewById(R.id.tv_total_question);
            tv_subject_name = itemView.findViewById(R.id.tv_subject_name);
            tv_chapter_name = itemView.findViewById(R.id.tv_chapter_name);
            tv_test_date = itemView.findViewById(R.id.tv_test_date);
//            btn_view_score = itemView.findViewById(R.id.btn_view_score);
            btn_start_test = itemView.findViewById(R.id.btn_start_test);
        }
    }
}
