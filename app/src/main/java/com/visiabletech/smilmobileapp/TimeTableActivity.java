package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.visiabletech.smilmobileapp.Adapter.TimeTableAdapter;
import com.visiabletech.smilmobileapp.Pogo.TimeTableInfo;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeTableActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<TimeTableInfo> timeTableList;
    private ProgressBar pb_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=TimeTableActivity.this;
        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Time Table");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TimeTableActivity.this,MainActivity.class));
                finish();
            }
        });

        pb_loader = findViewById(R.id.pb_loader);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (InternetConnection.checkConnection(context)) {
            getTimeTableValue();
        } else {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Const.USER_ID = pref.getString("student_id", "");
            Const.USER_ROLE = pref.getString("user_role", "");
            Const.PROFILENAME = pref.getString("profile_name", "");
            Const.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(TimeTableActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(TimeTableActivity.this,MainActivity.class));
        finish();
    }


    public void getTimeTableValue() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "get_timetable/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_loader.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        String dayStatus = obj.getString("day_status");
                        if (dayStatus.equals("true")) {
                            timeTableList = new ArrayList<>();
                            JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                TimeTableInfo timeTableInfo = new TimeTableInfo();
                                timeTableInfo.setTime(jsonObject.getString("time"));
                                timeTableInfo.setSubject1(jsonObject.getString("subject1"));
                                timeTableInfo.setSubject2(jsonObject.getString("subject2"));
                                timeTableInfo.setSubject3(jsonObject.getString("subject3"));
                                timeTableInfo.setSubject4(jsonObject.getString("subject4"));
                                timeTableInfo.setSubject5(jsonObject.getString("subject5"));
                                timeTableInfo.setSubject6("");
                                timeTableList.add(timeTableInfo);
                            }
                            recyclerView.setAdapter(new TimeTableAdapter(context, timeTableList));
                        }
                        else if (dayStatus.equals("false")){
                            timeTableList = new ArrayList<>();
                            JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                TimeTableInfo timeTableInfo = new TimeTableInfo();
                                timeTableInfo.setTime(jsonObject.getString("time"));
                                timeTableInfo.setSubject1(jsonObject.getString("subject1"));
                                timeTableInfo.setSubject2(jsonObject.getString("subject2"));
                                timeTableInfo.setSubject3(jsonObject.getString("subject3"));
                                timeTableInfo.setSubject4(jsonObject.getString("subject4"));
                                timeTableInfo.setSubject5(jsonObject.getString("subject5"));
                                timeTableInfo.setSubject6(jsonObject.getString("subject6"));

                                timeTableList.add(timeTableInfo);
                            }
                            recyclerView.setAdapter(new TimeTableAdapter(context, timeTableList));
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
                    else {
                        Toast.makeText(context, "No Value found", Toast.LENGTH_SHORT).show();
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
//                params.put("id", "1661");
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
