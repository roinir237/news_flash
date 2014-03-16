package com.fbhack.newsflash;

import android.graphics.Point;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by roinir on 15/03/2014.
 */
public abstract class CardTouchListener implements View.OnTouchListener, View.OnDragListener {
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
                return true;

            case MotionEvent.ACTION_UP:
                if(motionEvent.getEventTime() - downTime < tapDownTime){
                    onTap();
                } else {
                    float final_x = motionEvent.getRawX();
                    float final_y = motionEvent.getRawY();
                    if (Math.abs(final_x - start_x) >= THRESHOLD || Math.abs(final_y - start_y) >= THRESHOLD) {
                        close();
                    }
                }
                return true;
        }

        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                start_x = event.getX();
                start_y = event.getY();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                break;
        }

        }
    }

    protected abstract void close();
    protected abstract void onTap();
}