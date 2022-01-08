package com.visiabletech.smilmobileapp.Pogo;

import android.content.Context;

public class AttendanceInfo {

    Context context;

     String id;
     String std_id;
     String attendence_status;
     String late_reason;
     String date;


     String holiday_title;
     String holiday_desc;
     String holiday_date;



    public AttendanceInfo(Context context, String id, String std_id, String attendence_status, String date, String late_reason) {
        this.context = context;
        this.id = id;
        this.std_id = std_id;
        this.attendence_status = attendence_status;
        this.late_reason = late_reason;
        this.date = date;
    }

    public AttendanceInfo(String holiday_title, String holiday_desc, String holiday_date) {
        this.holiday_title = holiday_title;
        this.holiday_desc = holiday_desc;
        this.holiday_date = holiday_date;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getAttendence_status() {
        return attendence_status;
    }

    public void setAttendence_status(String attendence_status) {
        this.attendence_status = attendence_status;
    }

    public String getLate_reason() {
        return late_reason;
    }

    public void setLate_reason(String late_reason) {
        this.late_reason = late_reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
