package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by roinir on 15/03/2014.
 */
public class StatusItem extends CardItem{
    private final String status;
    private final Bitmap pic;

    public StatusItem(Context context,CardsChangedCallback callback, Bitmap profilePic, String status) {
        super(context,callback);
        this.pic = profilePic;
        this.status = status;
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
}
