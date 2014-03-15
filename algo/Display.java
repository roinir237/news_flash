import java.util.*;

class Inp implements Comparable {
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
        return new Double(this.importance).compareTo(o.importance);
    }
}

class Block {
    int x;
    int y;
    int width;
    int height;
    String name;

    public Block() {
    }

    public Block(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }
}

public class Display {
   
    public static double getRatio(int w, int h) {
        return (double) w / h;
    }

    public static double cutHorizontally(int w, int h, double p) {
        int cutAt = Math.round(p * h);
        return getRatio(w, cutAt);   
    }

    public static double cutVertically(int w, int h, double p) {
        int cutAt = Math.round(p*w);
        return getRatio(cutAt, h);
    }

    public static boolean firstClosest(double to, double first, double second) {
        return Math.abs(to-first) <= Math.abs(to-second);
    }

    public static Block[] calc(int screenWidth, int screenHeight, Inp inputs[]) {

        Arrays.sort(inputs);

        Block blocks[] = new Block[inputs.length];

        int end = inputs.length-1;
        for (int i=0;i<end;i++) {
            Inp first = inputs[0];

            double hRatio = cutHorizontally(screenWidth, screenHeight, first.importance);
            double vRatio  = cutVertically(screenWidth, screenHeight, first.importance); 
        
            if (firstClosest(first.ratio, hRatio, vRatio)) {
                // cut horizontally

            } else {
                // cut vertically
            }
        }


    }

}
