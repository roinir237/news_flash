package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();

        if (!spritzView) {
            windowManager.removeView(this.view);
            windowManager.addView(spritzerTextView, params);
        } else {
            windowManager.removeView(spritzerTextView);
//        params.width = 200;
            windowManager.addView(this.view, params);
        }

        spritzView = !spritzView;
    }
}
