package com.fbhack.newsflash;

import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.fbhack.PostDTO;
import com.fbhack.processing.Block;
import com.fbhack.processing.Packing;
import com.fbhack.services.NewsFetcher;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlashHead extends Service implements CardItem.CardsChangedCallback {


    private static final String TAG = "FlashHead";
    private WindowManager windowManager;
    private View flashHead;
    private NewsFetcher newsFetcher;
    private Looper mServiceLooper;
    private View overlay;
    private WindowManager.LayoutParams overlayParams;
    private ArrayList<CardItem> cards;

    public FlashHead() {
        cards = new ArrayList<CardItem>();
    }

    public void onCreate(){
        super.onCreate();

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        newsFetcher = new NewsFetcher(mServiceLooper);
        Message msg = newsFetcher.obtainMessage();
        msg.arg1 = 1;
        newsFetcher.sendMessage(msg);

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

                            List<PostDTO> posts = newsFetcher.getPosts();
                            showPosts(posts);
                            windowManager.removeView(flashHead);
                            windowManager.addView(flashHead,params);
                        }

                    });

                    spring.setEndValue(1);

                    fullscreen = true;

                } else {
                    removeAllCards();
                    fullscreen = !fullscreen;
                }
            }
        });
    }

    public void showPosts(List<PostDTO> posts){
        if(overlay == null){
            overlay = LayoutInflater.from(this).inflate(R.layout.overlay, null);
            overlayParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        windowManager.addView(overlay,overlayParams);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int sheight = displaymetrics.heightPixels;
        int swidth = displaymetrics.widthPixels;
        Block[] blocks = Packing.calc(swidth,sheight,posts);

        float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

        for(Block block : blocks){
            Log.d("FlashHead",String.format("%d, %d, %d, %d",block.x,block.y,block.w,block.h));
            int width = (int) (block.w - margin*2);
            int height = (int) (block.h - margin*2);
            int x = (int)(block.x + margin);
            int y = (int) (block.y + margin);

            addStatusCard(block.post.getProfilePicture(), block.post.getStatus(),
                    x,y,width,height);
        }
    }

    public void addStatusCard(Bitmap pic, String status,int x, int y, int w, int h){
        StatusItem card = new StatusItem(this,this,pic,status);
        card.setDims(h,w);
        card.setPosition(x,y);
        cards.add(card);
        card.addCard(windowManager);
    }

    public void removeAllCards(){
        windowManager.removeView(overlay);
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

    @Override
    public void cardCentered(CardItem card) {
      windowManager.removeView(flashHead);
      windowManager.addView(flashHead,flashHead.getLayoutParams());
    }
}
