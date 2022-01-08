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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.visiabletech.smilmobileapp.Adapter.PeriodListAdapter;
import com.visiabletech.smilmobileapp.ExamActivity;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.MainActivity;
import com.visiabletech.smilmobileapp.Pogo.PeriodListInfo;
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

public class PeriodListActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<PeriodListInfo> periodList;
    private RecyclerView rv_period_list;
    private ProgressBar pb_loader;
    private String days_id, days_name, reg_id;
    private LinearLayout ll_home, ll_profile, ll_exam, ll_share;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_list);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = PeriodListActivity.this;
        pb_loader=findViewById(R.id.pb_loader);


        //get value from previous class using bundle
        /*Bundle bundle;
        bundle = this.getIntent().getExtras();
         days_id = bundle.getString("days_id");
         days_name = bundle.getString("days_name");*/

        days_id = Const.DAYS_ID;
        days_name = Const.DAYS_NAME;

        final Toolbar toolbar = findViewById(R.id.toolbar);
       /* setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar.setTitle(days_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);
        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);*/
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
               /* startActivity(new Intent(context, MainActivity.class));
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

                /*startActivity(new Intent(context, ExamActivity.class));
                finish();*/
            }
        });

        rv_period_list=findViewById(R.id.rv_days_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_period_list.setLayoutManager(layoutManager);

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

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        if (InternetConnection.checkConnection(context)) {
            getPeriodList();
        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }

    }


    public void getPeriodList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "period_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        periodList=new ArrayList<>();

                        JSONArray jsonArray = jsonObject.getJSONArray("period_list");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PeriodListInfo periodListInfo=new PeriodListInfo();
                            periodListInfo.setId(jsonObject1.getString("id"));
                            periodListInfo.setPeriods(jsonObject1.getString("periods"));

                            periodList.add(periodListInfo);
                        }
                        rv_period_list.setAdapter(new PeriodListAdapter(context, periodList));
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
                        Toast.makeText(context, "No periods found", Toast.LENGTH_SHORT).show();
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
//                params.put("student_id", "2623");
                params.put("day_id", days_id);
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
}
