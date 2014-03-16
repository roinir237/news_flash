package com.fbhack.newsflash;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dorota on 16/03/14.
 */
public class Spritzer {
    private static final int BASE_TIME = 180;
    CountDownTimer timer;
    SpritzerTextView spritz;
    HashSet<String> stopWords = new HashSet<String>();

    protected Spritzer(SpritzerTextView spritz) {
        String csvStops = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";

        for (String s : csvStops.split(","))
            stopWords.add(s);

        ArrayList<String> arrayList = populateArrayList();
        spritz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
            }
        });
        spritz.setText("");
        this.spritz = spritz;
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

    public void countDown(final List<String> list) {
        final List<String> arrayList = new LinkedList<String>();

        for (String s : list) {
            if (s.length() > 0)
                arrayList.add(s);
        }

        if (arrayList == null || arrayList.size() == 0)
            return;

        final String word = arrayList.get(0);
        int time = getCountdownForString(word);
        timer = new CountDownTimer(time, time) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                SpannableStringBuilder builder = redHighlight(word);
                spritz.setText(builder, TextView.BufferType.SPANNABLE);
                arrayList.remove(0);
                if (arrayList.size() != 0)
                    countDown(arrayList);
            }
        }.start();
    }

    private int getCountdownForString(String s) {
        int time = BASE_TIME;

        if (s.contains(","))
            time *= 1.3;
        else if (s.contains("."))
            time *= 1.7;

        if (stopWords.contains(s))
            time *= 0.6;
        else if (s.length() > 5)
            time *= 1.0;


        return time;
    }

    public CountDownTimer getTimer() {
        return this.timer;
    }

}
