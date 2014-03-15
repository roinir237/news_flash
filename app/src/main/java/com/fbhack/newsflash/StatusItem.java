package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by roinir on 15/03/2014.
 */
public class StatusItem extends CardItem{
    private final String status;
    private final Bitmap pic;

    public StatusItem(Context context, Bitmap profilePic, String status) {
        super(context);
        this.pic = profilePic;
        this.status = status;
    }

    @Override
    public View getView() {
        View card = LayoutInflater.from(getContext()).inflate(R.layout.status_card, null);

        ImageView profilePic = (ImageView) card.findViewById(R.id.profile_pic);
        profilePic.setImageBitmap(this.pic);

        TextView statusField = (TextView) card.findViewById(R.id.status_field);
        statusField.setText(this.status);

        return card;
    }
}
