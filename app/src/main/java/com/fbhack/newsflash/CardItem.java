package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

/**
 * Created by roinir on 15/03/2014.
 */
public abstract class CardItem {
    private WindowManager.LayoutParams params;
    private Context mContext;
    private View view;

    protected abstract View getView();

    public CardItem(Context context){
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    public WindowManager.LayoutParams getParams(){
        return params;
    }

    public void setDims(int h, int w){
        fitInScreen(params.x,params.y,w,h);
    }

    public void setPosition(int x, int y){
        fitInScreen(x,y,params.width,params.height);
    }

    private void fitInScreen(int x, int y, int w, int h){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        int sheight = displaymetrics.heightPixels;
        int swidth = displaymetrics.widthPixels;

        w = w > swidth ? swidth:w;
        h = h > sheight ? sheight:h;

        x = x < 0 ? 0:x;
        x = x + w > swidth ? w-swidth:x;
        y = y < 0 ? 0:y;
        y = y + h > sheight ? h - sheight:y;

        params.y = y;
        params.x = x;
        params.width = w;
        params.height = h;
    }

    public void addCard(final WindowManager windowManager) {
        if(view != null) {
            removeCard(windowManager);
        }


        final WindowManager.LayoutParams params = getParams();
        view = getView();
        final int finalWidth = params.width;
        final int finalHeight = params.height;
        final int finalX = params.x;
        final int finalY = params.y;

        params.width = 0;
        params.height = 0;
        params.x = finalX + finalWidth/2;
        params.y = finalY + finalHeight/2;

        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();

        windowManager.addView(view,params);

        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();

                int width = (int) ((value + 1) * 0.5 * finalWidth);
                int height = (int) ((value + 1) * 0.5 * finalHeight);

                int x = (int) (finalX + (finalWidth - width)/2);
                int y = (int) (finalY + (finalHeight - height)/2);

                params.width = width;
                params.height = height;
                params.x = x;
                params.y = y;
                windowManager.updateViewLayout(view, params);
            }
        });

        spring.setEndValue(1);
    }

    public void removeCard(WindowManager windowManager) {
        windowManager.removeView(view);
    }
}
