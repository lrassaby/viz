/* color generator based on golden ratio */
public class ColorGenerator {
    private float golden_ratio_conjugate = 0.618033988749895;
    private float h = 0.5;
    public color generate() {
        h += golden_ratio_conjugate;
        h %= 1;
        return hsvToRGB(h, 0.60, 0.90);
    }
    public color hsvToRGB (float h, float s, float v) {
        float h_i = int(h*6);
        float f = h*6 - h_i;
        float p = v * (1 - s);
        float q = v * (1 - f*s);
        float t = v * (1 - (1 - f) * s);
        color c = color(0, 0, 0);
        if (h_i == 0) {
            c = color(int(v * 256), int(t * 256), int(p * 256));
        } else if (h_i == 1) {
            c = color(int(q * 256), int(v * 256), int(p * 256));
        } else if (h_i == 2) {
            c = color(int(p * 256), int(v * 256), int(t * 256));
        } else if (h_i == 3) {
            c = color(int(p * 256), int(q * 256), int(v * 256));
        } else if (h_i == 4) {
            c = color(int(t * 256), int(p * 256), int(v * 256));
        } else if (h_i == 5) {
            c = color(int(v * 256), int(p * 256), int(q * 256));
        }
        return c;
    }
};


// http://sebleedelisle.com/2009/05/super-fast-trianglerectangle-intersection-test/
boolean lineRectangleIntersect(float x1, float y1, float x2, float y2,
                               float rx, float ry, float rw, float rh) {
                                  
  float topIntersection;
  float bottomIntersection;
  float topPoint;
  float bottomPoint;
 
  // Calculate m and c for the equation for the line (y = mx+c)
  float m = (y2-y1) / (x2-x1);
  float c = y1 -(m*x1);
 
  // If the line is going up from right to left then the top intersect point is on the left
  if(m > 0) {
    topIntersection = (m*rx  + c);
    bottomIntersection = (m*(rx+rw)  + c);
  }
  // Otherwise it's on the right
  else {
    topIntersection = (m*(rx+rw)  + c);
    bottomIntersection = (m*rx  + c);
  }
 
  // Work out the top and bottom extents for the triangle
  if(y1 < y2) {
    topPoint = y1;
    bottomPoint = y2;
  } else {
    topPoint = y2;
    bottomPoint = y1;
  }
 
  float topOverlap;
  float botOverlap;
 
  // Calculate the overlap between those two bounds
  topOverlap = topIntersection > topPoint ? topIntersection : topPoint;
  botOverlap = bottomIntersection < bottomPoint ? bottomIntersection : bottomPoint;
 
  return (topOverlap<botOverlap) && (!((botOverlap<ry) || (topOverlap>ry+rh)));
 
}


/* Point class */
public class Point {
    float x, y;
    String disp;
    Point() {
    	this.x = 0;
    	this.y = 0;
    }
    Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }
    void setDisp(String disp) {
        this.disp = disp;
    }
};

/* holds a point and its classification */
public class Coordinate {
	Point p;
	int classification;

	Coordinate(Point p, int classification) {
		this.p = p;
		this.classification = classification;
	}

    public void draw(color c) {
        fill(c);
        stroke(c);
        ellipse(p.x, p.y, 4, 4);
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
