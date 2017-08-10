package com.example.tam.appnotes.model;

import java.io.Serializable;

/**
 * Created by tam on 8/3/2017.
 */

public class Note implements Serializable{
    public int idNote;
    public String title;
    public String note;
    public String date;
    public String timer;
    public String dateCurrent;
    public int color;
    public String picture;

    public Note(int idNote, String title, String note, String date, String timer, String dateCurrent, int color, String picture) {
        this.idNote = idNote;
        this.title = title;
        this.note = note;
        this.date = date;
        this.timer = timer;
        this.dateCurrent = dateCurrent;
        this.color = color;
        this.picture = picture;
    }

    public Note(String title, String note, String date, String timer, String dateCurrent, int color, String picture) {
        this.title = title;
        this.note = note;
        this.date = date;
        this.timer = timer;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
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
}