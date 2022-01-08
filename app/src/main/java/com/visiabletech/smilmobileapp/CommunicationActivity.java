package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.Helper;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;
import com.visiabletech.smilmobileapp.activity.ViewCommunicationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunicationActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button btn_from,btn_to, btn_submit, btn_view;
    private int mYear, mMonth, mDay;
    private String typeRequest = "";
    private EditText et_subject, et_message;
    private Context context;
    private ProgressBar pb_loader;
    private RequestQueue requestQueue;
    private LinearLayout ll_home, ll_profile, ll_exam, ll_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=CommunicationActivity.this;
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Communication");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunicationActivity.this,MainActivity.class));
                finish();
            }
        });

        //Start Bottom navigation

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

                /*startActivity(new Intent(context, ExamActivity.class));
                finish();*/
            }
        });

        //End Bottom navigation


        spinner = findViewById(R.id.spinner);
        btn_from = findViewById(R.id.btn_from);
        btn_to = findViewById(R.id.btn_to);
        btn_submit = findViewById(R.id.btn_submit);
        et_subject = findViewById(R.id.et_subject);
        et_message = findViewById(R.id.et_message);
        pb_loader = findViewById(R.id.pb_loader);
        btn_view = findViewById(R.id.btn_view);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ViewCommunicationActivity.class));
            }
        });

        btn_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog datePickerDialog = new DatePickerDialog(CommunicationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btn_from.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });



        btn_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatePickerDialog datePickerDialog1 = new DatePickerDialog(CommunicationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btn_to.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.show();

            }
        });



        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select request type");
        categories.add("Leave Request");
        categories.add("Request for Uniform");
        categories.add("Request for Books");
        categories.add("Request for Special");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position != 0) {
                    typeRequest = spinner.getSelectedItem().toString().trim();
                }
                //String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String typeSpinnerString = typeSpinner.getSelectedItem().toString().trim();
                String fromDateString = btn_from.getText().toString().trim();
                String toDateString = btn_to.getText().toString().trim();
                String subject = et_subject.getText().toString().trim();
                String message = et_message.getText().toString().trim();
                if (subject.isEmpty()){
                    showMessage("Required subject");
                    return;
                }
                if (typeRequest.isEmpty()){
                    showMessage("Required request type");
                    return;
                }
                if (fromDateString.equalsIgnoreCase("From Date")){
                    showMessage("Required From Date");
                    return;
                }
                if (toDateString.equalsIgnoreCase("To Date")){
                    showMessage("Required To Date");
                    return;
                }
                if (message.isEmpty()){
                    showMessage("Required message");
                    return;
                }
                if (!fromDateString.equalsIgnoreCase("From Date")
                        && !toDateString.equalsIgnoreCase("To Date")) {
                    if (fromDateString.compareTo(toDateString) <= 0) {
                        if(InternetConnection.checkConnection(context)){
                            String from_date= Helper.parseDateYyyyMMdd(fromDateString);
                            String to_date= Helper.parseDateYyyyMMdd(toDateString);
                            submitRequest(subject, message, typeRequest, from_date, to_date);
                            /*et_subject.setText("");
                            et_message.setText("");*/
                        }else {
                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


    }



    private void showMessage(String required_subject) {

        Toast.makeText(CommunicationActivity.this, required_subject, Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(CommunicationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(CommunicationActivity.this,MainActivity.class));
        finish();
    }


    private void submitRequest(final String subject, final String message, final String typeRequest, final String fromDateString, final String toDateString) {

        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "send_request/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        /*Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();*/

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        et_subject.setText("");
                        et_message.setText("");
                        spinner.setSelection(0);
                        btn_from.setText("From");
                        btn_to.setText("To");
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
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
                params.put("subject", subject);
                params.put("msg", message);
                params.put("msg_type", typeRequest);
                params.put("start_date", fromDateString);
                params.put("end_date", toDateString);
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
