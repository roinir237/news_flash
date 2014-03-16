package com.fbhack.processing;

import android.util.Log;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleImage {

    public  static  void main(String... args) {
        String url = fetch("cat");
        Log.d("CAT", url != null ? url : "nothing, sorry");
    }

    public static String fetch(String word) {

        String url = null;
        try {
            url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            return  null;
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){ }
        }

        try {
            JSONObject res = new JSONObject(result);
            JSONArray arr = res.getJSONObject("responseData").getJSONArray("results");
            if (arr.length() == 0) return null;
            return arr.getJSONObject(0).getString("unescapedUrl");
        } catch (Exception e) {
            return null;
            // something bad happened
        }
    }
}
