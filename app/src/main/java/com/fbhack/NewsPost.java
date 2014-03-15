package com.fbhack;

import android.graphics.Bitmap;

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
        name = post.getJSONObject("from").getString("name");
        status = post.getString("message");

        if (post.has("likes"))
            loadLikers(post.getJSONObject("likes").getJSONArray("data"));

        String fromId = post.getJSONObject("from").getString("id");
        String profUrl = "http://graph.facebook.com/" + fromId + "/picture";

        profilePic = loadImageAsync(profUrl);

        
        image = loadImageAsync(profUrl);
    }

    private void loadLikers(JSONArray likersJson) throws JSONException {

        for (int i = 0; i < likersJson.length(); i++) {
            JSONObject liker = likersJson.getJSONObject(i);
            likers.add(liker.getString("name"));

        }
    }

    private Bitmap loadImageAsync(String url) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public Bitmap getProfilePicture() {
        return null;
    }

    @Override
    public Bitmap getPostedImage() {
        return null;
    }

    @Override
    public List<String> getLikers() {
        return null;
    }

    @Override
    public double getImportance() {
        return 0;
    }

    @Override
    public boolean hasLoaded() {
        return false;
    }
}
