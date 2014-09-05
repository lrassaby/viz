class Point {
  float x, y;
};

class Button {
  boolean isect;
  float x, y; /* center */
  float w, h; /* width & height */
  float roundness;
  color c;
  String text;
  Point center = new Point();


  
  Button(float x, float y, float w, float h, float roundness, color c, String text) { /* height and width of button */
     this.isect = false;
     this.x = x;
     this.y = y;
     this.w = w;
     this.h = h;
     this.roundness = roundness;
     this.c = c;
     this.center.x = x + w/2;
     this.center.y = y + h/2;
     this.text = text;
  }

  
  void intersect (int mousex, int mousey) {
    /* if it's within x + width and y + height */
    if (mousex <= (x + w) && mousex >= x && mousey <= (y + h) && mousey >= y) {
      isect = true;
    }
    else {
      isect = false;
    }
  }
  
  void setSelected (boolean s) {
    isect = s;
  }

  void setPos(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  void setSize (float w, float h) {
    this.w = w;
    this.h = h;
  }
  
  void setColor (int r, int g, int b) {
    c = color (r, g, b);
  }
  
  void setColor (color c) {
    this.c = c;
  }
  
  void setText (String text) {
     this.text = text; 
  }

  boolean getIsect() {return isect;}
  float getPosX() {return x;}
  float getPosY() {return y;}
  float getCenterX() {return center.x;}
  float getCenterY() {return center.y;}
  float getWidth() {return w;}
  float getHeight() {return h;}
  color getColor() {return c;}
  float getRoundness() {return roundness;}
  String getText() {return text;}
}

