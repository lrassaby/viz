class MyCircle {
  boolean isect;
  float radius;
  float posx, posy;
  float velx, vely;  
  color c;
  
  MyCircle() {
     isect = false;
     radius = random(10, 100);
     posx = random(radius, width-radius);
     posy = random(radius, height-radius);
     velx = random(-5, 5);
     vely = random(-5, 5);
     //c = color(int(random(0, 255)), int(random(0, 255)), int(random(0, 255)));
     c = color(random(0, 255), random(0, 255), random(0, 255));
  }
  
  void update() {
    if ((posx + velx + radius > width) || (posx + velx - radius < 0)){
      velx = -velx;
    }
    if ((posy + vely + radius  > height) || (posy + vely - radius < 0)){
      vely = -vely;
    }
    posx = posx + velx;
    posy = posy + vely;
  }
  
  void intersect (int mousex, int mousey) {
    float distance = sqrt((mousex - posx) * (mousex - posx) + 
                          (mousey - posy) * (mousey - posy));
    if (distance < radius) {
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
    posx = x;
    posy = y;
  }
  
  void setVel (float vx, float vy) {
    velx = vx;
    vely = vy;
  }
  
  void setColor (int r, int g, int b) {
    c = color (r, g, b);
  }

  boolean getIsect() {return isect;}
  float getPosX() {return posx;}
  float getPosY() {return posy;}
  float getVelX() {return velx;}
  float getVelY() {return vely;}
  float getRadius() {return radius;}
  color getColor() {return c;}
}
