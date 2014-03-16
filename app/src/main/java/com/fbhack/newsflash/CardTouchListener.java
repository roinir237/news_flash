package com.fbhack.newsflash;

import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by roinir on 15/03/2014.
 */
public abstract class CardTouchListener implements View.OnTouchListener {
    private long downTime;
    private static final long tapDownTime = 250;

    private float start_x;
    private float start_y;

    public  static  float THRESHOLD = 20;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = motionEvent.getEventTime();
                start_x = motionEvent.getRawX();
                start_y = motionEvent.getRawY();
                Log.d("listenerev", String.format("Mouse down at %fx%f", start_x, start_y));
                return true;

            case MotionEvent.ACTION_UP:
                float final_x = motionEvent.getRawX();
                float final_y = motionEvent.getRawY();
                Log.d("listenerev", String.format("Mouse up at %fx%f", final_x, final_y));
                if (Math.sqrt(Math.pow(final_x - start_x, 2)+Math.pow(final_y-start_y,2)) >= THRESHOLD) {
                    Log.d("listenerev", "THRESHOLD MET");
                    close();
                } else if(motionEvent.getEventTime() - downTime < tapDownTime){
                    onTap();
                }
                return true;
        }

        return false;
    }

    protected abstract void close();
    protected abstract void onTap();
}