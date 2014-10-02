PGraphics pickbuffer = null;
int numCircles = 10;
MyCircle[] circles;
color[] colors;

void setup() {
  smooth();
  size(600, 600);
  pickbuffer = createGraphics(width, height);
    colors = new color [numCircles];
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

void draw () {
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

void drawPickBuffer() {
  pickbuffer.beginDraw();
  for (int i = 0; i < numCircles; i++) {
    pickbuffer.fill(colors[i]);
    pickbuffer.ellipse(circles[i].posx, circles[i].posy,
               circles[i].radius * 2, circles[i].radius * 2);
  }
  pickbuffer.endDraw();
}

void mouseMoved () {
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












