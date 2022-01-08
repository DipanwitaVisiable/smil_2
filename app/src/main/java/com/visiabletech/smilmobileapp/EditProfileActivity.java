package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.visiabletech.smilmobileapp.Adapter.CustomSpinnerSubjectAdapter;
import com.visiabletech.smilmobileapp.Pogo.CustomSpinnerSubjectGetSet;
import com.visiabletech.smilmobileapp.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private Context context;
    private RequestQueue requestQueue;
    private ProgressBar pb_loader;
    private CircleImageView civ_profile_image;
    private TextView tv_phone_number, tv_dob;
    private EditText et_name, et_father_name, et_class, et_pin_code, et_city, et_state, et_address, et_section;
    private RadioButton rb_male, rb_female;
    private ArrayList<CustomSpinnerSubjectGetSet> customSpinnerSubjectList_1=new ArrayList<>();
    public static ArrayList<CustomSpinnerSubjectGetSet> customSpinnerSubjectList_2=new ArrayList<>();
    private Spinner spinner_1, spinner_2, spinner_3, spinner_4, spinner_5, spinner_6 ;
    private boolean spin_1=false, spin_2=false, spin_3=false, spin_4=false, spin_5=false, spin_6=false;
    private Button btn_save;
    private String subject_1="1", subject_2="", subject_3="", subject_4="", subject_5="", subject_6="";
    private LinearLayout ll_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        hideKeyboard();
        context=EditProfileActivity.this;
        requestQueue = Volley.newRequestQueue(this);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar=findViewById(R.id.toolbar);
        pb_loader=findViewById(R.id.pb_loader);
        civ_profile_image=findViewById(R.id.civ_profile_image);
        tv_phone_number=findViewById(R.id.tv_phone_number);
        tv_dob=findViewById(R.id.tv_dob);
        et_name=findViewById(R.id.et_name);
        et_father_name=findViewById(R.id.et_father_name);
        et_class=findViewById(R.id.et_class);
        et_section=findViewById(R.id.et_section);
        et_pin_code=findViewById(R.id.et_pin_code);
        et_city=findViewById(R.id.et_city);
        et_state=findViewById(R.id.et_state);
        et_address=findViewById(R.id.et_address);
        rb_male=findViewById(R.id.rb_male);
        rb_female=findViewById(R.id.rb_female);
        spinner_1=findViewById(R.id.spinner_1);
        spinner_2=findViewById(R.id.spinner_2);
        spinner_3=findViewById(R.id.spinner_3);
        spinner_4=findViewById(R.id.spinner_4);
        spinner_5=findViewById(R.id.spinner_5);
        spinner_6=findViewById(R.id.spinner_6);
        btn_save=findViewById(R.id.btn_save);
        ll_spinner=findViewById(R.id.ll_spinner);

        spinner_1.setEnabled(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subject_2.equals(""))
                    Toast.makeText(context, "Please select your subject 2", Toast.LENGTH_SHORT).show();
                else if (subject_3.equals(""))
                    Toast.makeText(context, "Please select your subject 3", Toast.LENGTH_SHORT).show();
                else if (subject_4.equals(""))
                    Toast.makeText(context, "Please select your subject 4", Toast.LENGTH_SHORT).show();
                else if (subject_5.equals(""))
                    Toast.makeText(context, "Please select your subject 5", Toast.LENGTH_SHORT).show();
                else {
                    saveSubject();
                }
            }
        });

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
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        getProfileDetails();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
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
                            et_name.setText(name);
                            et_class.setText(className);
                            et_section.setText(section);
                            tv_phone_number.setText(contactNo);
                            et_city.setText(city);
                            et_state.setText(state);
                            et_father_name.setText(fatherName);
                            et_address.setText(permanentAddress);
                            et_pin_code.setText(pinCode);
                            tv_dob.setText(dob);
                            if (gender.equals("female"))
                                rb_female.setEnabled(true);
                            else
                                rb_male.setEnabled(true);

                        }

                        customSpinnerSubjectList_1.clear();
                        customSpinnerSubjectList_2.clear();

                        if (!obj.getString("subject_list").equals("false"))
                        {
                            ll_spinner.setVisibility(View.VISIBLE);
                            JSONArray subject_list_array = obj.getJSONArray("subject_list");
                            for (int i = 0; i <subject_list_array.length() ; i++) {
                                JSONObject jsonObject1 = subject_list_array.getJSONObject(i);
                                CustomSpinnerSubjectGetSet subjectGetSet=new CustomSpinnerSubjectGetSet();
                                subjectGetSet.setSubject(jsonObject1.getString("subject"));
                                subjectGetSet.setSubject_id(jsonObject1.getString("subject_id"));
                                customSpinnerSubjectList_1.add(subjectGetSet);
                            }


                            spinner_1.setAdapter(new CustomSpinnerSubjectAdapter(EditProfileActivity.this,customSpinnerSubjectList_1));
                            CustomSpinnerSubjectGetSet subjectGetSet2=new CustomSpinnerSubjectGetSet();
                            subjectGetSet2.setSubject("Select your subject");
                            subjectGetSet2.setSubject_id("");
                            customSpinnerSubjectList_2.add(0, subjectGetSet2);
                            customSpinnerSubjectList_2.addAll(customSpinnerSubjectList_1);
                            customSpinnerSubjectList_2.remove(1);

                            spinner_2.setAdapter(new CustomSpinnerSubjectAdapter(EditProfileActivity.this,customSpinnerSubjectList_2));
                            spinner_3.setAdapter(new CustomSpinnerSubjectAdapter(EditProfileActivity.this,customSpinnerSubjectList_2));
                            spinner_4.setAdapter(new CustomSpinnerSubjectAdapter(EditProfileActivity.this,customSpinnerSubjectList_2));
                            spinner_5.setAdapter(new CustomSpinnerSubjectAdapter(EditProfileActivity.this,customSpinnerSubjectList_2));
                            spinner_6.setAdapter(new CustomSpinnerSubjectAdapter(EditProfileActivity.this,customSpinnerSubjectList_2));

                            //start spinner 2
                            spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                   if (spin_2) {
//                                       sectionTopicList.remove(i);
                                       subject_2=customSpinnerSubjectList_2.get(i).getSubject_id();
                                       customSpinnerSubjectList_2.remove(i);
                                       spinner_2.setEnabled(false);
                                   }
                                   spin_2=true;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            //end spinner 2

                            //start spinner_3
                            spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (spin_3) {
//                                        sectionTopicList.remove(i);
                                        subject_3=customSpinnerSubjectList_2.get(i).getSubject_id();
                                        customSpinnerSubjectList_2.remove(i);
                                        spinner_3.setEnabled(false);
                                    }
                                    spin_3=true;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            //end spinner_3

                            //start spinner_4
                            spinner_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (spin_4) {
//                                        sectionTopicList.remove(i);
                                        subject_4=customSpinnerSubjectList_2.get(i).getSubject_id();
                                        customSpinnerSubjectList_2.remove(i);
                                        spinner_4.setEnabled(false);
                                    }
                                    spin_4=true;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            //end spinner_4


                            //start spinner_5
                            spinner_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (spin_5) {
//                                        sectionTopicList.remove(i);
                                        subject_5=customSpinnerSubjectList_2.get(i).getSubject_id();
                                        customSpinnerSubjectList_2.remove(i);
                                        spinner_5.setEnabled(false);
                                    }
                                    spin_5=true;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            //end spinner_5


                            //start spinner 6
                            spinner_6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (spin_6) {
//                                        sectionTopicList.remove(i);
                                        subject_6=customSpinnerSubjectList_2.get(i).getSubject_id();
                                        customSpinnerSubjectList_2.remove(i);
                                        spinner_6.setEnabled(false);
                                    }
                                    spin_6=true;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            //end spinner 6
                        }
                        else {
                            ll_spinner.setVisibility(View.GONE);
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
//                params.put("id", "2652");
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

    public void saveSubject() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "save_subject", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(context, "Please Try again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    pb_loader.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loader.setVisibility(View.GONE);
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Const.USER_ID);
                params.put("subject_1", subject_1);
                params.put("subject_2", subject_2);
                params.put("subject_3", subject_3);
                params.put("subject_4", subject_4);
                params.put("subject_5", subject_5);
                params.put("subject_6", subject_6);
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

        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);
    }
}
