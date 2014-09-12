import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lab1 extends PApplet {

int[] margins = {120, 30, 30, 120}; // left, top, right, bottom
Button button = new Button(new Point(width - 50, height + 20), new Dimensions(70, 30), 7, color(255, 153, 51), "Bar Chart");
Linechart linechart = new Linechart();
Table data;

public void setup () {
  frame.setResizable(true);
  size(700, 700);
 
  data = loadTable("data.csv");
}



  // draw 2 lines
  // w/ labels
  // rotate axes with matrix transforms: pushMatrix, rotate(HALF_PI)
  // 
  // For animataion use frameRate() to throttle frames
  // lerp (linear interpolation)
  // use wide line rather than rectangle

public void draw() {
  background(200, 200, 200);

  linechart.setAxes(new Point(margins[0], height - margins[3]), 
                    new Point(margins[0], margins[1]), 
                    new Point(width - margins[2], height - margins[3]));
  linechart.drawAxes();
  linechart.draw();
  // Hover: on mouse move do stuff
  // background(255, 255, 255);
  // Button button = buttons[currbutton];
  
}

public void mouseClicked() {
  button.intersect(mouseX, mouseY);
}




public class Barchart extends Chart {

    
};
public class Button {
  boolean isect;
  Point center, pos; 
  Dimensions dim;
  float roundness;
  int c;
  String text;
 

  public void draw() {
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

  
  Button(Point pos, Dimensions dim, float roundness, int c, String text) { /* height and width of button */
     this.isect = false;
     this.pos = pos;
     this.dim = dim;
     this.roundness = roundness;
     this.c = c;
     this.center = center;
     this.text = text;
  }

  
  public void intersect (int mousex, int mousey) {
    /* if it's within x + width and y + height */
    if (mousex <= (pos.x + dim.w) && mousex >= pos.x && mousey <= (pos.y + dim.h) && mousey >= pos.y) {
      isect = true;
    }
    else {
      isect = false;
    }
  }
  
  public void setSelected (boolean s) {
    isect = s;
  }

  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public void setSize (float w, float h) {
    this.dim.w = w;
    this.dim.h = h;
  }
  
  public void setColor (int r, int g, int b) {
    c = color (r, g, b);
  }

  public void setColorObject (int c) {
    this.c = c;
  }
  
  
  public void setColor (int c) {
    this.c = c;
  }
  
  public void setText (String text) {
     this.text = text; 
  }

  public boolean getIsect() {return isect;}
  public Point getCenter() {return center;}
  public Point getPos() {return pos;}
  public Dimensions getDim() {return dim;}
  public int getColor() {return c;}
  public float getRoundness() {return roundness;}
  public String getText() {return text;}
};

public class Chart {
    Point origin, topyaxis, rightxaxis;
    String x_label, y_label;
    public void setAxes(Point origin, Point topyaxis, Point rightxaxis) {
        this.origin = origin;
        this.topyaxis = topyaxis;
        this.rightxaxis = rightxaxis;
    }
    public void drawAxes() {
        strokeWeight(2);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }
};
public class Point {
    float x, y;
    Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
};

public class Dimensions {
    float w, h;
    Dimensions(float w, float h) {
        this.w = w;
        this.h = h;
    }
};
public class Linechart extends Chart {

    public void draw () {
        
    }

    // public linechart (x_data, y_data, x_lbl, y_lbl) {
    //     this.x_data = x_data;
    //     this.y_data = y_data;
    // }
    
    // Draw function
    // Circles w/ ellipse function

};
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lab1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
