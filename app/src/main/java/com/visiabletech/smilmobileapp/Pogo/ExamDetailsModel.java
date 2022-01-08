package com.visiabletech.smilmobileapp.Pogo;

public class ExamDetailsModel {

    private String subject,exam,time,date;


    public ExamDetailsModel(String subject, String exam, String time, String date) {
        this.subject = subject;
        this.exam = exam;
        this.time = time;
        this.date = date;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
