int numCircles = 50;

MyCircle[] circles = null;

void setup () {
  size(800, 600);
  circles = new MyCircle[numCircles];
  for (int i=0; i<numCircles; i++) {
    circles[i] = new MyCircle();
  }
  //ellipseMode(RADIUS);
  textAlign(CENTER);
}

void draw() {
  background(255, 255, 255);
  for (int i=0; i<numCircles; i++) {
    MyCircle c = circles[i]; 
    if (c.getIsect() == false) {
      noFill();
      stroke(c.getColor());
      ellipse(c.getPosX(), c.getPosY(), c.getRadius()*2, c.getRadius()*2);
    }
    else {
      fill(c.getColor());
      ellipse(c.getPosX(), c.getPosY(), c.getRadius()*2, c.getRadius()*2);
      fill(0);
      text("("+int(c.getPosX())+","+int(c.getPosY())+")", c.getPosX(), c.getPosY());
    }
    c.update();
  }
}

void mouseMoved() {
  for (int i=0; i<numCircles; i++) {
    MyCircle c = circles[i]; 
    c.intersect(mouseX, mouseY);
  }
}

