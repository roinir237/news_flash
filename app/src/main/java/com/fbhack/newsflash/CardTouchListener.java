package com.fbhack.newsflash;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by roinir on 15/03/2014.
 */
public abstract class CardTouchListener implements View.OnTouchListener {
    private long downTime;
    private static final long tapDownTime = 250;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = motionEvent.getEventTime();
                return true;

            case MotionEvent.ACTION_UP:
                if(motionEvent.getEventTime() - downTime < tapDownTime){
                    onTap();
                }
                return true;
        }

        return false;
    }

    protected abstract void onTap();
}