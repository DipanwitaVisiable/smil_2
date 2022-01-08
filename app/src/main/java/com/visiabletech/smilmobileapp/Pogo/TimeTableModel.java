package com.visiabletech.smilmobileapp.Pogo;

public class TimeTableModel {

    private String subject_mon,subject_tue,subject_wed,subject_thu,subject_fri,predios;

    public TimeTableModel(String predios, String subject_mon, String subject_tue, String subject_wed, String subject_thu, String subject_fri) {
        this.subject_mon = subject_mon;
        this.subject_tue = subject_tue;
        this.subject_wed = subject_wed;
        this.subject_thu = subject_thu;
        this.subject_fri = subject_fri;
        this.predios = predios;
    }


    public String getSubject_mon() {
        return subject_mon;
    }

    public void setSubject_mon(String subject_mon) {
        this.subject_mon = subject_mon;
    }

    public String getSubject_tue() {
        return subject_tue;
    }

    public void setSubject_tue(String subject_tue) {
        this.subject_tue = subject_tue;
    }

    public String getSubject_wed() {
        return subject_wed;
    }

    public void setSubject_wed(String subject_wed) {
        this.subject_wed = subject_wed;
    }

    public String getSubject_thu() {
        return subject_thu;
    }

    public void setSubject_thu(String subject_thu) {
        this.subject_thu = subject_thu;
    }

    public String getSubject_fri() {
        return subject_fri;
    }

    public void setSubject_fri(String subject_fri) {
        this.subject_fri = subject_fri;
    }


    public String getPredios() {
        return predios;
    }

    public void setPredios(String predios) {
        this.predios = predios;
    }
}
