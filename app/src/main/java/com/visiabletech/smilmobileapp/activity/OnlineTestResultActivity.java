package com.visiabletech.smilmobileapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.MainActivity;
import com.visiabletech.smilmobileapp.ProfileActivity;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnlineTestResultActivity extends AppCompatActivity {

    PieChart pieChart;
    PieDataSet dataSet;
    PieData data;
    ProgressBar progressBar;
    ArrayList<Entry> yvalues = new ArrayList<>();
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();
    TextView incorrectAns, correctAns, notAns;
    private TextView tv_total_question, tv_total_time, tv_message;
    LinearLayout linearLayout1, linearLayout2;
    Button btn_view_solution;
    Context context;
    String exam_id, test_time, exam_taken_id, tot_qus;
    private StringRequest stringRequest_endQuiz;
    private RequestQueue requestQueue_endQuiz;
    private Toolbar toolbar;
    int total_correct_int = 0, total_wrong_int = 0, total_attempt_int = 0, total_skip_int = 0;
    private LinearLayout ll_home, ll_profile, ll_exam, ll_share;
    private String reg_id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_test_result);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=OnlineTestResultActivity.this;

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        Intent intent = getIntent();
        exam_taken_id = intent.getStringExtra("exam_taken_id");
        tot_qus = intent.getStringExtra("tot_qus");
        exam_id = intent.getStringExtra("exam_id");

//        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_home = findViewById(R.id.ll_home);
        ll_profile = findViewById(R.id.ll_profile);
        ll_exam = findViewById(R.id.ll_exam);
        ll_share = findViewById(R.id.ll_share);

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(context, MainActivity.class));
                finish();*/
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ProfileActivity.class));
                finish();
            }
        });
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        ll_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* startActivity(new Intent(context, ExamActivity.class));
                finish();*/
            }
        });
        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        progressBar = findViewById(R.id.wheel_refresh);
        tv_total_question = findViewById(R.id.tv_total_question);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_message = findViewById(R.id.tv_message);
        linearLayout1 = findViewById(R.id.l1);
        linearLayout2 = findViewById(R.id.l2);
        incorrectAns = findViewById(R.id.inc_ans);
        correctAns = findViewById(R.id.c_ans);
        notAns = findViewById(R.id.not_ans);
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        btn_view_solution = findViewById(R.id.btn_view_solution);
        showResult();

        btn_view_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SmallQuizViewSolutionActivity.class);
                intent.putExtra("exam_taken_id", exam_taken_id);
                intent.putExtra("exam_id", exam_id);
                context.startActivity(intent);
            }
        });

    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }


    private void showResult() {
        requestQueue_endQuiz = Volley.newRequestQueue(context);
        progressBar.setVisibility(View.VISIBLE);
        stringRequest_endQuiz = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "view_result",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            final JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                pieChart.setVisibility(View.VISIBLE);
                                JSONObject object = new JSONObject(jsonObject.getString("message"));

                                pieChart.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorBlack));

                                int total_correct_int = 0, total_wrong_int = 0, total_attempt_int = 0, total_skip_int = 0;

                                total_correct_int=Integer.parseInt(object.optString("correct"));
                                total_wrong_int=Integer.parseInt(object.optString("incorrect"));

                                tv_total_question.setText("Total Questions: " + tot_qus);
                                tv_message.setText("Total correct: " + object.optString("correct") + "   |   " +
                                        "Total wrong: " + object.optString("incorrect"));



                                if (total_correct_int > 0) {
                                    yvalues.add(new Entry(total_correct_int, 0));
                                    colors.add(getResources().getColor(R.color.colorPresent));
//                                    xVals.add("Correct");
                                    xVals.add("");
                                    correctAns.setText(String.valueOf(total_correct_int));
                                }
                                if (total_wrong_int > 0) {
                                    yvalues.add(new Entry(total_wrong_int, 1));
                                    colors.add(getResources().getColor(R.color.colorPrimary));
//                                    xVals.add("Wrong");
                                    xVals.add("");
                                    incorrectAns.setText(String.valueOf(total_wrong_int));
                                }

                                pieChart.setDescription("");
                                dataSet = new PieDataSet(yvalues, "");

                                data = new PieData(xVals, dataSet);
                                pieChart.setData(data);
                                pieChart.setDrawHoleEnabled(false);
                                pieChart.setTransparentCircleRadius(20f);
                                pieChart.setRotationEnabled(false);//For Rotation
                                pieChart.setDrawSliceText(false);//For Text
                                pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad); // Rotate Event
                                dataSet.setSliceSpace(1f);
                                dataSet.setColors(colors);
                                data.setValueTextSize(13f);
                                data.setValueTextColor(Color.WHITE);
                                data.setValueFormatter(new MyValueFormatter());
                                pieChart.setDrawHoleEnabled(true);
                                pieChart.setTouchEnabled(false);
                                linearLayout1.setVisibility(View.VISIBLE);
                                linearLayout2.setVisibility(View.VISIBLE);


                                progressBar.setVisibility(View.GONE);

                                Const.answerCheckHash.clear();
                                Const.answerStoreHash.clear();
                                Const.testquestionAnswerStoreHash.clear();

                            }
                            else if (status.equalsIgnoreCase("206"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(R.string.force_logout)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(context, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else {
                                pieChart.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getBaseContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();

                myParams.put("student_id", Const.USER_ID);
                myParams.put("exam_taken_id", exam_taken_id);
                myParams.put("reg_id", reg_id);

                return myParams;
            }
        };

        // Start- To remove timeout error
        stringRequest_endQuiz.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // End- To remove timeout error

        requestQueue_endQuiz.add(stringRequest_endQuiz);

    }

}

