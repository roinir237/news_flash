package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by roinir on 15/03/2014.
 */
public class StatusItem extends CardItem {
    private final String status;
    private final Bitmap pic;

    private View spritzerTextView;
    private boolean spritzView = false;

    public StatusItem(Context context,CardsChangedCallback callback, Bitmap profilePic, String status) {
        super(context,callback);
        this.pic = profilePic;
        this.status = status;


        spritzerTextView = LayoutInflater.from(getContext()).inflate(R.layout.spritz_view, null);
    }

    @Override
    public View getView() {

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) this.getParams();

        float previewImageLimit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics());

        View card = LayoutInflater.from(getContext()).inflate(R.layout.status_card, null);

        ImageView profilePic = (ImageView) card.findViewById(R.id.profile_pic);
        profilePic.setImageBitmap(this.pic);

        TextView statusField = (TextView) card.findViewById(R.id.status_field);
        statusField.setText(this.status);

        return card;
    }

    public void swapView(WindowManager windowManager) {
        if (!spritzView) {
            windowManager.removeView(this.view);


            DisplayMetrics displaymetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displaymetrics);
            int sheight = displaymetrics.heightPixels;
            int swidth = displaymetrics.widthPixels;

            int height = 250;
            int width = 900;

            WindowManager.LayoutParams newParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            newParams.height = height;
            newParams.width = width;

            newParams.x = swidth/2 - width / 2;
            newParams.y = sheight/2 - height / 2;
            newParams.gravity = Gravity.TOP | Gravity.LEFT;

            windowManager.addView(spritzerTextView, newParams);

            SpritzerTextView textView = (SpritzerTextView) spritzerTextView.findViewById(R.id.spritz_view_2);
            textView.setTextSize(30);
            new Spritzer(textView);

        } else {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
            windowManager.removeView(spritzerTextView);

            windowManager.addView(this.view, params);
        }

        spritzView = !spritzView;
    }
}
