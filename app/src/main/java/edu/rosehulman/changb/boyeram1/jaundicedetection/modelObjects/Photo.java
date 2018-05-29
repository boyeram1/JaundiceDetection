package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

public class Photo {

    private String key;
    private String fileName;
    private Bitmap imageBitmap;

    public Photo() {

    }

    public Photo(String fileName, Bitmap imageBitmap) {
        this.fileName = fileName;
        this.imageBitmap = imageBitmap;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Exclude
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setValues(Photo photo) {
        this.fileName = photo.fileName;
        this.imageBitmap = photo.imageBitmap;
    }
}
