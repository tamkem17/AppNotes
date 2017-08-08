package com.example.tam.appnotes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by tam on 8/3/2017.
 */

public class Note implements Serializable{
    public int idNote;
    public int idPicture;
    public String title;
    public String note;
    public String date;
    public String timer;
    public String dateCurrent;
    public int color;

    public Note(int idNote, int idPicture, String title, String note, String date, String timer, String dateCurrent, int color) {
        this.idNote = idNote;
        this.idPicture = idPicture;
        this.title = title;
        this.note = note;
        this.date = date;
        this.timer = timer;
        this.dateCurrent = dateCurrent;
        this.color = color;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public int getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(int idPicture) {
        this.idPicture = idPicture;
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