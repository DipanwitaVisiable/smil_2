package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.visiabletech.smilmobileapp.Adapter.DaysListAdapter;
import com.visiabletech.smilmobileapp.Pogo.DaysListInfo;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ImageView iv_back;
    private RequestQueue requestQueue;
    Button btn_edit_profile;
    private Context context;
    private ProgressBar pb_loader;
    private TextView tv_student_name, tv_class, tv_section, tv_phone_number, tv_profile_name ;
    private CircleImageView civ_profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=ProfileActivity.this;
        requestQueue = Volley.newRequestQueue(this);
        iv_back = findViewById(R.id.iv_back);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        tv_student_name = findViewById(R.id.tv_student_name);
        tv_class = findViewById(R.id.tv_class);
        tv_section = findViewById(R.id.tv_section);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_profile_name = findViewById(R.id.tv_profile_name);
        pb_loader = findViewById(R.id.pb_loader);
        civ_profile_image = findViewById(R.id.civ_profile_image);

        //Get profile value
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Const.USER_ID = pref.getString("student_id", "");
            Const.USER_ROLE = pref.getString("user_role", "");
            Const.PROFILENAME = pref.getString("profile_name", "");
            Const.PhoneNo = pref.getString("phoneNo", "");
            Const.CLASS_NAME = pref.getString("class_name", "");
            Const.SECTION_NAME = pref.getString("section", "");
        } else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        /*tv_student_name.setText(Const.PROFILENAME);
        tv_class.setText(Const.CLASS_NAME);
        tv_section.setText(Const.SECTION_NAME);
        tv_phone_number.setText(Const.PhoneNo);
        tv_profile_name.setText(Const.PROFILENAME);*/


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
            }
        });



        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
                finish();
            }
        });

        if (InternetConnection.checkConnection(context)) {
            getProfileDetails();
        } else {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
    }


    public void getProfileDetails() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "student_profile/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        JSONArray jsonArrayList = obj.getJSONArray("student_details");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String firstName = jsonObject.getString("First_Name");
                            String middleName = jsonObject.getString("middle_name");
                            String lastName = jsonObject.getString("Last_Name"); //active
                            String name = firstName + " " + middleName + " " + lastName;
                            String className = jsonObject.getString("class_or_year"); //active
                            String section = jsonObject.getString("section"); //active
                            String pinCode = jsonObject.getString("pin_Code");
                            String dob = jsonObject.getString("date_of_birth"); //active
                            String gender = jsonObject.getString("gender"); //active
                            String rollNo = jsonObject.getString("roll_no");
                            String motherTongue = jsonObject.getString("mother_tongue");
                            String mailingAddress = jsonObject.getString("mailing_address");
                            String city = jsonObject.getString("city"); //active
                            String state = jsonObject.getString("state"); //active
                            String homePhoneNo = jsonObject.getString("home_phoneno");
                            String contactNo = jsonObject.getString("mobile_number"); // active
                            String permanentAddress = jsonObject.getString("permanent_address"); // active
                            String permanentCity = jsonObject.getString("permanent_city");
                            String permanentState = jsonObject.getString("permanent_state");
                            String permanentPinCode = jsonObject.getString("permanent_pin");
                            String country = jsonObject.getString("country"); // active
                            String studentCode = jsonObject.getString("student_code"); // active
                            String mobileNo = jsonObject.getString("second_mobile_number"); // active
                            String fatherName = jsonObject.getString("father_name"); // active
                            String motherName = jsonObject.getString("mother_name"); // active
                            String imageUrl = jsonObject.getString("image_url"); // active

                            if (!imageUrl.equals("")) {
                                Picasso.with(getBaseContext())
                                        .load(imageUrl)
                                        .placeholder(R.drawable.user)
                                        .noFade()
                                        .into(civ_profile_image);
                            }
                            tv_student_name.setText(name);
                            tv_class.setText(className);
                            tv_section.setText(section);
                            tv_phone_number.setText(contactNo);
                            tv_profile_name.setText(name);
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

}
