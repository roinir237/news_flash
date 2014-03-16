package com.fbhack.processing;

import com.fbhack.PostDTO;

/**
 * Created by roinir on 16/03/2014.
 */
public class Block {
    public int x;
    public int y;
    public int w;
    public int h;
    public PostDTO post;

    public Block(PostDTO post, int x, int y, int w, int h) {
        this.post = post;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}