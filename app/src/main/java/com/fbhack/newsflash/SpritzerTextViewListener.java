package com.fbhack.newsflash;

import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by Dorota on 16/03/14.
 */
public class SpritzerTextViewListener implements OnClickListener{
    CountDownTimer timer;

    public SpritzerTextViewListener(CountDownTimer timer) {
        this.timer = timer;
    }

    @Override
    public void onClick(View v) {
        timer.cancel();
    }
}
