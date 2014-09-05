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
  size(screenWidth, screenHeight);
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




