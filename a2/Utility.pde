void makeText(String str, int x, int y, float rotation) {      
    if (rotation != 0) {
        pushMatrix();
        translate(x, y);
        rotate(rotation);
        translate(-x, -y);
        text(str, x, y);
        popMatrix();
    } else {
        text(str, x, y);
    }
}

void drawCircle(int x, int y, float diameter) {
    ellipse(x, y, diameter, diameter);
}

// see http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
public class ColorGenerator {
    private float golden_ratio_conjugate = 0.618033988749895;
    private float h = 0.5;
    public color generate() {
        h += golden_ratio_conjugate;
        h %= 1;
        return hsvToRGB(h, 0.70, 0.80);
    }
    public color hsvToRGB (float h, float s, float v) {
        float h_i = int(h*6);
        float f = h*6 - h_i;
        float p = v * (1 - s);
        float q = v * (1 - f*s);
        float t = v * (1 - (1 - f) * s);
        if (h_i == 0) {
            return color(int(v * 256), int(t * 256), int(p * 256));
        } else if (h_i == 1) {
            return color(int(q * 256), int(v * 256), int(p * 256));
        } else if (h_i == 2) {
            return color(int(p * 256), int(v * 256), int(t * 256));
        } else if (h_i == 3) {
            return color(int(p * 256), int(q * 256), int(v * 256));
        } else if (h_i == 4) {
            return color(int(t * 256), int(p * 256), int(v * 256));
        } else if (h_i == 5) {
            return color(int(v * 256), int(p * 256), int(q * 256));
        }
        return color(0, 0, 0);
    }
};