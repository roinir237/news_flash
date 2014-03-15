package com.fbhack.newsflash;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by roinir on 13/03/2014.
 */
public abstract class FlashHeadTouchListener implements View.OnTouchListener {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private final View parentView;
    private long downTime;
    private static final long tapDownTime = 250;

    public FlashHeadTouchListener(View parentView) {
        this.parentView = parentView;
   }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        WindowManager.LayoutParams params;

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = motionEvent.getEventTime();
                params = (WindowManager.LayoutParams) parentView.getLayoutParams();
                initialX = params.x;
                initialY = params.y;
                initialTouchX = motionEvent.getRawX();
                initialTouchY = motionEvent.getRawY();
                return true;

            case MotionEvent.ACTION_UP:
                if(motionEvent.getEventTime() - downTime < tapDownTime)
                    onTap();
                else{
                    onRelease();
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(motionEvent.getEventTime() - downTime > tapDownTime){
                    int xf = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                    int yf = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                    onDrag(xf,yf);
                }
                return true;
        }
        return false;
    }

    public abstract void onDrag(int xf, int yf);
    public abstract void onTap();
    public abstract void onRelease();
}
