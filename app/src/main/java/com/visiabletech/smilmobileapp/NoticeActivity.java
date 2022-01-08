package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.smilmobileapp.Adapter.NoticeAdapter;
import com.visiabletech.smilmobileapp.Pogo.NoticeModel;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private ProgressBar pb_loader;
    private ArrayList<NoticeModel> noticeArrayList ;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        requestQueue = Volley.newRequestQueue(this);
        context=NoticeActivity.this;
        pb_loader=findViewById(R.id.pb_loader);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notice");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoticeActivity.this,MainActivity.class));
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (InternetConnection.checkConnection(context)) {
            getNoticeList();
        } else {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    public void getNoticeList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "get_notice/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        noticeArrayList=new ArrayList<>();

                        JSONArray jsonArray = jsonObject.getJSONArray("notice_details");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            NoticeModel noticeModel=new NoticeModel();
                            noticeModel.setId(jsonObject1.getString("id"));
                            noticeModel.setClass_name(jsonObject1.getString("class_name"));
                            noticeModel.setSection(jsonObject1.getString("section"));
                            noticeModel.setNotice_date(jsonObject1.getString("notice_date"));
                            noticeModel.setNotice(jsonObject1.getString("notice"));
                            noticeModel.setNotice_subject(jsonObject1.getString("notice_subject"));
                            noticeModel.setTeacher_id(jsonObject1.getString("teacher_id"));
                            noticeModel.setStudent_id(jsonObject1.getString("student_id"));
                            noticeModel.setNotice_pdf(jsonObject1.getString("notice_pdf"));
                            noticeModel.setNotice_type(jsonObject1.getString("notice_type"));

                            noticeArrayList.add(noticeModel);
                        }
                        recyclerView.setAdapter(new NoticeAdapter(context, noticeArrayList));
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
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
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
                params.put("id", Const.USER_ID);
//                params.put("student_id", "2623");
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

        startActivity(new Intent(NoticeActivity.this,MainActivity.class));
        finish();
    }

}
