package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private final Bitmap previewImage;

    public StatusItem(Context context,CardsChangedCallback callback, Bitmap profilePic, String status, Bitmap previewImage) {
        super(context,callback);
        this.pic = profilePic;
        this.status = status;
        this.previewImage = previewImage;
    }

    @Override
    public View getView() {

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) this.getParams();

        float previewImageLimit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getContext().getResources().getDisplayMetrics());
        View card;
        if(params.height > previewImageLimit){
            card = LayoutInflater.from(getContext()).inflate(R.layout.status_card, null);
            if (this.previewImage != null){
                ImageView profilePic = (ImageView) card.findViewById(R.id.preview_pic);
                profilePic.setImageBitmap(this.previewImage);
            }

        }else{
            card = LayoutInflater.from(getContext()).inflate(R.layout.status_card_long, null);
            params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getContext().getResources().getDisplayMetrics());
        }

        ImageView profilePic = (ImageView) card.findViewById(R.id.profile_pic);
        profilePic.setImageBitmap(this.pic);

        TextView statusField = (TextView) card.findViewById(R.id.status_field);
        statusField.setText(this.status);

        return card;
    }
}
