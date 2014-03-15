package com.fbhack;

import android.graphics.Bitmap;

import java.util.ArrayList;
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

    public double getImportance();

    public boolean hasLoaded();
}
