package com.fbhack.processing;

import java.util.*;
import com.fbhack.*;


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

        int vcut = optimal_cut(inputs[from].getImportance()*1.5, (inputs[from+1].getImportance() + inputs[from+2].getImportance())/1.5, w);
        int side_width = w-vcut;

        result[0] = new Block(inputs[from], first_row ? 0 : side_width, top_offset, vcut, h);

        double cutpercent = inputs[from+1].getImportance() / (inputs[from+1].getImportance() + inputs[from+2].getImportance());
        cutpercent = (0.2 * cutpercent) + 0.4;

        int hcut = (int)Math.round(cutpercent * h);
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

        ArrayList<PostDTO> list = new ArrayList<PostDTO>(inputList.subList(0,6));

        // normalize importance
        double p_sum = 0;
        for (PostDTO p : list) { p_sum += p.getImportance(); }
        for (PostDTO p : list) { p.setImportance(p.getImportance() / p_sum); }

        permute(list, 0);
        PostDTO[] inputs = bestPermutation;

        Comparator<PostDTO> comparator = new Comparator<PostDTO>() {
            @Override
            public int compare(PostDTO p1, PostDTO p2) {
                return new Double(p2.getImportance()).compareTo(p1.getImportance());
            }
        };

        Arrays.sort(inputs, 0, 3, comparator);
        Arrays.sort(inputs, 3, 6, comparator);

        double top_importance = sumImportance(0,3,inputs);
        double bottom_importance = sumImportance(3,inputs.length,inputs);

        int row_cut = optimal_cut(top_importance, bottom_importance, screenHeight);

        Block top[] = compute_row(0, screenWidth, row_cut, 0, inputs);
        Block bottom[] = compute_row(3, screenWidth, screenHeight-row_cut, row_cut, inputs);

        System.arraycopy(top, 0, blocks, 0, 3);
        System.arraycopy(bottom, 0, blocks, 3, 3);

        return blocks;
    }

}

