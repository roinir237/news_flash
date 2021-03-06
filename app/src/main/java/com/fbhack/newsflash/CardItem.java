package com.fbhack.newsflash;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by roinir on 15/03/2014.
 */
public abstract class CardItem{
    private WindowManager.LayoutParams params;
    private Context mContext;
    private boolean centered = false;
    private int originalX;
    private int originalY;
    protected View view;

    public interface CardsChangedCallback {
        void cardCentered(CardItem card);
        void cardRemoved();
    }

    final private CardsChangedCallback callback;

    protected CardsChangedCallback getCallback() { return this.callback; }

    protected abstract View getView();

    public CardItem(Context context,CardsChangedCallback callback ){
        this.callback = callback;
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

        this.originalX = x;
        this.originalY = y;
    }

    abstract protected void closePost();

    public void addCard(final WindowManager windowManager) {
        if(view != null) {
            removeCard(windowManager);
        }


        final WindowManager.LayoutParams params = getParams();
        view = getView();

        view.setOnTouchListener(new CardTouchListener() {
            @Override
            protected void onTap() {
                if(!centered){
                    centerCard(windowManager);
                    centered = true;
                }
            }

            @Override
            protected void close() {
                Log.d("listeverev", "close() got called");
                closePost();
            }
        });

        final int finalWidth = params.width;
        final int finalHeight = params.height;
        final int finalX = params.x;
        final int finalY = params.y;

//        params.width = 0;
//        params.height = 0;
//        params.x = finalX + finalWidth/2;
//        params.y = finalY + finalHeight/2;

        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();

        windowManager.addView(view,params);

        ImageView preview = (ImageView) view.findViewById(R.id.preview_pic);
        if(preview != null){
            float bottomHeight  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getContext().getResources().getDisplayMetrics());

            int previewWidth = ((BitmapDrawable)preview.getDrawable()).getBitmap().getWidth();
            int previewHeight = ((BitmapDrawable)preview.getDrawable()).getBitmap().getHeight();


            int containerWidth = finalWidth;
            int containerHeight = (int) (finalHeight);

            int newHeight;
            int newWidth;

            Bitmap previewBmp = ((BitmapDrawable)preview.getDrawable()).getBitmap();
            int h = previewBmp.getHeight();
            int w = previewBmp.getWidth();

            if(previewBmp.getHeight() < previewBmp.getWidth()){
                newHeight = containerHeight;
                newWidth = containerHeight*previewWidth/previewHeight;
            } else {
                newWidth = containerWidth;
                newHeight = containerWidth*previewHeight/previewWidth;
            }

            ViewGroup.LayoutParams imageParams = preview.getLayoutParams();
            imageParams.width = newWidth;
            imageParams.height = newHeight;
            preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            preview.setLayoutParams(imageParams);
            preview.setImageBitmap(Bitmap.createScaledBitmap(previewBmp, newWidth, newHeight, false));


        } else {
            int containerWidth = finalWidth;
            float longLimit  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getContext().getResources().getDisplayMetrics());
            if(longLimit > containerWidth){
                params.height = containerWidth;
                windowManager.updateViewLayout(view,params);

                view.findViewById(R.id.status_field).setVisibility(View.GONE);
                ImageView profilePic = (ImageView) view.findViewById(R.id.profile_pic);
         //       Bitmap previewBmp = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
                ViewGroup.LayoutParams imageParams = profilePic.getLayoutParams();
                imageParams.width = containerWidth;
                imageParams.height = containerWidth;
                profilePic.setLayoutParams(imageParams);
           //     profilePic.setImageBitmap(Bitmap.createScaledBitmap(previewBmp, containerWidth, containerWidth, false));

            }
        }


//        spring.addListener(new SimpleSpringListener() {
//
//            @Override
//            public void onSpringUpdate(Spring spring) {
//                float value = (float) spring.getCurrentValue();
//
//                int width = (int) SpringUtil.mapValueFromRangeToRange(value,0,1,finalWidth*0.7,finalWidth);
//                int height = (int) SpringUtil.mapValueFromRangeToRange(value,0,1,finalHeight*0.7,finalHeight);
//
//                int x = (int) (finalX + (finalWidth - width)/2);
//                int y = (int) (finalY + (finalHeight - height)/2);
//
//                params.width = width;
//                params.height = height;
//                params.x = x;
//                params.y = y;
//
//                try {
//                    windowManager.updateViewLayout(view, params);
//                } catch (Exception e) {
//                    Log.e("CardItem", e.getLocalizedMessage());
//                }
//            }
//        });
//        SpringConfig config = new SpringConfig(50,10);
//        spring.setSpringConfig(config);
//        spring.setEndValue(1);
    }

    public void removeCard(WindowManager windowManager) {
        try {
            windowManager.removeView(view);
        } catch (Exception e) {
            Log.e("CardItem", e.getLocalizedMessage());
        }
    }

    public void centerCard(final WindowManager windowManager){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int sheight = displaymetrics.heightPixels;
        int swidth = displaymetrics.widthPixels;

        final int finalX = swidth/2 - params.width/2;
        final int finalY = sheight/2 - params.height/2;
        final int initialX = params.x;
        final int initialY = params.y;
        windowManager.removeView(view);
        final View overlay = LayoutInflater.from(getContext()).inflate(R.layout.overlay, null);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decenterCard(windowManager,overlay);
            }
        });
        WindowManager.LayoutParams overlayParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowManager.addView(overlay,overlayParams);
        windowManager.addView(view,params);

        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();

                int x = (int) SpringUtil.mapValueFromRangeToRange(value,0,1,initialX,finalX);
                int y = (int) SpringUtil.mapValueFromRangeToRange(value,0,1,initialY,finalY);

                params.x = x;
                params.y = y;
                try {
                    windowManager.updateViewLayout(view, params);
                } catch (Exception e) {
                    Log.e("CardItem", e.getLocalizedMessage());
                }
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);

                CardItem.this.swapView(windowManager);
            }
        });

        windowManager.removeView(view);
        windowManager.addView(view,params);
        callback.cardCentered(this);
        spring.setEndValue(1);
    }

    public abstract void swapView(WindowManager windowManager);

    public void decenterCard(final WindowManager windowManager, View overlay) {
        windowManager.removeView(overlay);

        final int finalX = this.originalX;
        final int finalY = this.originalY;
        final int initialX = params.x;
        final int initialY = params.y;

        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringActivate(Spring spring) {
                super.onSpringActivate(spring);
                
                CardItem.this.swapView(windowManager);
            }

            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();

                int x = (int) SpringUtil.mapValueFromRangeToRange(value,0,1,initialX,finalX);
                int y = (int) SpringUtil.mapValueFromRangeToRange(value,0,1,initialY,finalY);

                params.x = x;
                params.y = y;
                try {
                    windowManager.updateViewLayout(view, params);
                } catch (Exception e) {
                    Log.e("CardItem", e.getLocalizedMessage());
                }
            }
        });

        spring.setEndValue(1);
        centered = false;
    }
}
