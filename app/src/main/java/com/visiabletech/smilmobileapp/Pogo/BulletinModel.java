package com.visiabletech.smilmobileapp.Pogo;

public class BulletinModel {
    private  String id;
    private String notice;
    private String date;
    private String notice_subject;
    private String post_by;
    private String bullet_pdf;
    private String bullet_type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotice_subject() {
        return notice_subject;
    }

    public void setNotice_subject(String notice_subject) {
        this.notice_subject = notice_subject;
    }

    public String getPost_by() {
        return post_by;
    }

    public void setPost_by(String post_by) {
        this.post_by = post_by;
    }

    public String getBullet_pdf() {
        return bullet_pdf;
    }

    public void setBullet_pdf(String bullet_pdf) {
        this.bullet_pdf = bullet_pdf;
    }

    public String getBullet_type() {
        return bullet_type;
    }

    public void setBullet_type(String bullet_type) {
        this.bullet_type = bullet_type;
    }

}
