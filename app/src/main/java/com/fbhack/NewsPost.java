package com.fbhack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
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


    public NewsPost(final JSONObject post) throws JSONException {
        Log.d(this.getClass().toString(), "Loading NewsPost");

        name = post.getJSONObject("from").getString("name");

        status = post.has("message") ? post.getString("message") : "";

        if (post.has("likes"))
            loadLikers(post.getJSONObject("likes").getJSONArray("data"));

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

    private void loadLikers(JSONArray likersJson) throws JSONException {

        for (int i = 0; i < likersJson.length(); i++) {
            JSONObject liker = likersJson.getJSONObject(i);
            likers.add(liker.getString("name"));
        }
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
    public double getImportance() {
        return likers.size();
    }

    @Override
    public boolean hasLoaded() {
        return hasImage ? image != null : profilePic != null;
    }
}
