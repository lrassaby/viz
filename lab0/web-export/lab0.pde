int screenWidth = 400, screenHeight = 300;

int currbutton = 0;
Button buttons[] = {new Button(screenWidth/4, screenHeight/4, screenWidth/2, screenHeight/2, 7, color(255, 153, 51), "Hello world."),
                    new Button(screenWidth/3, screenHeight/3, screenWidth/3, screenHeight/3, 7, color(100, 0, 204), "Mission"),
                    new Button(3 * screenWidth/8, 3 * screenHeight/8, screenWidth/4, screenHeight/4, 7, color(100, 140, 0), "accomplished.")};

float widthdelta = 0, heightdelta = 0;

void update () {
  int prevbutton = currbutton;
  currbutton = (currbutton + 1) % buttons.length;
}

void setup () {
    size(400, 300);
}

void draw() {
  background(255, 255, 255);
  Button button = buttons[currbutton];
  
  boolean click = button.getIsect();
  if (click) {
    update();
    button.setSelected(false);
  } 
  fill(button.getColor()); 
  if (widthdelta < 0) {
     rect(button.getPosX(), button.getPosY(), button.getWidth(), button.getHeight(), button.getRoundness()); 
  } else if (widthdelta > 0) {
    
  } else {
     rect(button.getPosX(), button.getPosY(), button.getWidth(), button.getHeight(), button.getRoundness()); 
  }
  
  
  fill(255);
  textSize(12); 
  textAlign(CENTER, CENTER); /* hacky solution --> should be based on rectangle rather than canvas */
  text(button.getText(), button.getCenterX(), button.getCenterY());
}

void mouseClicked() {
  Button button = buttons[currbutton];
  button.intersect(mouseX, mouseY);
}




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


