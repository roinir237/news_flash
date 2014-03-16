package com.fbhack.newsflash;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;

public class FlashHead extends Service {
    private static final String TAG = "FlashHead";
    private WindowManager windowManager;
    private View flashHead;

    private ArrayList<CardItem> cards;

    public FlashHead() {
        cards = new ArrayList<CardItem>();
    }

    public void onCreate(){
        super.onCreate();
        LayoutInflater inflater = LayoutInflater.from(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        flashHead = inflater.inflate(R.layout.flash_head, null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(flashHead, params);
        final ImageView flashIcon = (ImageView) flashHead.findViewById(R.id.flash_head);
        flashIcon.setOnTouchListener(new FlashHeadTouchListener(flashHead){
            private boolean fullscreen = false;

            @Override
            public void onDrag(int xf, int yf) {
                if (!fullscreen) {
                    params.x = xf;
                    params.y = yf;
                    windowManager.updateViewLayout(flashHead, params);

                } else {
                    toggleMode();
                }
            }

            @Override
            public void onTap() {
                toggleMode();
            }

            @Override
            public void onRelease() {
                final int initialX = params.x;
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                final int finalX = screenWidth/2 < params.x ? screenWidth:0;

                SpringSystem springSystem = SpringSystem.create();
                Spring spring = springSystem.createSpring();
                spring.addListener(new SimpleSpringListener() {

                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        int xi = (int) (initialX - value*(initialX - finalX));
                        params.x = xi;
                        windowManager.updateViewLayout(flashHead, params);
                    }
                });

                spring.setEndValue(1);
            }

            public void toggleMode(){
                if(!fullscreen){
                    final int initialX = params.x;
                    final int initialY = params.y;
                    final int finalX = getResources().getDisplayMetrics().widthPixels;
                    final int finalY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

                    SpringSystem springSystem = SpringSystem.create();
                    Spring spring = springSystem.createSpring();
                    spring.addListener(new SimpleSpringListener() {

                        @Override
                        public void onSpringUpdate(Spring spring) {
                            float value = (float) spring.getCurrentValue();
                            int xi = (int) (initialX - value * (initialX - finalX));
                            int yi = (int) (initialY - value * (initialY - finalY));
                            params.x = xi;
                            params.y = yi;
                            windowManager.updateViewLayout(flashHead, params);
                        }

                        @Override
                        public void onSpringAtRest(Spring spring) {
                            super.onSpringAtRest(spring);
                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                            int sheight = displaymetrics.heightPixels;
                            int swidth = displaymetrics.widthPixels;
                            Bitmap pic  = BitmapFactory.decodeResource(getResources(),R.drawable.test);
                            addStatusCard(pic,"making progress @londonFbHackathon",swidth/2 - swidth/6,sheight/2 - sheight/6,swidth/3,sheight/3);
                        }

                    });

                    spring.setEndValue(1);

                    params.dimAmount = 0.8f;
                    params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    windowManager.updateViewLayout(flashHead, params);
                    fullscreen = true;

                } else {
                    params.flags ^= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    windowManager.updateViewLayout(flashHead, params);
                    removeAllCards();
                    fullscreen = !fullscreen;
                }
            }
        });
    }

    public void addStatusCard(Bitmap pic, String status,int x, int y, int w, int h){
        StatusItem card = new StatusItem(this,pic,status);
        card.setDims(h,w);
        card.setPosition(x,y);
        cards.add(card);
        card.addCard(windowManager);
    }

    public void removeAllCards(){
        for(CardItem card : cards){
            card.removeCard(windowManager);
        }

        cards = new ArrayList<CardItem>();
    }

    public void onDestroy(){
        super.onDestroy();

        if(flashHead!= null) windowManager.removeView(flashHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
