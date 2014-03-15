package com.fbhack.services;

import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.fbhack.NewsPost;
import com.fbhack.PostDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shaundowling on 15/03/2014.
 */
public class NewsFetcher {

    private Map<String, PostDTO> newsFeed = new HashMap<String, PostDTO>();

    public NewsFetcher() {
        startFetching();
    }

    /**
     * At the moment this just does one fetch.
     */
    private void startFetching() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // If we want to put extra options
//                Bundle options = new Bundle();
//                options.putInt("limit", 10);

                    Log.d(NewsFetcher.class.toString(), "Awaiting response");
                    Response res = new Request(
                            Session.getActiveSession(),
                            "/me/home",
                            null,
                            HttpMethod.GET
                    ).executeAndWait();

                    Log.d(NewsFetcher.class.toString(), "Received response");

                    if (res.getError() == null)
                        processResponse(res);
                    else
                        Log.e(NewsFetcher.class.toString(), "Error getting response");

                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {

                    }

                    Log.d(NewsFetcher.class.toString(), "Finished loading feed");

                    break;
                }
            }
        }).start();
    }

    private void processResponse(Response response) {
        JSONArray data = (JSONArray) response.getGraphObject().getProperty("data");

        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject post = data.getJSONObject(i);
                processPost(post);
            }
        } catch (JSONException exc) {
            Log.e(NewsFetcher.class.toString(), exc.getLocalizedMessage());
        }
    }

    private void processPost(JSONObject post) throws JSONException {
        String id = (String) post.get("id");

        if (newsFeed.keySet().contains(id))
            return;

        newsFeed.put(id, new NewsPost(post));
    }

    public List<PostDTO> getPosts() {
        List<PostDTO> posts = new LinkedList<PostDTO>();

        for (String id : newsFeed.keySet()) {
            PostDTO post = newsFeed.get(id);

            if (!post.hasLoaded())
                continue;

            posts.add(post);
        }

        return posts;
    }
}
