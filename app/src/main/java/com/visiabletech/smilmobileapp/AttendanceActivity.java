package com.visiabletech.smilmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.smilmobileapp.Adapter.CalendarAdapter;
import com.visiabletech.smilmobileapp.Pogo.AttendanceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    TextView title;
    public Calendar month, itemmonth;// calendar instances.
    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    public ArrayList<String> items; // container to store calendar items which

    RelativeLayout previous,next;
    int i;
    private ArrayList<AttendanceInfo> attendanceArrayList;
    private GridView gridview;
    private TextView present_textView, absent_textView, messageTextView, holiday_textView, leave_textView;
    private Button lateReasonButton;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    String BASE_SERVER="http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/get_old_student_attendence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendance");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendanceActivity.this,MainActivity.class));
                finish();
            }
        });


        gridview = findViewById(R.id.gridview);

        present_textView = findViewById(R.id.present_textView);
        holiday_textView = findViewById(R.id.holiday_textView);
        leave_textView = findViewById(R.id.leave_textView);
        absent_textView = findViewById(R.id.absent_textView);


        /*
          Retrieved Current date from System.
         */
        month = Calendar.getInstance();
        itemmonth = (Calendar) month.clone();

         /*
          Display Current Month with Year on
          the Top of Calendar.
         */
        title = findViewById(R.id.title);


         /*
          RelativeLayout for displaying
          previous Month of current year.
         */
        previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                if (adapter != null) {
                    refreshCalendar();
                }
            }
        });

        /*
          RelativeLayout for displaying
          next month of current year.
         */
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                if (adapter != null) {
                    refreshCalendar();
                }

            }
        });


        fetchAttendanceDetails();

    }



    //----------------------------
    // Showing calendar next
    // current month.
    //----------------------------
    @SuppressLint("SetTextI18n")
    protected void setNextMonth() {
        int absentCount = 0, presentCount = 0, holidayCount = 0, leaveCount = 0;
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) + 1), month.getActualMinimum(Calendar.MONTH), 1);
            int nextMonth = month.get(Calendar.MONTH) + 1;
            //Toast.makeText(this, "nextMonth" + nextMonth, Toast.LENGTH_SHORT).show();

            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.getDate();
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.getAttendence_status().equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }

                }

            }

        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
            int nextMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.getDate();
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.getAttendence_status().equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }


                }
            }


        }
        present_textView.setText(String.valueOf(presentCount));
        absent_textView.setText(String.valueOf(absentCount));
        holiday_textView.setText("HOLIDAY: " + holidayCount);
        leave_textView.setText("LEAVE: " + leaveCount);
    }





    //-----------------------------
    // Showing calendar previous
    // current month.
    //-----------------------------
    @SuppressLint("SetTextI18n")
    protected void setPreviousMonth() {
        int absentCount = 0, presentCount = 0, holidayCount = 0, leaveCount = 0;
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) - 1),
                    month.getActualMaximum(Calendar.MONTH), 1);
            int previousMonth = month.get(Calendar.MONTH) + 1;
            //Toast.makeText(this, "previousMonth" + previousMonth, Toast.LENGTH_SHORT).show();

            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.getDate();
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (previousMonth == currentMonth) {
                        if (attendanceInfo.getAttendence_status().equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }

                }

            }


        }

        else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
            int previousMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.getDate();
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (previousMonth == currentMonth) {
                        if (attendanceInfo.getAttendence_status().equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }


                }
            }


        }
        present_textView.setText(String.valueOf(presentCount));
        absent_textView.setText(String.valueOf(absentCount));
        holiday_textView.setText("HOLIDAY: " + holidayCount);
        leave_textView.setText("LEAVE: " + leaveCount);
    }





    //----------------------------
    // Showing calendar
    // current month with no of
    // present, absent, holiday and leave.
    //----------------------------
    @SuppressLint("SetTextI18n")
    protected void setCurrentMonth() {
        int absentCount = 0, presentCount = 0, holidayCount = 0, leaveCount = 0;

        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            //  month.set((month.get(Calendar.YEAR)),
            // month.getActualMinimum(Calendar.MONTH), 1);
            int nextMonth = month.get(Calendar.MONTH) + 1;
            //Toast.makeText(this, "" + nextMonth, Toast.LENGTH_SHORT).show();

            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.getDate();
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    //Toast.makeText(this, "currentMonth" + currentMonth, Toast.LENGTH_SHORT).show();
                    //New Add
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.getAttendence_status().equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }

                }

            }



        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH));
            int nextMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.getDate();
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (currentMonth==nextMonth) {
                        if (attendanceInfo.getAttendence_status().equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.getAttendence_status().equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }


                }
            }


        }
        refreshCalendar();
        present_textView.setText(String.valueOf(presentCount));
        absent_textView.setText(String.valueOf(absentCount));
        holiday_textView.setText("HOLIDAY: " + holidayCount);
        leave_textView.setText("LEAVE: " + leaveCount);
    }





    //-------------------------------
    // Refresh calendar.
    //-------------------------------
    public void refreshCalendar() {
        //TextView title = findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        // handler.post(calendarUpdater); // generate some calendar items
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        //title.setTypeface(typeface);
    }




    private void fetchAttendanceDetails()
    {

        requestQueue= Volley.newRequestQueue(AttendanceActivity.this);

       /* progressDialog=new ProgressDialog(AttendanceActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        stringRequest=new StringRequest(Request.Method.POST, BASE_SERVER,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");
                            String message=jsonObject.getString("message");

                            if (status.equalsIgnoreCase("200"))
                            {

                                attendanceArrayList=new ArrayList<>();



                                JSONArray jsonArray = jsonObject.getJSONArray("attendence_details");

                                JSONArray jsonArray_holiday = jsonObject.getJSONArray("holiday_list");

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String id = jsonObject1.getString("id");
                                    String std_id = jsonObject1.getString("std_id");
                                    String attendence_status = jsonObject1.getString("attendence_status");
                                    String date = jsonObject1.getString("date");
                                    String late_reason = jsonObject1.getString("late_reason");

                                    AttendanceInfo attendanceInfo = new AttendanceInfo(AttendanceActivity.this,id,std_id,attendence_status,date,late_reason);
                                    attendanceArrayList.add(i,attendanceInfo);

                                }


                                for (int j=0; j<jsonArray_holiday.length(); j++)
                                {
                                    JSONObject jsonObject_holiday = jsonArray_holiday.getJSONObject(j);

                                    String holiday_title = jsonObject_holiday.getString("holiday_title");
                                    String holiday_desc = jsonObject_holiday.getString("holiday_desc");
                                    String holiday_date = jsonObject_holiday.getString("holiday_date");

                                    AttendanceInfo attendanceInfo = new AttendanceInfo(holiday_title,holiday_desc,holiday_date);
                                   // attendanceArrayList.add(j,attendanceInfo);
                                }


                                month = Calendar.getInstance();
                                adapter = new CalendarAdapter(AttendanceActivity.this, (GregorianCalendar) month, attendanceArrayList);
                                gridview.setAdapter(adapter);
                                items = new ArrayList<>();

                                //progressDialog.dismiss();




                                    /*
                              Set current month absent, present, holiday and leave.
                             */
                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                        String selectedGridDate = CalendarAdapter.dayString.get(position);
                                        String[] separatedTime = selectedGridDate.split("-");
                                        String gridvalueString = separatedTime[2].replaceFirst("^0*", "");// taking last part of date. ie; 2 from 2012-12-02.
                                        int gridvalue = Integer.parseInt(gridvalueString);
                                        // navigate to next or previous month on clicking offdays.
                                        if ((gridvalue > 10) && (position < 8)) {
                                            setPreviousMonth();
                                            refreshCalendar();
                                        } else if ((gridvalue < 7) && (position > 28)) {
                                            setNextMonth();
                                            refreshCalendar();
                                        }


                                        //Toast.makeText(AttendanceActivity.this, selectedGridDate, Toast.LENGTH_SHORT).show();
                                    }
                                });


                                setCurrentMonth();




                            }

                            else
                            {

                                Toast.makeText(AttendanceActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                               // progressDialog.dismiss();


                            }



                        }catch (JSONException e)
                        {

                            e.printStackTrace();
                            //progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AttendanceActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>myParams=new HashMap<String, String>();
                myParams.put("student_id","5");

                return myParams;
            }
        };

        requestQueue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(AttendanceActivity.this,MainActivity.class));
        finish();
    }
}
