package com.example.tam.appnotes.model;

/**
 * Created by tam on 8/8/2017.
 */

public class Picture {
    public int idPicture;
    public int idNote;
    public String linkImage;

    public Picture(int idPicture, int idNote, String linkImage) {
        this.idPicture = idPicture;
        this.idNote = idNote;
        this.linkImage = linkImage;
    }
    public Picture(int idNote, String linkImage) {
        this.idNote = idNote;
        this.linkImage = linkImage;
    }

    public Picture(String linkImage) {
        this.linkImage = linkImage;
    }

    public int getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(int idPicture) {
        this.idPicture = idPicture;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }
}
