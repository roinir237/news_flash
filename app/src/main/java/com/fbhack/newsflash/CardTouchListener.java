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
                if(motionEvent.getEventTime() - downTime < tapDownTime){
                    onTap();
                } else {
                    float final_x = motionEvent.getRawX();
                    float final_y = motionEvent.getRawY();
                    Log.d("listenerev", String.format("Mouse up at %fx%f", final_x, final_y));
                    if (Math.abs(final_x - start_x) >= THRESHOLD || Math.abs(final_y - start_y) >= THRESHOLD) {
                        Log.d("listenerev", "THRESHOLD MET");
                        close();
                    } else {
                        Log.d("listenerv", "THRESHOLD NOT MET");
                    }
                }
                return true;
        }

        return false;
    }

    protected abstract void close();
    protected abstract void onTap();
}