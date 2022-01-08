package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.visiabletech.smilmobileapp.Adapter.DaysListAdapter;
import com.visiabletech.smilmobileapp.Pogo.DaysListInfo;
import com.visiabletech.smilmobileapp.Pogo.SwitchChildInfo;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;
import com.visiabletech.smilmobileapp.Utils.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText et_mobile_number, et_password;
    private ProgressBar pbHeaderProgress;
    private Context context;
    private RequestQueue requestQueue;
    private  String username, password;
    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;
    private Typeface typeface;
    private RadioGroup rg;
    private String firebase_id;
    private TextView tv_privacy_policy;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=LoginActivity.this;
        requestQueue = Volley.newRequestQueue(this);
        btn_login = findViewById(R.id.btn_login);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_password = findViewById(R.id.et_password);
        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
        loginReset();

        // Start- Get firebase token

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                firebase_id = newToken;
                Log.e("newToken", newToken);
                SharedPreferences prefsForID = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
                prefsForID.edit().putString("FIREBASE_ID", firebase_id).apply();


            }
        });

        // End-Get firebase token


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();*/

                if (validate()) {
                    pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                    if (InternetConnection.checkConnection(context)) {
                        pbHeaderProgress.setVisibility(View.GONE);
                        fetchLogin();
                    } else {
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacyDialog();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

         username = et_mobile_number.getText().toString();
         password = et_password.getText().toString();
//         password ="123456";

        if (username.isEmpty() || username.trim().length() == 0) {
            et_mobile_number.setError("enter username");
            valid = false;
        } else if (username.length() < 10) {
            et_mobile_number.setError("at least 10 characters");
            valid = false;
        } else {
            et_mobile_number.setError(null);
        }
        if (password.isEmpty() || password.trim().length() == 0) {
            et_password.setError("enter password");
            valid = false;
        } else if (password.length() < 6) {
            et_password.setError("minimum length 6");
            valid = false;
        } else {
            et_password.setError(null);
        }
        return valid;
    }


    public void fetchLogin() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "svclogin", new Response.Listener<String>() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://nirbadhngo.com/snrmemorialtrust/webservices/websvc_smil/" + "svclogins", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                  count=count+1;

                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("200")) {
                        String id, firstName, lastName, class_name, section_name;
                        JSONObject jsonObject = obj.getJSONObject("info");
                        id = jsonObject.getString("id");
                        firstName = jsonObject.getString("firstname");
                        lastName = jsonObject.getString("lastname");
                        class_name = jsonObject.getString("class");
                        section_name = jsonObject.getString("section");
                        String role = jsonObject.getString("role");

                        if (role.equalsIgnoreCase("s")) {

                            String childType = jsonObject.getString("childType");
                            if (childType.equalsIgnoreCase("m")) {
                                JSONArray areaListJsonArray = jsonObject.getJSONArray("student_code");
                                switchChildInfoArrayList = new ArrayList<>();
                                for (int i = 0; i < areaListJsonArray.length(); i++) {
                                    JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                                    String studentCode = cuisineJsonObject.getString("student_code");
                                    String fName = cuisineJsonObject.getString("First_Name");
                                    String lName = cuisineJsonObject.getString("Last_Name");
                                    String studentId = cuisineJsonObject.getString("Student_id");
                                    String class_or_year = cuisineJsonObject.getString("class_or_year");
                                    String section = cuisineJsonObject.getString("section");
                                    SwitchChildInfo switchChildInfo = new SwitchChildInfo(studentCode, fName, lName, studentId, class_or_year, section);
                                    switchChildInfoArrayList.add(i, switchChildInfo);
                                }
                                if (switchChildInfoArrayList != null) {
                                    showMultipleChild(role, switchChildInfoArrayList, username);
                                }
                            } else if (childType.equalsIgnoreCase("s")) {
                                Const.PROFILENAME = firstName + " " + lastName;
                                Const.USER_ID = id;
                                Const.USER_ROLE = role;
                                Const.PhoneNo = username;
                                Const.CLASS_NAME = class_name;
                                Const.SECTION_NAME = section_name;

                                StoreLogIn();
                                StoreUserDetails();
                                saveDeviceTokenWithUserIdApi(Const.USER_ID); //added
                            }
                        } else if (role.equalsIgnoreCase("d")) {

                            Const.PROFILENAME = firstName + " " + lastName;
                            Const.USER_ID = id;
                            Const.USER_ROLE = role;
                            Const.PhoneNo = username;
                            Const.CLASS_NAME = class_name;
                            Const.SECTION_NAME = section_name;

                            StoreLogIn();
                            StoreUserDetails();
                            saveDeviceTokenWithUserIdApi(Const.USER_ID);  //added
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    pbHeaderProgress.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("reg_id", firebase_id);
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

    public void StoreUserDetails() {
        SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
        editor.putString("PROFILENAME", Const.PROFILENAME);
        editor.putString("USERID", Const.USER_ID);
        editor.putString("CLASS_NAME", Const.CLASS_NAME);
        editor.putString("SECTION_NAME", Const.SECTION_NAME);
        editor.apply();

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, true);
        edt.putString(Key.KEY_STUDENT_ID, Const.USER_ID);
        edt.apply();
    }

    public void StoreLogIn() {
        SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
        editor.putString("UID", et_mobile_number.getText().toString().trim());
        editor.putString("PWD", et_password.getText().toString());
        editor.apply();

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(Key.KEY_STUDENT_ID, Const.USER_ID);
        edt.putString(Key.KEY_USER_ROLE, Const.USER_ROLE);
        edt.putString(Key.KEY_PROFILE_NAME, Const.PROFILENAME);
        edt.putString(Key.KEY_PHONE_NO, Const.PhoneNo);
        edt.putString(Key.KEY_CLASS_NAME, Const.CLASS_NAME);
        edt.putString(Key.KEY_SECTION_NAME, Const.SECTION_NAME);
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, true);
        edt.apply();
    }

    public void showMultipleChild(final String role, final ArrayList<SwitchChildInfo> switchChildInfoArrayList, final String phoneNo) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_multiple_child);
        List<String> stringList = new ArrayList<>();  // here is list
        for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
            stringList.add(switchChildInfo.fName + " " + switchChildInfo.lName);
        }
        TextView alertTitleTextView = dialog.findViewById(R.id.alertTitleTextView);
        alertTitleTextView.setTypeface(typeface);
        rg = dialog.findViewById(R.id.radioGroup);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setTypeface(typeface);
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                int radioButtonID = rg.getCheckedRadioButtonId();
                RadioButton radioButton = rg.findViewById(radioButtonID);
                String selectChild = radioButton.getText().toString().trim();
                //Log.e("Select from radio ", selectChild);
                for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
                    String childName = switchChildInfo.fName + " " + switchChildInfo.lName;
                    if (childName.equalsIgnoreCase(selectChild)) {
                        Const.PROFILENAME = childName;
                        //Log.e("After select ", Constants.PROFILENAME);
                        Const.USER_ID = switchChildInfo.studentId;
                        Const.USER_ROLE = role;
                        Const.PhoneNo = phoneNo;
                        Const.CLASS_NAME = switchChildInfo.className;
                        Const.SECTION_NAME = switchChildInfo.section;


                        /*Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();*/   //close-  This code is added on saveDeviceTokenWithUserIdApi()

                        StoreLogIn();
                        StoreUserDetails();
                        saveDeviceTokenWithUserIdApi(Const.USER_ID); //added
                    }
                }
            }
        });
        dialog.show();
    }

    public void loginReset() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.apply();
        Const.PROFILENAME = "";
        Const.USER_ROLE = "";
        Const.PhoneNo = "";
        Const.USER_ID = "";
        Const.CLASS_NAME = "";
        Const.SECTION_NAME = "";

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, false);
        edt.putString(Key.KEY_STUDENT_ID, Const.USER_ID);
        edt.putString(Key.KEY_USER_ROLE, Const.USER_ROLE);
        edt.putString(Key.KEY_PROFILE_NAME, Const.PROFILENAME);
        edt.putString(Key.KEY_PHONE_NO, Const.PhoneNo);
        edt.putString(Key.KEY_CLASS_NAME, Const.CLASS_NAME);
        edt.putString(Key.KEY_SECTION_NAME, Const.SECTION_NAME);
        edt.apply();
    }


    public void saveDeviceTokenWithUserIdApi(final String student_id) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "update_reg_id", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(context, "No days found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbHeaderProgress.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", student_id);
                params.put("reg_id", firebase_id);
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

    private void privacyDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_privacy_policy);
        TextView text0 = dialog.findViewById(R.id.text0);
        text0.setTypeface(typeface);
        TextView text1 = dialog.findViewById(R.id.text1);
        text1.setTypeface(typeface);
        TextView text2 = dialog.findViewById(R.id.text2);
        text2.setTypeface(typeface);
        TextView text3 = dialog.findViewById(R.id.text3);
        text3.setTypeface(typeface);
        Button ok = dialog.findViewById(R.id.button);
        ok.setTypeface(typeface);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
