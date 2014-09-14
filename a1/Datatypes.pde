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