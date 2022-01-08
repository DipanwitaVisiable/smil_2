package com.visiabletech.smilmobileapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.visiabletech.smilmobileapp.Pogo.SwitchChildInfo;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.Key;
import com.visiabletech.smilmobileapp.activity.AssignmentTopicListActivity;
import com.visiabletech.smilmobileapp.activity.DaysListActivity;
import com.visiabletech.smilmobileapp.activity.SyllabusActivity;
import com.visiabletech.smilmobileapp.activity.TermExamActivity;
import com.visiabletech.smilmobileapp.activity.OnlineTestActivity;
import com.visiabletech.smilmobileapp.activity.SubjectListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private String currentVersion, latestVersion;
    private Dialog force_update_dialog;
    private Context context;
    private ProgressDialog version_checking_loader;

    private ImageView iv_notice, iv_bulletins, iv_assigment, iv_profile, iv_time_table, iv_medi_wallent, iv_term_exam,
            iv_ptm, iv_communication, iv_health_card, iv_online_class, iv_worksheet, iv_pay_online, iv_syllabus,
            iv_assignment, iv_online_test;

    private LinearLayout lin_exam;
    public static Dialog dialog;
    private ImageView iv_cancel_popup, iv_popup_image, iv_notification;
    private SliderLayout sliderLayout;
    private TextView tv_name, tv_wish_message, tv_class, tv_section, tv_notification;
    private ProgressBar pb_loader;
    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;
    private RequestQueue requestQueue;
    private String role = "s", reg_id;
    private Typeface typeface;
    //For  popup dialog
    public static Dialog reg_popup_dialog;
    private ImageView iv_image;
    private Button btn_register;
    private RelativeLayout rl_click_here;
    private String pop_up_button_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SMIL");
        toolbar.setTitleTextColor(Color.WHITE);
        context = MainActivity.this;
        version_checking_loader = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

//        getCurrentVersion();


        tv_name = findViewById(R.id.tv_name);
        tv_wish_message = findViewById(R.id.tv_wish_message);
        tv_class = findViewById(R.id.tv_class);
        tv_section = findViewById(R.id.tv_section);
        pb_loader = findViewById(R.id.pb_loader);


        iv_assignment = findViewById(R.id.iv_assignment);
        iv_syllabus = findViewById(R.id.iv_syllabus);
        iv_pay_online = findViewById(R.id.iv_pay_online);
        iv_notice = findViewById(R.id.iv_notice);
        iv_bulletins = findViewById(R.id.iv_bulletins);
        iv_online_test = findViewById(R.id.iv_online_test);
        iv_profile = findViewById(R.id.iv_profile);
        iv_time_table = findViewById(R.id.iv_time_table);
        iv_medi_wallent = findViewById(R.id.iv_medi_wallent);
        iv_term_exam = findViewById(R.id.iv_term_exam);
        iv_communication = findViewById(R.id.iv_communication);
//        iv_ptm = findViewById(R.id.iv_ptm);
        iv_health_card = findViewById(R.id.iv_health_card);

        iv_online_class = findViewById(R.id.iv_online_class);
        iv_worksheet = findViewById(R.id.iv_worksheet);

        lin_exam = findViewById(R.id.lin_exam);

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id = device_token.getString("FIREBASE_ID", "");

        sliderLayout = findViewById(R.id.slider_layout);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(5);

        getPayOnlineUrl();
        //Start reg popup
        getRegPopupImage();
        /*reg_popup_dialog = new Dialog(this);
        reg_popup_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        reg_popup_dialog.setCancelable(true);
        reg_popup_dialog.setContentView(getLayoutInflater().inflate(R.layout.popup_image, null));
        iv_image=reg_popup_dialog.findViewById(R.id.iv_image);*/
        //Start reg popup

        setSliderViews();

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
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        tv_name.setText(Const.PROFILENAME);
        tv_class.setText(Const.USER_ROLE);
        tv_section.setText("Section- " + Const.SECTION_NAME);
        tv_class.setText("Class- " + Const.CLASS_NAME);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            // Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            tv_wish_message.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            // Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();

            tv_wish_message.setText("Good Afternoon");


        } else if (timeOfDay >= 17 && timeOfDay < 21) {
            // Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();

            tv_wish_message.setText("Good Evening");

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            // Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();

            tv_wish_message.setText("Good Night");
        }


        iv_online_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DaysListActivity.class));
//                startActivity(new Intent(MainActivity.this, CustomPlayerTwoActivity.class));
//                finish();
            }
        });

        iv_worksheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SubjectListActivity.class));
//                finish();
            }
        });


        iv_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
                finish();
            }
        });


        iv_bulletins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, BulletinsActivity.class));
                finish();
            }
        });


        /*iv_assigment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, AssigmentActivity.class));
                finish();
            }
        });*/

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            }
        });


        iv_time_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, TimeTableActivity.class));
                finish();
            }
        });


        iv_medi_wallent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, MediWallentActivity.class));
                finish();
            }
        });

        lin_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivity(new Intent(MainActivity.this, ExamActivity.class));
                finish();*/
            }
        });

      iv_term_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
              startActivity(new Intent(MainActivity.this, TermExamActivity.class));
                finish();
            }
        });

       /* iv_ptm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, PTMActivity.class));
                finish();
            }
        });*/


        iv_communication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, CommunicationActivity.class));
                finish();
            }
        });

        iv_health_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, HealthCardActivity.class));
                finish();
            }
        });

        iv_pay_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Const.PAY_ONLINE_URL.equals("")) {
                    Uri uri = Uri.parse(Const.PAY_ONLINE_URL);
                    Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent_1);
                }
            }
        });

        iv_syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SyllabusActivity.class));
            }
        });

        iv_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AssignmentTopicListActivity.class));
            }
        });

        iv_online_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OnlineTestActivity.class));
            }
        });


        //Start- code to show pop up first time
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_up_image_dialog);
        iv_cancel_popup = dialog.findViewById(R.id.iv_cancel_popup);
        iv_popup_image = dialog.findViewById(R.id.iv_popup_image);

//        checkFirstRun();


        //Push notification Firebase
        FirebaseMessaging.getInstance().subscribeToTopic("SMIL2")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "successful";
                        if (!task.isSuccessful())
                            msg = "failed";
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                    }
                });

        //End push notification


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.switch_user, menu);

        MenuItem item = menu.findItem(R.id.menu_notification);
        MenuItemCompat.setActionView(item, R.layout.badge_layout);
        View view = MenuItemCompat.getActionView(item);
        tv_notification = view.findViewById(R.id.textView);
        iv_notification = view.findViewById(R.id.imageView);
        getNotificationCount();

        tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NotificationActivity.class));
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NotificationActivity.class));
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_notification:

                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                finish();
                break;

            case R.id.menu_logout:
                logoutAlert();
                break;

            case R.id.menu_switch_user:
                if (Const.USER_ROLE.equalsIgnoreCase("s")) {
                    switchUser();
                }
                if (Const.USER_ROLE.equalsIgnoreCase("d")) {

                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.home_slider_1);
                    //sliderView.setImageUrl("https://www.guideposts.org/sites/guideposts.org/files/styles/bynder_webimage/public/story/doctorvisit_marquee.jpg");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.home_slider_2);
                    //sliderView.setImageUrl("https://i2-prod.mirror.co.uk/interactives/article12645227.ece/ALTERNATES/s615/doctor.jpg");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.home_slider_3);
                    //sliderView.setImageUrl("https://worklogichr.com/sites/default/files/8-Important-Tips-on-How-to-Choose-a-Primary-Care-Doctor-copy.jpg");
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.home_slider_4);
                    //sliderView.setImageUrl("https://www.pymnts.com/wp-content/uploads/2018/03/doctor.jpg");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("Education for all Health" + (i + 1));
            final int finalI = i;

            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    //Toast.makeText(MediWallentActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();

                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }

    }


    //Start- code to show pop up first time

    public void checkFirstRun() {


        if (Const.OPEN_HOME_FIRST_TIME.equals("")) {
            Const.OPEN_HOME_FIRST_TIME = "false";
            //getPopUpImageApiCall();
            showPopUp();
        }

    }

    private void showPopUp() {

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);
        dialog.getWindow().setLayout(width, height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

        iv_cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);

    }


    public void logoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                loginReset();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorAccent));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void loginReset() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
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

    public void switchUser() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "switch_user_check", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray areaListJsonArray = obj.getJSONArray("child_details");
                        switchChildInfoArrayList = new ArrayList<>();
                        for (int i = 0; i < areaListJsonArray.length(); i++) {
                            JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                            String studentCode = cuisineJsonObject.getString("student_code");
                            String fName = cuisineJsonObject.getString("First_Name");
                            String lName = cuisineJsonObject.getString("Last_Name");
                            String class_name = cuisineJsonObject.getString("class_or_year");
                            String studentId = cuisineJsonObject.getString("Student_id");
                            String section = cuisineJsonObject.getString("section");
                            String roll_no = cuisineJsonObject.getString("roll_no");
                            String image_url = cuisineJsonObject.getString("image_url");

                            Const.PROFILENAME = fName + " " + lName;
                            Const.USER_ID = studentId;
                            Const.USER_ROLE = role;
                            Const.CLASS_NAME = class_name;
                            Const.SECTION_NAME = section;

                            storeLogIn();
                            storeUserDetails();

                            SwitchChildInfo switchChildInfo = new SwitchChildInfo(studentCode, fName, lName, studentId, class_name, section);
                            switchChildInfoArrayList.add(i, switchChildInfo);
                                /*SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = pref.edit();
                                edt.putString("student_id",studentId);
                                edt.commit();*/
                        }
                        if (switchChildInfoArrayList != null && role != null) {
                            showMultipleChild(role, switchChildInfoArrayList);
                        }

                    }
                    /*else if (status.equalsIgnoreCase("206"))
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage(R.string.force_logout)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }*/
                    else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
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

    public void storeLogIn() {

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
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

    public void storeUserDetails() {
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

    public void showMultipleChild(final String role, final ArrayList<SwitchChildInfo> switchChildInfoArrayList) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        //dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_multiple_child);
        List<String> stringList = new ArrayList<>();  // here is list
        for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
            stringList.add(switchChildInfo.fName + " " + switchChildInfo.lName);
        }
        TextView alertTitleTextView = dialog.findViewById(R.id.alertTitleTextView);
        alertTitleTextView.setTypeface(typeface);
        final RadioGroup rg = dialog.findViewById(R.id.radioGroup);
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
                String selectChild = radioButton.getText().toString();
                Log.i("Child name: ", selectChild);
                for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
                    String childName = switchChildInfo.fName + " " + switchChildInfo.lName;
                    if (childName.equalsIgnoreCase(selectChild)) {
                        Const.PROFILENAME = childName;
                        Const.USER_ID = switchChildInfo.studentId;
                        Const.CLASS_NAME = switchChildInfo.className;
                        Const.SECTION_NAME = switchChildInfo.section;
                        Const.USER_ROLE = role;
                        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = pref.edit();
                        edt.putString("student_id", Const.USER_ID);
                        edt.putString("class_name", Const.CLASS_NAME);
                        edt.commit();
                        Toast.makeText(MainActivity.this, "" + Const.USER_ID, Toast.LENGTH_SHORT).show();
                        getRegPopupImage();
                        //Constants.PhoneNo = phoneNo;
                        /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*/
                        storeLogIn();
                        storeUserDetails();
                        saveDeviceTokenWithUserIdApi(Const.USER_ID);
                    }
                }
            }
        });
        dialog.show();

    }


    @Override
    public void onBackPressed() {
        finish();
    }


    //1st method
    private void getCurrentVersion() {
        version_checking_loader.setCancelable(false);//you can cancel it by pressing back button.
        version_checking_loader.setMessage("loading...");
        version_checking_loader.show();
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();
    }

    //2nd method
    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                /*Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.visiabletech.smilmobileapp&hl=en").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();*/

                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                        version_checking_loader.dismiss();
                    }
                } else
                    version_checking_loader.dismiss();
            } else
//                background.start();
                ;
            super.onPostExecute(jsonObject);
        }
    }


    //3rd method
    private void showUpdateDialog() {
        /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=" + context.getPackageName())));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setCancelable(false);
        force_update_dialog = builder.show();*/

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.Theme_AppCompat_Dialog_Alert));
        alertDialogBuilder.setTitle("Update Available");
        alertDialogBuilder.setMessage("A new version of SMIL 2 is available. Please update");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                dialog.dismiss();
                finish();

            }
        });

        force_update_dialog = alertDialogBuilder.create();
        force_update_dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentVersion();
    }


    public void getNotificationCount() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "notification_count", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/notification_count", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200")) {
                        String notification_count = jsonObject.getString("notification_count");
                        tv_notification.setText(notification_count);
                    } else if (status.equalsIgnoreCase("206")) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage(R.string.force_logout)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    } else {
//                        Toast.makeText(context, "No days found", Toast.LENGTH_SHORT).show();
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

    public void saveDeviceTokenWithUserIdApi(final String student_id) {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "update_reg_id", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200")) {
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(context, "No days found", Toast.LENGTH_SHORT).show();
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
                params.put("student_id", student_id);
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


    //Start for reg popup

    private void getRegPopupImage() {

        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "show_popup", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    pop_up_button_text = jsonObject.getString("button_val");
                    if (status.equalsIgnoreCase("200")) {
                        pop_up_button_text = jsonObject.getString("button_val");
                        showRegPopUp();

                    } /*else {
                        showRegPopUp();
                    }*/
                    pb_loader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Const.USER_ID);
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

    private void showRegPopUp() {
        reg_popup_dialog = new Dialog(this);
        reg_popup_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        reg_popup_dialog.setCancelable(false);
        reg_popup_dialog.setContentView(getLayoutInflater().inflate(R.layout.popup_image, null));
        btn_register = reg_popup_dialog.findViewById(R.id.btn_register);
        rl_click_here = reg_popup_dialog.findViewById(R.id.rl_click_here);
        btn_register.setText(pop_up_button_text);
        reg_popup_dialog.show();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg_popup_dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                finish();
            }
        });
        rl_click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_register.performLongClick();
            }
        });

        reg_popup_dialog.setCanceledOnTouchOutside(false);

    }

    //End for reg popup



    private void getPayOnlineUrl() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Const.BASE_SERVER + "payonline", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String online_link = jsonObject.getString("online_link");
                    Const.PAY_ONLINE_URL=online_link;
                    pb_loader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("student_id", Const.USER_ID);
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
