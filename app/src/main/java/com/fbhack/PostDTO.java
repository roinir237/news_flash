package com.fbhack;

import android.graphics.Bitmap;

import com.facebook.model.GraphObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shaundowling on 15/03/2014.
 */
public interface PostDTO {
    public String getName();
    public String getStatus();

    public Bitmap getProfilePicture();
    public Bitmap getPostedImage();

    public List<String> getLikers();
    public List<String> getCommenters();

    public Date getCreatedTime();
    public Date getUpdatedTime();

    public double getImportance();
    public void setImportance(double imp);

    public boolean hasLoaded();
}
