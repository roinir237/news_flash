package com.fbhack.processing;

import java.util.*;
import com.fbhack.*;

class Block {
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

public class Packing {
    public static int optimal_cut(double i1, double i2, int size) {
        double total = i1+i2;
        double cut = i1/total;
        return (int)Math.round(size*cut);
    }

    public static double sumImportance(int from, int to, PostDTO inputs[]) {
        double imp = 0;
        for (int i=from;i<to;i++) {
            imp+=inputs[i].getImportance();
        }
        return imp;
    }

    public static Block[] compute_row(int from, int w, int h, int top_offset, PostDTO inputs[]) {
        Block[] result = new Block[3];

        boolean first_row = from == 0;

        int vcut = optimal_cut(inputs[from].getImportance(), inputs[from+1].getImportance() + inputs[from+2].getImportance(), w);
        int side_width = w-vcut;

        result[0] = new Block(inputs[from], first_row ? 0 : side_width, top_offset, vcut, h);

        int hcut = optimal_cut(inputs[from+1].getImportance(), inputs[from+2].getImportance(), side_width);
        result[1] = new Block(inputs[from+1], first_row ? vcut : 0, top_offset, side_width, hcut);

        result[2] = new Block(inputs[from+2], first_row ? vcut : 0, top_offset + hcut, side_width, h-hcut);

        return result;
    }

    static PostDTO[] bestPermutation;
    static double bestPermutationScore = 99999999;

    static void permute(java.util.List<PostDTO> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }

        double pScore = (arr.get(0).getImportance() + arr.get(1).getImportance() + arr.get(2).getImportance()) - (arr.get(3).getImportance() + arr.get(4).getImportance() + arr.get(5).getImportance());
        pScore = Math.abs(pScore);

        if (pScore < bestPermutationScore) {
            bestPermutationScore = pScore;
            bestPermutation = arr.toArray(new PostDTO[1]);
        }
    }



    /**
     * LAYOUT:
     * 0   | 4
     *     |__
     *     | 5
     * ____|__
     * 2 |1
     * __|
     * 3 |
     * __|____
     */
    public static Block[] calc(int screenWidth, int screenHeight, List<PostDTO> inputList) {

        bestPermutation = new PostDTO[6];
        bestPermutationScore = 99999999;
        Block[] blocks = new Block[6];
        permute(new ArrayList<PostDTO>(inputList.subList(0,6)), 0);
        PostDTO[] inputs = bestPermutation;

        double top_importance = sumImportance(0,3,inputs);
        double bottom_importance = sumImportance(3,inputs.length,inputs);

        int row_cut = optimal_cut(top_importance, bottom_importance, screenHeight);

        Block top[] = compute_row(0, screenWidth, row_cut, 0, inputs);
        Block bottom[] = compute_row(3, screenWidth, screenHeight-row_cut, row_cut, inputs);

        for (int i=0;i<3;i++) blocks[i]=top[i];
        for (int i=3;i<6;i++) blocks[i]=bottom[i-3];

        return blocks;
    }

}

