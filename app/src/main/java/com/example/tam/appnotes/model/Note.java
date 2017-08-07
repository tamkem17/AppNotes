package com.example.tam.appnotes.model;

/**
 * Created by tam on 8/3/2017.
 */

public class Note {
    public int idNote;
    public String title;
    public String note;
    public String timer;
    public String dateCurrent;
    public int color;
    public byte[] image;

    public Note(int idNote, String title, String note, String timer, String dateCurrent, int color, byte[] image) {
        this.idNote = idNote;
        this.title = title;
        this.note = note;
        this.timer = timer;
        this.dateCurrent = dateCurrent;
        this.color = color;
        this.image = image;
    }

    public Note(String title, String note, String timer, String dateCurrent, int color, byte[] image) {
        this.title = title;
        this.note = note;
        this.timer = timer;
        this.dateCurrent = dateCurrent;
        this.color = color;
        this.image = image;
    }
    public Note(String title, String note, String timer, String dateCurrent, byte[] image) {
        this.title = title;
        this.note = note;
        this.timer = timer;
        this.dateCurrent = dateCurrent;
        this.image = image;
    }

    public Note(String title, String note, String dateCurrent, int color) {
        this.title = title;
        this.note = note;
        this.dateCurrent = dateCurrent;
        this.color = color;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}