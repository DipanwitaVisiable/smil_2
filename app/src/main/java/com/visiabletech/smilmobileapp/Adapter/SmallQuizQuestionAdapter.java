package com.visiabletech.smilmobileapp.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.smilmobileapp.Pogo.AnswerTable;
import com.visiabletech.smilmobileapp.Pogo.QuestionTable;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SmallQuizQuestionAdapter extends PagerAdapter {
    Context context;
    private ArrayList<QuestionTable> quesList;
    private LayoutInflater inflater;
    private HashMap<String, ArrayList<AnswerTable>> listHashMap;
    private Button submitButton;
    private String exam_ques;
    private RequestQueue requestQueue;

    public SmallQuizQuestionAdapter(Context context, ArrayList<QuestionTable> quesList,
                               HashMap<String, ArrayList<AnswerTable>> listHashMap, Button submitButton) {
        this.context = context;
        this.quesList = quesList;
        this.listHashMap = listHashMap;
        this.submitButton = submitButton;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return listHashMap.size();
      return quesList.size();
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

        View myView = inflater.inflate(R.layout.item_small_quiz_question_adapter, view, false);
        TextView question_no = myView.findViewById(R.id.question_no);
        WebView question_body = myView.findViewById(R.id.question_body);

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
        SmallQuizAnswerAdapter answerAdapter;
        /*if (Const.answerCheckHash.size() > 0 && Const.answerCheckHash.containsKey(quesList.get(position).getQuestionId())) {
            answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, Integer.parseInt(Const.answerCheckHash.get(quesList.get(position).getQuestionId())));
        } else {
            answerAdapter = new SmallQuizAnswerAdapter(context, listHashMap.get(quesList.get(position).getQuestionId()), quesList.get(position).getQuestion(), quesList.get(position).getQuestionId(), submitButton, -1);
        }*/

      answerAdapter = new SmallQuizAnswerAdapter(context, quesList.get(position).getQuestionId(), quesList.get(position).getQuestion(), quesList.get(position).getAnswerDetails(), submitButton);

        question_body.loadDataWithBaseURL(null, ques, "text/html", "utf-8", null);
        RecyclerView ans_listView = myView.findViewById(R.id.ans_listView);
        ans_listView.setLayoutManager(new LinearLayoutManager(context));
        ans_listView.setAdapter(answerAdapter);
        view.addView(myView);
        return myView;
    }

}
