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
    int w;
    int h;
    String name;

    public Block(Root r, Inp c) {
        // compute 'perfect' size from percentages
        double ratio = c.ratio;
        double perc = c.importance;
        int screenSize = r.w * r.h;
        double blockSize = perc * screenSize;

        double height = Math.sqrt(blockSize / perc);
        double width = perc * height;

        this.w = Math.round(width);
        this.h = Math.round(height);
    }

    public Block(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}

class Screen {
    int w;
    int h;
    int x;
    int y;

    Root root;
    ArrayList<Screen> children;

    public Screen(Root r, int w, int h, int x, int y) {
        this.root = r;
        this.w=w;
        this.h=h;
        this.x=x;
        this.y=y;
        children = new ArrayList<Screen>();
    }

    double getRatio() {
        return (double)w/h;
    }
   
    int getSize() { return w*h; }

    public boolean insert(Block b) {
    }
}

class Root extends Screen {
    ArrayList<Block> blocks = new ArrayList<Block>();
    public Root(int w, int h) {
        super(this, w, h, 0, 0);
    }
}

public class Packing {

    public static Block[] calc(int screenWidth, int screenHeight, Inp inputs[]) {
        Root root = new Root();
    }


}

