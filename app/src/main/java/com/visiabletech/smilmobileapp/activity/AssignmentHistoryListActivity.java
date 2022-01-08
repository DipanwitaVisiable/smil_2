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
import android.webkit.WebView;
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
import com.visiabletech.smilmobileapp.Adapter.AssignmentHistoryListAdapter;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.Pogo.AssignmentFileModel;
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

public class AssignmentHistoryListActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<AssignmentFileModel> assignmentFileList;
    private RecyclerView rv_history_list;
    private ProgressBar pb_loader;
    private String reg_id, exam_id, exam_date, end_time;
    private TextView btn_reply;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);

        context = AssignmentHistoryListActivity.this;
        pb_loader = findViewById(R.id.pb_loader);
        btn_reply = findViewById(R.id.btn_reply);
        rv_history_list = findViewById(R.id.rv_history_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_history_list.setLayoutManager(layoutManager);

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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        exam_id = bundle.getString("exam_id");
        exam_date = bundle.getString("exam_date");
        end_time = bundle.getString("end_time");

        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AssignmentReplyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("exam_id", exam_id);
                bundle.putString("exam_date", exam_date);
                bundle.putString("end_time", end_time);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id = device_token.getString("FIREBASE_ID", "");

        if (InternetConnection.checkConnection(context)) {
            getHistoryList();
        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void getHistoryList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "student_exam_file", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        assignmentFileList=new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AssignmentFileModel topicListInfo=new AssignmentFileModel();
                            topicListInfo.setFile_url(jsonObject1.getString("upload_file"));
                            assignmentFileList.add(topicListInfo);
                        }
                        rv_history_list.setAdapter(new AssignmentHistoryListAdapter(context, assignmentFileList));
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
                        Toast.makeText(context, "No Assignment found", Toast.LENGTH_SHORT).show();
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
                params.put("exam_id", exam_id);

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


}
