package com.fbhack.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbhack.PostDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by roinir on 15/03/2014.
 */
public class StatusItem extends CardItem {

    private final PostDTO post;
    private View spritzerTextView;
    private boolean spritzView = false;

    public StatusItem(Context context,CardsChangedCallback callback, PostDTO post) {
        super(context,callback);
        this.post = post;

        spritzerTextView = LayoutInflater.from(getContext()).inflate(R.layout.spritz_view, null);
    }

    public PostDTO getPost() {
        return this.post;
    }

    @Override
    public View getView() {

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) this.getParams();

        float previewImageLimit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getContext().getResources().getDisplayMetrics());
        View card;
        if(params.height > previewImageLimit){
            card = LayoutInflater.from(getContext()).inflate(R.layout.status_card, null);
            if (this.post.getPostedImage() != null){
                ImageView profilePic = (ImageView) card.findViewById(R.id.preview_pic);
                profilePic.setImageBitmap(this.post.getPostedImage());
            }

        }else{
            card = LayoutInflater.from(getContext()).inflate(R.layout.status_card_long, null);
            params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getContext().getResources().getDisplayMetrics());
        }

        ImageView profilePic = (ImageView) card.findViewById(R.id.profile_pic);
        profilePic.setImageBitmap(this.post.getProfilePicture());

        TextView statusField = (TextView) card.findViewById(R.id.status_field);
        statusField.setText(this.post.getStatus());

        return card;
    }

    @Override
    protected void closePost() {
        Log.d("listenerev", "closePost() called");
        this.post.setRead(true);
        this.getCallback().cardRemoved();
    }

    public void swapView(WindowManager windowManager) {
        if (!spritzView && this.post.getStatus() != null && this.post.getStatus().length() > 0) {
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

            Spritzer spritzer = new Spritzer(textView);

            List<String> spritzText = new LinkedList<String>();

            for (String s : this.post.getStatus().split(" "))
                spritzText.add(s);

            Map<String, String> comments = this.post.getCommenters();

            for (String s : comments.keySet()) {
                String comment = comments.get(s);

                spritzText.add(" ");
                spritzText.addAll(Arrays.asList(s.split(" ")));
                spritzText.add("said");

                spritzText.addAll(Arrays.asList(comment.split(" ")));
            }

            spritzer.countDown(spritzText);

            spritzView = !spritzView;

        } else if (spritzView) {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
            windowManager.removeView(spritzerTextView);

            windowManager.addView(this.view, params);

            spritzView = !spritzView;
        }


    }
}
