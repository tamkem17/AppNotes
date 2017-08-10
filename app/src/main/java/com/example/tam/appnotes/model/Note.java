package com.example.tam.appnotes.model;

import java.io.Serializable;

/**
 * Created by tam on 8/3/2017.
 */

public class Note implements Serializable{
    public int idNote;
    public String title;
    public String note;
    public String alarm;
    public String dateCurrent;
    public int color;
    public String picture;

    public Note(int idNote, String title, String note, String alarm, String dateCurrent, int color, String picture) {
        this.idNote = idNote;
        this.title = title;
        this.note = note;
        this.alarm = alarm;
        this.dateCurrent = dateCurrent;
        this.color = color;
        this.picture = picture;
    }

    public Note(String title, String note, String alarm, String dateCurrent, int color, String picture) {
        this.title = title;
        this.note = note;
        this.alarm = alarm;
        this.dateCurrent = dateCurrent;
        this.color = color;
        this.picture = picture;
    }

    public Note(String picture) {
        this.picture = picture;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateCurrent() {
        return dateCurrent;
    }

    public void setDateCurrent(String dateCurrent) {
        this.dateCurrent = dateCurrent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}