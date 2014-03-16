package com.fbhack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shaundowling on 15/03/2014.
 */
public class NewsPost implements PostDTO {
    private String name;

    private String status;

    private Bitmap profilePic;

    boolean hasImage;
    private Bitmap image;

    private List<String> likers;
    private List<String> commenters;
    private double importance;

    Date createdDate;
    Date updatedDate;


    public NewsPost(JSONObject post) throws JSONException {
        likers = new LinkedList<String>();
        commenters = new LinkedList<String>();
        Log.d(this.getClass().toString(), "Loading NewsPost");

        name = post.getJSONObject("from").getString("name");

        status = post.has("message") ? post.getString("message") : "";

        if (post.has("likes"))
            likers = loadUsers(post, "likes");

        if (post.has("comments"))
            commenters = loadUsers(post, "comments");

        parseDates(post);

        fetchImages(post);
    }

    private List<String> loadUsers(JSONObject post, String key) throws JSONException {
        JSONArray arr = post.getJSONObject(key).getJSONArray("data");

        List<String> output = new LinkedList<String>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject liker = arr.getJSONObject(i);
            output.add(liker.getString("name"));
        }

        return output;
    }

    private void parseDates(JSONObject post) throws JSONException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");

        try {
            createdDate = format.parse(post.getString("created_time"));
            updatedDate = format.parse(post.getString("updated_time"));
        } catch (Exception e) {
            Log.e(NewsPost.class.toString(), e.getMessage());
        }
    }

    private void fetchImages(final JSONObject post) throws JSONException {
        String fromId = post.getJSONObject("from").getString("id");
        final String profUrl = "http://graph.facebook.com/" + fromId + "/picture";

        new Thread(new Runnable() {

            @Override
            public void run() {
                profilePic = fetchImage(profUrl);

                if (post.has("picture")) {
                    hasImage = true;

                    try {
                        image = fetchImage(post.getString("picture"));
                    } catch (Exception e) {

                    }
                }

                Log.e(NewsPost.class.toString(), "Finished loading images");
            }
        }).start();
    }

    private Bitmap fetchImage(String url) {
        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public Bitmap getProfilePicture() {
        return profilePic;
    }

    @Override
    public Bitmap getPostedImage() {
        return image;
    }

    @Override
    public List<String> getLikers() {
        return likers;
    }

    @Override
    public List<String> getCommenters() {
        return commenters;
    }

    @Override
    public double getImportance() {
        Date mostRecent = createdDate.compareTo(updatedDate) > 0 ? createdDate : updatedDate;
        long age = new Date().getTime() - mostRecent.getTime();
        age /= 1000*60; // to minutes
        if (age == 0) { age = 1; }
        return 0.2 * likers.size() + 0.4 * commenters.size() + 0.1 * (hasImage ? 1.0 : 0) + 0.3 * age;
    }

    @Override
    public boolean hasLoaded() {
        return hasImage ? image != null : profilePic != null;
    }

    @Override
    public Date getCreatedTime() {
        return null;
    }

    @Override
    public Date getUpdatedTime() {
        return null;
    }
}
