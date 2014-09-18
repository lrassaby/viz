public class Point {
    int x, y;
    String disp;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void setDisp(String disp) {
        this.disp = disp;
    }
};

public class Dimensions {
    int w, h;
    Dimensions(int w, int h) {
        this.w = w;
        this.h = h;
    }
    void setWH(int w, int h) {
        this.w = w;
        this.h = h;
    }
};

public class Line {
    Point start, end;
    String disp;
    Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    void draw() {
        
    }
    void setPoints(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
};

public class Rect {
    float d_short, d_long;
    float x, y; 
    Rect(float d_short, float d_long) {
        this.d_short = d_short;
        this.d_long = d_long;
    }
    float getAspectRatio() {
        float aspect_ratio = d_short / d_long;
        if (aspect_ratio > 1)
            aspect_ratio = 1 / aspect_ratio;
        return aspect_ratio;
    }
};