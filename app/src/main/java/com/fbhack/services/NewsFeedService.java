package com.fbhack.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;

import java.util.Arrays;

public class NewsFeedService extends IntentService {

    public NewsFeedService() {
        super("NewsFeedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        


        int i = 0;
    }

}
