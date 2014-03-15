package com.fbhack.newsflash;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spritz_item);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        TextView spritz = (TextView) findViewById(R.id.spritz_view);
        ArrayList<CharSequence> arrayList = new ArrayList<CharSequence>();
        populateArrayList(arrayList);
        countDown(spritz, arrayList);

       // startService(new Intent(this, FlashHead.class));
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
            View rootView = inflater.inflate(R.layout.spritz_item, container, false);
            return rootView;
        }
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
        CountDownTimer timer = new CountDownTimer(10000, 10000) {
            @Override
            public void onTick(long l) {
                spritz.setText(arrayList.get(0));
            }

            @Override
            public void onFinish() {
                arrayList.remove(0);
                if (arrayList.size() != 0)
                    countDown(spritz, arrayList);
            }
        }.start();
    }

}
