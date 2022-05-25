package com.example.hee.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {
    private String documentId;
    private String nicname;
    private String title;
    private String date;
    private String contents;
    private String writedate;
    @ServerTimestamp
    private Date data;

    public Post() {
    }

    public Post(String documentId, String nicname,String title,String date ,String contents,String writedate) {
        this.documentId = documentId;
        this.nicname = nicname;
        this.title = title;
        this.date = date;
        this.contents = contents;
        this.writedate = writedate;
    }

    public String getNicname() {
        return nicname;
    }

    public void setNicname(String nicname) {
        this.nicname = nicname;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWritedate() { return writedate; }

    public void setWritedate() { this.writedate =  writedate; }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", nicname='" + nicname + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", contents='" + contents + '\'' +
                ", writedate='" + writedate + '\'' +
                ", data=" + data +
                '}';
    }
}
