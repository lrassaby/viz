public class Button {
  boolean isect;
  Point center, pos; 
  Dimensions dim;
  float roundness;
  color c;
  String text;
 

  void draw() {
      boolean selected = button.getIsect();
      if (selected) { 
         // DO SOMETHING
         button.setSelected(false);
      } 
      fill(button.getColor()); 

      fill(255);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(button.getText(), button.center.x, button.center.y);
  }

  
  Button(Point pos, Dimensions dim, float roundness, color c, String text) { /* height and width of button */
     this.isect = false;
     this.pos = pos;
     this.dim = dim;
     this.roundness = roundness;
     this.c = c;
     this.center = center;
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
  
  void setSize (float w, float h) {
    this.dim.w = w;
    this.dim.h = h;
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
  Point getCenter() {return center;}
  Point getPos() {return pos;}
  Dimensions getDim() {return dim;}
  color getColor() {return c;}
  float getRoundness() {return roundness;}
  String getText() {return text;}
};

