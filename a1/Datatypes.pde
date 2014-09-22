public class Color {
    int r, g, b;
    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    void setRGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
};

// see http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
public class ColorGenerator {
    private float golden_ratio_conjugate = 0.618033988749895;
    private float h = 0.5;
    public Color generate() {
        h += golden_ratio_conjugate;
        h %= 1;
        return hsvToRGB(h, 0.70, 0.90);
    }
    public Color hsvToRGB (float h, float s, float v) {
        float h_i = int(h*6);
        float f = h*6 - h_i;
        float p = v * (1 - s);
        float q = v * (1 - f*s);
        float t = v * (1 - (1 - f) * s);
        if (h_i == 0) {
            return new Color(int(v * 256), int(t * 256), int(p * 256));
        } else if (h_i == 1) {
            return new Color(int(q * 256), int(v * 256), int(p * 256));
        } else if (h_i == 2) {
            return new Color(int(p * 256), int(v * 256), int(t * 256));
        } else if (h_i == 3) {
            return new Color(int(p * 256), int(q * 256), int(v * 256));
        } else if (h_i == 4) {
            return new Color(int(t * 256), int(p * 256), int(v * 256));
        } else if (h_i == 5) {
            return new Color(int(v * 256), int(p * 256), int(q * 256));
        }
        return new Color(0, 0, 0);
    }
};

public class Rect {
    float d_short, d_long;
    String name;

    Rect(float d_short, float d_long, String name) {
        this.d_short = d_short;
        this.d_long = d_long;
        this.name = name;
    }
    // value between 0 and 1, where closer to 1 is more square
    float getAspectRatio() {
        float aspect_ratio = d_short / d_long;
        if (aspect_ratio > 1) {
            aspect_ratio = 1 / aspect_ratio;
        }
        return aspect_ratio;
    }

    float size() {
        return d_short * d_long;
    }

};

public class Canvas {
  float x, y, w, h;
  Canvas(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  float size() {
    return w * h;
  }
  float getShortSide() {
    return w <= h ? w : h;
  }
  float getLongSide() {
    return w <= h ? h : w;
  }
};

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

