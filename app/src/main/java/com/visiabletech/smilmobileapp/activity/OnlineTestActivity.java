package com.visiabletech.smilmobileapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.smilmobileapp.Adapter.DaysListAdapter;
import com.visiabletech.smilmobileapp.Adapter.OnlineTestListAdapter;
import com.visiabletech.smilmobileapp.HealthCardActivity;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.MainActivity;
import com.visiabletech.smilmobileapp.Pogo.DaysListInfo;
import com.visiabletech.smilmobileapp.Pogo.OnlineTestInfo;
import com.visiabletech.smilmobileapp.ProfileActivity;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnlineTestActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<OnlineTestInfo> testList;
    private RecyclerView rv_test_list;
    private ProgressBar pb_loader;
    private TextView tv_no_exam;
    private String reg_id;
    private LinearLayout ll_home, ll_profile, ll_exam, ll_share;
    private WebView wv_test;
    private Button btn_test;
    private String exam_name, exam_id, total_question, total_time, exam_taken_id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_test);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = OnlineTestActivity.this;
        pb_loader=findViewById(R.id.pb_loader);
        tv_no_exam=findViewById(R.id.tv_no_exam);
        wv_test=findViewById(R.id.wv_test);
        btn_test=findViewById(R.id.btn_test);
        rv_test_list=findViewById(R.id.rv_test_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_home=findViewById(R.id.ll_home);
        ll_profile=findViewById(R.id.ll_profile);
        ll_exam=findViewById(R.id.ll_exam);
        ll_share=findViewById(R.id.ll_share);

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_test_list.setLayoutManager(layoutManager);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Const.USER_ID = pref.getString("student_id", "");
            Const.USER_ROLE = pref.getString("user_role", "");
            Const.PROFILENAME = pref.getString("profile_name", "");
            Const.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (InternetConnection.checkConnection(context)) {
            getExamList();
        } else {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show();
        }


        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_test.getText().toString().trim().equals("Start Test")) {
//                if (btn_test.getText().toString().trim().equals("Exam Completed") || btn_test.getText().toString().trim().equals("Exam Not Completed") || btn_test.getText().toString().trim().equals("View Result")|| btn_test.getText().toString().trim().equals("Start Test")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.quiz_dialog_view);
                    dialog.setCancelable(true);
                    Button noB = dialog.findViewById(R.id.noButton);
                    Button yesB = dialog.findViewById(R.id.yesButton);
                    TextView textViewTopic = dialog.findViewById(R.id.testTopic);
                    TextView questions = dialog.findViewById(R.id.testQuestions);
                    TextView duration = dialog.findViewById(R.id.duration);
                    textViewTopic.setText(exam_name);
                    questions.setText(total_question);
                    duration.setText(total_time + " min");
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
                            intent.putExtra("quiz_id", exam_id);
                            intent.putExtra("time", total_time);
                            context.startActivity(intent);

                        }
                    });
                    dialog.show();
                }
                else if (btn_test.getText().toString().trim().equals("View Result"))
//                else if (btn_test.getText().toString().trim().equals("Vi ew Result") || btn_test.getText().toString().trim().equals("Exam Completed"))
                {
                    Intent intent = new Intent(context, OnlineTestResultActivity.class);
                    intent.putExtra("exam_taken_id", exam_taken_id);
                    intent.putExtra("tot_qus", total_question);
                    intent.putExtra("exam_id", exam_id);
                    context.startActivity(intent);

                }
            }
        });

    }


    public void getExamList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "exam_test", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        tv_no_exam.setVisibility(View.GONE);
                        testList=new ArrayList<>();

                        if (jsonObject.optString("message")!=null && !jsonObject.optString("message").equals("")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            if (jsonArray.length() > 0) {
                                rv_test_list.setVisibility(View.GONE);
                                wv_test.setVisibility(View.VISIBLE);
                                btn_test.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    wv_test.loadUrl(jsonObject1.getString("page_link"));
                                    exam_taken_id = jsonObject1.getString("exam_taken_id");
                                    exam_id = jsonObject1.getString("exam_id");
                                    exam_name = jsonObject1.getString("exam_name");
                                    total_time = jsonObject1.getString("tot_time");
                                    total_question = jsonObject1.getString("tot_qus");
                                    btn_test.setText(jsonObject1.getString("status_text"));
                                }
                            }
                        }
                        if (jsonObject.optString("result_array")!=null && !jsonObject.optString("result_array").equals("")) {
                            JSONArray result_array = jsonObject.getJSONArray("result_array");
                            if (result_array.length() > 0) {
                                rv_test_list.setVisibility(View.VISIBLE);
                                wv_test.setVisibility(View.GONE);
                                btn_test.setVisibility(View.GONE);
                                for (int i = 0; i < result_array.length(); i++) {
                                    JSONObject jsonObject2 = result_array.getJSONObject(i);
                                    OnlineTestInfo onlineTestInfo = new OnlineTestInfo();
                                    onlineTestInfo.setExam_id(jsonObject2.getString("exam_id"));
                                    onlineTestInfo.setExam_name(jsonObject2.getString("exam_name"));
                                    onlineTestInfo.setTot_time(jsonObject2.getString("tot_time"));
                                    onlineTestInfo.setTot_qus(jsonObject2.getString("tot_qus"));
                                    onlineTestInfo.setSubject_name(jsonObject2.getString("subject_name"));
                                    onlineTestInfo.setExam_date(jsonObject2.getString("exam_date"));
                                    onlineTestInfo.setStatus(jsonObject2.getString("status"));
                                    onlineTestInfo.setStatus_text(jsonObject2.getString("status_text"));
                                    onlineTestInfo.setExam_taken_id(jsonObject2.getString("exam_taken_id"));
                                    onlineTestInfo.setChapter_name(jsonObject2.getString("chapter_name"));
                                    testList.add(onlineTestInfo);
                                }
                                rv_test_list.setAdapter(new OnlineTestListAdapter(context, testList));
                            }
                        }



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
                    else
                    {
                        tv_no_exam.setVisibility(View.VISIBLE);
                        btn_test.setVisibility(View.GONE);
//                        Toast.makeText(context, "No Exam found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loader.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loader.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Const.USER_ID);
                params.put("reg_id", reg_id);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                for (Map.Entry<String, String> pairs : map.entrySet()) {
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OnlineTestActivity.this, MainActivity.class));
        finish();
    }
}
