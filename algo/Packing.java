import java.util.*;

class Inp implements Comparable<Inp> {
    double importance;
    double ratio;
    String name;

    public Inp() {}

    public Inp(String name, double imp, double ratio) {
        this.importance = imp;
        this.ratio = ratio;
        this.name = name;
    }

    public int compareTo(Inp o) {
        return -(new Double(this.importance).compareTo(o.importance));
    }
}

class Block {
    int x;
    int y;
    int w;
    int h;
    String name;

    public Block(int sw, int sh, Inp c) {
        // compute 'perfect' size from percentages
        double ratio = c.ratio;
        double perc = c.importance;
        int screenSize = sw * sh;
        double blockSize = perc * screenSize;

        double height = Math.sqrt(blockSize / perc);
        double width = perc * height;

        this.w = (int)Math.round(width);
        this.h = (int)Math.round(height);
    }

    public Block(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public String toString() {
        return String.format("{width: %dpx; height: %dpx; left: %dpx; top:%dpx;} /*%s*/", w, h, x, y, name);
    }
}

public class Packing {
    public static void main(String... args) {
    
        Inp inputs[] = new Inp[6];
        inputs[0] = new Inp("first 30", 0.3, 1);
        inputs[1] = new Inp("second 20", 0.2, 1);
        inputs[2] = new Inp("second 12", 0.12, 1);
        inputs[3] = new Inp("second 13", 0.13, 1);
        inputs[4] = new Inp("second 09", 0.09, 1);
        inputs[5] = new Inp("second 16", 0.16, 1);
   
        Block result[] = calc(480, 800, inputs);
        int i=1;
        for (Block b : result) { System.out.print("#d"+(i++)+" "); System.out.println(b);  }
 
    }

    public static int optimal_cut(double i1, double i2, int size) {
        double total = i1+i2;
        double cut = i1/total;
        return (int)Math.round(size*cut);
    }

    public static double sumImportance(int from, int to, Inp inputs[]) {
        double imp = 0;
        for (int i=from;i<to;i++) {
            imp+=inputs[i].importance;
        }
        return imp;
    }

    public static Block[] compute_row(int from, int w, int h, int top_offset, Inp inputs[]) {
        Block[] result = new Block[3];
        
        boolean first_row = from == 0;
        
        int vcut = optimal_cut(inputs[from].importance, inputs[from+1].importance + inputs[from+2].importance, w);
        int side_width = w-vcut;

        result[0] = new Block(inputs[from].name, first_row ? 0 : side_width, top_offset, vcut, h);

        int hcut = optimal_cut(inputs[from+1].importance, inputs[from+2].importance, side_width);
        result[1] = new Block(inputs[from+1].name, first_row ? vcut : 0, top_offset, side_width, hcut);

        result[2] = new Block(inputs[from+2].name, first_row ? vcut : 0, top_offset + hcut, side_width, h-hcut);

        return result;
    }

    static Inp[] bestPermutation;
    static double bestPermutationScore = 99999999;

    static void permute(java.util.List<Inp> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        double pScore = (arr.get(0).importance + arr.get(1).importance + arr.get(2).importance) - (arr.get(3).importance + arr.get(4).importance + arr.get(5).importance);
        pScore = Math.abs(pScore);
        
//        System.out.println("score="+pScore);
        if (pScore < bestPermutationScore) {
            bestPermutationScore = pScore;
            bestPermutation = arr.toArray(new Inp[1]);
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
    public static Block[] calc(int screenWidth, int screenHeight, Inp inputs[]) {

        Block[] blocks = new Block[6];

        permute(Arrays.asList(inputs), 0);

        inputs = bestPermutation;
        // print inputs
//        for (Inp x : inputs) System.out.println(String.format("input '%s': %f", x.name, x.importance));

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

