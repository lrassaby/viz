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

public class pickbuffer_test extends PApplet {

PGraphics pickbuffer = null;
int numCircles = 10;
MyCircle[] circles;
int[] colors;

public void setup() {
  smooth();
  size(600, 600);
  pickbuffer = createGraphics(width, height);
    colors = new int [numCircles];
    for (int i = 0; i < numCircles; i++) {
    colors[i] = color(random(255), random(255), random(255));
  }
  
  circles = new MyCircle [numCircles];
  
  for (int i=0; i<numCircles; i++) {
    int radius = (int)random(10, 100);
    int posx = (int)random(radius, width-radius);
    int posy = (int)random(radius, height-radius);

    circles[i] = new MyCircle(i*10000, posx, posy, radius);
    
    int r = (int)random(0, 255);
    int g = (int)random(0, 255);
    int b = (int)random(0, 255);
    circles[i].setColor (r, g, b);    
  }
}

public void draw () {
  background(255);

  for (int i=0; i<numCircles; i++) { 
    if (circles[i].getSelected() == true) {
      circles[i].renderSelected();
    }
    else {
      circles[i].render();
    }
  }
  
  if (keyPressed) {
    drawPickBuffer();
    image(pickbuffer, 0, 0);
  }
}

public void drawPickBuffer() {
  pickbuffer.beginDraw();
  for (int i = 0; i < numCircles; i++) {
    pickbuffer.fill(colors[i]);
    pickbuffer.ellipse(circles[i].posx, circles[i].posy,
               circles[i].radius * 2, circles[i].radius * 2);
  }
  pickbuffer.endDraw();
}

public void mouseMoved () {
  drawPickBuffer();
  
  for (int i=0; i<numCircles; i++) {
    if (circles[i].isect(colors, pickbuffer, i) == true) {
      circles[i].setSelected(true);
    }
    else {
      circles[i].setSelected(false);
    }
  }
}












class MyCircle {
  int id;
  int posx, posy, radius;
  int r, g, b;
  boolean selected = false;
  
  MyCircle(int _id, int _posx, int _posy, int _radius) {
    id = _id;
    posx = _posx;
    posy = _posy;
    radius = _radius;
  }
  
  public void setColor (int _r, int _g, int _b) {
    r = _r; g = _g; b = _b;
  }
  
  public boolean getSelected () {
    return selected;
  }
  
  public void setSelected (boolean _selected) {
    selected = _selected;
  }
  
  public void render() {
    strokeWeight(5);
    stroke(r, g, b);
    noFill();
    ellipse(posx, posy, radius*2, radius*2);  
  }
  
  public void renderIsect(PGraphics pg) {
    pg.fill(red(id), green(id), blue(id));
    pg.stroke(red(id), green(id), blue(id));
    pg.strokeWeight(5);
    pg.ellipse(posx, posy, radius*2, radius*2);  
  }
  
  public void renderSelected() {
    strokeWeight(1);
    stroke(r, g, b);
    fill (r, g, b, 128);
    ellipse(posx, posy, radius*2, radius*2);      
  }
  
  public boolean isect (int[] colors, PGraphics pickbuffer, int i) {
    int pixel = pickbuffer.get(mouseX, mouseY);
      if (colors[i] == pixel)
        return true;
      else
        return false;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "pickbuffer_test" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
