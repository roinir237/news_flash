package com.fbhack.newsflash;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.fbhack.services.NewsFeedService;

import android.widget.TextView;

import com.fbhack.newsflash.R;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
        //TextView spritz = (TextView) findViewById(R.id.spritz_view);
        //ArrayList<CharSequence> arrayList = new ArrayList<CharSequence>();
       // populateArrayList(arrayList);
        //countDown(spritz, arrayList);
        startService(new Intent(this, FlashHead.class));

        Button buttonLoginActivity = (Button) findViewById(R.id.button);
        buttonLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginUsingActivityActivity.class);
                startActivity(intent);
            }
        });

        Button buttonService = (Button) findViewById(R.id.service);
        buttonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsFeedService.class);
                startService(intent);
            }
        });

//        mySession();
    }

    private void mySession() {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();

        if (session == null) {
            session = new Session(this);
        }

        Session.setActiveSession(session);
        if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
            Session.OpenRequest request = new Session.OpenRequest(this);
            request.setPermissions(Arrays.asList("user_groups"));
            request.setCallback(new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    if (session.isOpened())
                        startSessionIntent(session);
                }
            });

            session.openForRead(request);
        }

        if (session.isOpened())
            startSessionIntent(session);
    }

    private void startSessionIntent(Session session) {
        Session.setActiveSession(session);
        Intent intent = new Intent(this, NewsFeedService.class);
        intent.putExtra("session", session);

        this.startService(intent);
    }

    private void populateArrayList(ArrayList<CharSequence> arrayList) {
        arrayList.add("Make");
        arrayList.add("sure");
        arrayList.add("you've");
        arrayList.add("installed");
        arrayList.add("the");
        arrayList.add("following");
        arrayList.add("from");
        arrayList.add("the");
        arrayList.add("Android");
        arrayList.add("SDK");
        arrayList.add("Manager");
        arrayList.add("before");
        arrayList.add("building");
    }

    private void countDown(final TextView spritz, final ArrayList<CharSequence> arrayList) {
        CountDownTimer timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                spritz.setText(arrayList.get(0));
                arrayList.remove(0);
                if (arrayList.size() != 0)
                    countDown(spritz, arrayList);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
