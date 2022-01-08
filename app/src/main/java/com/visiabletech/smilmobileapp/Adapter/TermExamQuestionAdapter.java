package com.visiabletech.smilmobileapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.RequestQueue;
import com.visiabletech.smilmobileapp.Pogo.AnswerTable;
import com.visiabletech.smilmobileapp.Pogo.QuestionTable;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;

import java.util.ArrayList;
import java.util.HashMap;

import at.blogc.android.views.ExpandableTextView;

public class TermExamQuestionAdapter extends PagerAdapter {
  Context context;
  private ArrayList<QuestionTable> quesList;
  private LayoutInflater inflater;
  private HashMap<String, ArrayList<AnswerTable>> listHashMap;
  private Button submitButton;
  private String exam_ques;
  private RequestQueue requestQueue;

  public TermExamQuestionAdapter(Context context, ArrayList<QuestionTable> quesList,
                                 HashMap<String, ArrayList<AnswerTable>> listHashMap, Button submitButton) {
    this.context = context;
    this.quesList = quesList;
    this.listHashMap = listHashMap;
    this.submitButton = submitButton;
    inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return listHashMap.size();
  }


  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view.equals(object);
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getItemPosition(@NonNull Object object) {

    return POSITION_NONE;

  }

  @SuppressLint("SetTextI18n")
  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup view, final int position) {

    View myView = inflater.inflate(R.layout.item_exam_question_adapter, view, false);

    final ExpandableTextView tv_direction=myView.findViewById(R.id.tv_direction);
    final WebView wv_direction=myView.findViewById(R.id.wv_direction);
    final Button btn_toggle=myView.findViewById(R.id.btn_toggle);

    TextView question_no = myView.findViewById(R.id.question_no);
    WebView question_body = myView.findViewById(R.id.question_body);

    wv_direction.setBackgroundColor(Color.TRANSPARENT);
    final String direction = "<html><head>"
      + "<style type=\"text/css\">body{color: #000000;}"
      + "</style></head>"
      + "<body>"
      + quesList.get(position).getDirections()
      + "</body></html>";
    Spanned htmlAsSpanned = Html.fromHtml(quesList.get(position).getDirections());
    if (quesList.get(position).getDirections().equalsIgnoreCase("") || quesList.get(position).getDirections().equalsIgnoreCase("null")){
      wv_direction.setVisibility(View.GONE);
      btn_toggle.setVisibility(View.GONE);
      tv_direction.setVisibility(View.GONE);
    }else {
      wv_direction.loadDataWithBaseURL(null, direction, "text/html", "utf-8", null);
      wv_direction.setVisibility(View.GONE);
      btn_toggle.setVisibility(View.VISIBLE);
      tv_direction.setVisibility(View.VISIBLE);
      tv_direction.setText(htmlAsSpanned);
    }
    btn_toggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {

        if (btn_toggle.getText().toString().equalsIgnoreCase("Read More")){
          if (tv_direction.getVisibility()==View.VISIBLE){
            btn_toggle.setText("Read more");
            wv_direction.setVisibility(View.VISIBLE);
            tv_direction.setVisibility(View.GONE);
            wv_direction.loadDataWithBaseURL(null, direction, "text/html", "utf-8", null);
            if (wv_direction.getVisibility()==View.VISIBLE){
              btn_toggle.setText("Read less");
              tv_direction.setVisibility(View.GONE);

            }
          }
        }else {
          wv_direction.setVisibility(View.GONE);
          btn_toggle.setText("Read more");
          tv_direction.setVisibility(View.VISIBLE);

        }
      }
    });


    question_body.setBackgroundColor(Color.TRANSPARENT);
    String ques = "<html><head>"
      + "<style type=\"text/css\">body{color: #fff;}"
      + "</style></head>"
      + "<body>"
      + quesList.get(position).getQuestion()
      + "</body></html>";
    GradientDrawable gd = new GradientDrawable(
      GradientDrawable.Orientation.LEFT_RIGHT,
      new int[] {Color.parseColor("#135080"),Color.parseColor("#7cbaeb")});
    gd.setCornerRadius(0f);

    int no = position + 1;
    Const.QUES_NO = no;
    question_no.setText("Q. " + no);
    TermExamAnswerAdapter answerAdapter;
    if (Const.answerCheckHash.size() > 0 && Const.answerCheckHash.containsKey(quesList.get(position).getQuestionId())) {
      answerAdapter = new TermExamAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, Integer.parseInt(Const.answerCheckHash.get(quesList.get(position).getQuestionId())));
    } else {
      answerAdapter = new TermExamAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, -1);
    }

    question_body.loadDataWithBaseURL(null, ques, "text/html", "utf-8", null);
    RecyclerView ans_listView = myView.findViewById(R.id.ans_listView);
    ans_listView.setLayoutManager(new LinearLayoutManager(context));
    ans_listView.setAdapter(answerAdapter);
    view.addView(myView);
    return myView;
  }

}
