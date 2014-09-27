int numPoints = 20;
Point[] shape;
Point endP;

void setup() {
    size(400, 400);
    smooth();
    shape = new Point[numPoints];
    endP = new Point();

    makeRandomShape();

    frame.setResizable(true);
}

void draw() {
    background(255, 255, 255);
    stroke(0, 0, 0);

    drawShape();
    if (mousePressed == true) {
        stroke(255, 0, 0);
        line(mouseX, mouseY, endP.x, endP.y);

        fill(0, 0, 0);
        boolean isect = isectTest();
        if (isect == true) {
            text("Inside", mouseX, mouseY);
        } else {
            text("Outside", mouseX, mouseY);
        }
    }
}

void mousePressed() {
    endP.x = random(-1, 1) * 2 * width;
    endP.y = random(-1, 1) * 2 * height;
}

void drawShape() {
    for (int i = 0; i < numPoints; i++) {
        int start = i;
        int end = (i + 1) % numPoints;

        line(shape[start].x + width/2.0f, 
             shape[start].y + height/2.0f,
             shape[end].x + width/2.0f, 
             shape[end].y + height/2.0f);
    }
}

boolean isectTest() {
  
  Point currPoint = new Point();
  currPoint.x = mouseX;
  currPoint.y = mouseY;
  int intersections = 0;
  for (int i = 0; i < shape.length; i++) {

    if(i == shape.length - 1) {
      Point p1 = new Point();
      p1.x = shape[i].x + width/2.0f;
      p1.y = shape[i].y + height/2.0f;
      Point p2 = new Point();
      p2.x = shape[0].x + width/2.0f;
      p2.y = shape[0].y + height/2.0f;
      if(lineIsect(p1, p2, currPoint, endP)) {
        intersections += 1;
      }
    } else { 
      Point p1 = new Point();
      p1.x = shape[i].x + width/2.0f;
      p1.y = shape[i].y + height/2.0f;
      Point p2 = new Point();
      p2.x = shape[i + 1].x + width/2.0f;
      p2.y = shape[i + 1].y + height/2.0f;
      if(lineIsect(p1, p2, currPoint, endP)) {
        /*println("Shape point 1: " + shape[i].x, shape[i].y);
        println("Shape point 2: " + shape[i+1].x, shape[i+1].y);
        println("My point : " + currPoint.x, currPoint.y);*/
        intersections += 1;
      }
    }
  }
  if (intersections%2 == 0) {return false;}
  return true;

}

boolean lineIsect(Point p1, Point q1, Point p2, Point q2) {
  float a1 = p1.y - q1.y;
  float b1 = q1.x - p1.x;
  float c1 = q1.x * p1.y - p1.x * q1.y;
  float a2 = p2.y - q2.y;
  float b2 = q2.x - p2.x;
  float c2 = q2.x * p2.y - p2.x * q2.y;
  float det = a1 * b2 - a2 * b1;
  //if (det == 0) {
  if (isBetween(det, -0.0000001, 0.0000001)) {
    return false;
  } else {
  float isectx = (b2 * c1 - b1 * c2) / det;
  float isecty = (a1 * c2 - a2 * c1) / det;
  //println ("isectx: " + isectx + " isecty: " + isecty);
    if ((isBetween(isecty, p1.y, q1.y) == true) &&
      (isBetween(isecty, p2.y, q2.y) == true) &&
      (isBetween(isectx, p1.x, q1.x) == true) &&
      (isBetween(isectx, p2.x, q2.x) == true)) {
      return true;
    }
  }
  return false;
}

boolean isBetween(float val, float range1, float range2) {
    float largeNum = range1;
    float smallNum = range2;
    if (smallNum > largeNum) {
        largeNum = range2;
        smallNum = range1;
    }

    if ((val < largeNum) && (val > smallNum)) {
        return true;
    }
    return false;
}

void makeRandomShape() {
    float slice = 360.0 / (float) numPoints;
    for (int i = 0; i < numPoints; i++) {
        float radius = (float) random(5, 100);
        shape[i] = new Point();
        shape[i].x = radius * cos(radians(slice * i));
        shape[i].y = radius * sin(radians(slice * i));
    }
}





  /*float a1 = 0;
  float b1 = 0;
  float c1 = 0;
  float a2 = 0;
  float b2 = 0;
  float c2 = 0;
  int numHits = 0;
  
  for (int i = 0; i < shape.length; i++) {
      a1 = shape[i+1].y - shape[i].y;
      b1 = shape[i].x - shape[i+1].x;
      c1 = a1*shape[i].x + b1 * shape[i].y;
      a2 = endP.y - mouseY;
      b2 = mouseX - endP.x;
      c2 = a2*mouseX + b2*mouseY;
      
    float det = a1*b2 - a2*b1;
    if(det != 0){
        float x = (b2*c1 - b1*c2)/det;
        float y = (a1*c2 - a2*c1)/det;
        if x 
    }
      
  }*/
