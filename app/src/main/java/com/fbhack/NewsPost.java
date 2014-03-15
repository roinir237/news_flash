package com.fbhack;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private List<String> likers = new LinkedList<String>();
    private double importance;


    public NewsPost(JSONObject post) throws JSONException {
        Log.d(this.getClass().toString(), "Loading NewsPost");

        name = post.getJSONObject("from").getString("name");

        status = post.has("message") ? post.getString("message") : "";

        if (post.has("likes"))
            loadLikers(post.getJSONObject("likes").getJSONArray("data"));

        String fromId = post.getJSONObject("from").getString("id");
        String profUrl = "http://graph.facebook.com/" + fromId + "/picture";

        loadImageAsync(profilePic, profUrl);

        if (post.has("picture")) {
            hasImage = true;
            loadImageAsync(image, post.getString("picture"));
        }
    }

    private void loadLikers(JSONArray likersJson) throws JSONException {

        for (int i = 0; i < likersJson.length(); i++) {
            JSONObject liker = likersJson.getJSONObject(i);
            likers.add(liker.getString("name"));
        }
    }

    private void loadImageAsync(Bitmap bitmap, String url) {

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
    public double getImportance() {
        return likers.size();
    }

    @Override
    public boolean hasLoaded() {
        return hasImage ? image != null : profilePic != null;
    }
}
