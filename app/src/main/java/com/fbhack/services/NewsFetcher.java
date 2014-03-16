package com.fbhack.services;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.fbhack.NewsPost;
import com.fbhack.PostDTO;

import org.apache.http.util.ExceptionUtils;
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
public class NewsFetcher extends Handler {

    private Map<String, PostDTO> newsFeed = new HashMap<String, PostDTO>();

    public NewsFetcher(Looper looper) {
        super(looper);
    }


    @Override
    public void handleMessage(Message msg) {
        startFetching();
    }
    /**
     * At the moment this just does one fetch.
     */
    public void startFetching() {

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
    }

    private void processResponse(Response response) {
        JSONArray data = (JSONArray) response.getGraphObject().getProperty("data");


        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject post = data.getJSONObject(i);
                processPost(post);
            } catch (JSONException exc) {
                Log.e(NewsFetcher.class.toString(), exc.getLocalizedMessage());
            }
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
