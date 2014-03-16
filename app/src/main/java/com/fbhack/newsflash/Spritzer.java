package com.fbhack.newsflash;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Dorota on 16/03/14.
 */
public class Spritzer {
    CountDownTimer timer;

    protected Spritzer(SpritzerTextView spritz) {
        ArrayList<String> arrayList = populateArrayList();
        spritz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
            }
        });
        countDown(spritz, arrayList);
    }

    /**
     * Create an array list of words to be displayed.
     * @return the created list
     */
    private ArrayList<String> populateArrayList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Make");
        arrayList.add("sure");
        arrayList.add("you've");
        arrayList.add("installed");
        arrayList.add("the");
        arrayList.add("following");
        arrayList.add("from");
        arrayList.add("the");
        arrayList.add("Android");
        arrayList.add("SDK");
        arrayList.add("Manager");
        arrayList.add("before");
        arrayList.add("building.");
        return arrayList;
    }

    /**
     * For a given word, pick the character in the middle, make it red, add spacing.
     * @param word - string to be displayed
     * @return processed word (middle letter red, spacing added at the beginning)
     */
    private SpannableStringBuilder redHighlight(String word) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        // the position of the red character in the given string (word)
        int midPosition = (word.length() - 1) / 2;

        if (midPosition < 6) {
            for (int i = 0; i < 6 - midPosition; i++) {
                builder.append(" ");
            }
        }

        String prefix = word.substring(0, midPosition);
        builder.append(prefix);

        String red = word.substring(midPosition, midPosition + 1);
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
        builder.append(redSpannable);

        String suffix = word.substring(midPosition + 1, word.length());
        builder.append(suffix);

        return builder;
    }

    private void countDown(final SpritzerTextView spritz, final ArrayList<String> arrayList) {
        final String word = arrayList.get(0);

        timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                SpannableStringBuilder builder = redHighlight(word);
                spritz.setText(builder, TextView.BufferType.SPANNABLE);
                arrayList.remove(0);
                if (arrayList.size() != 0)
                    countDown(spritz, arrayList);
            }
        }.start();
    }

    public CountDownTimer getTimer() {
        return this.timer;
    }
}
