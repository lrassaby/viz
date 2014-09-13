public class Button {
  boolean isect;
  Point pos; 
  Dimensions dim;
  float roundness;
  color c;
  String text;
 

  void draw() {
      strokeWeight(2);
      fill(button.getColor()); 
      rect(button.pos.x, button.pos.y, button.dim.w, button.dim.h, button.roundness); 

      fill(0);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(button.text, button.pos.x + dim.w / 2, button.pos.y + dim.h / 2);
  }

  
  Button(Point pos, Dimensions dim, float roundness, color c, String text) { /* height and width of button */
     this.isect = false;
     this.pos = pos;
     this.dim = dim;
     this.roundness = roundness;
     this.c = c;
     this.text = text;
  }

  
  void intersect (int mousex, int mousey) {
    /* if it's within x + width and y + height */
    if (mousex <= (pos.x + dim.w) && mousex >= pos.x && mousey <= (pos.y + dim.h) && mousey >= pos.y) {
      isect = true;
    }
    else {
      isect = false;
    }
  }
  
  void setSelected (boolean s) {
    isect = s;
  }

  void setPos(Point pos) {
    this.pos = pos;
  }
  
  void setSize (Dimensions dim) {
    this.dim = dim;
  }
  
  void setColor (int r, int g, int b) {
    c = color (r, g, b);
  }

  void setColorObject (color c) {
    this.c = c;
  }
  
  
  void setColor (color c) {
    this.c = c;
  }
  
  void setText (String text) {
     this.text = text; 
  }

  boolean getIsect() {return isect;}
  Point getPos() {return pos;}
  Dimensions getDim() {return dim;}
  color getColor() {return c;}
  float getRoundness() {return roundness;}
  String getText() {return text;}
};

