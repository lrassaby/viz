int screenWidth = 400, screenHeight = 300;
int currcolor = 0, currtext = 0, currwidth = 0, currheight = 0;
color colors[] = {color(255, 153, 51), color(100, 0, 204)};
String texts[] = {"Mission", "accomplished!"};
int widths[] = {screenWidth / 2, screenWidth / 3};
int heights[] = {screenHeight / 2, screenHeight / 3};

Button button;

void update () {
  button.setColor(colors[(++currcolor) % colors.length]);
  button.setText(texts[(++currtext) % texts.length]);
  button.setSize(widths[(++currwidth) % widths.length], heights[(++currheight) % heights.length]);
}

void setup () {
  size(screenWidth, screenHeight);
  button = new Button(screenWidth/4, screenHeight/4, screenWidth/2, screenHeight/2, 7, colors[currcolor]);
  button.setText(texts[currtext]);
}

void draw() {
  background(255, 255, 255);
  boolean click = button.getIsect();
  if (click) {
    update();
    button.setSelected(false);
  } 
  fill(button.getColor()); 
  rect(button.getPosX(), button.getPosY(), button.getWidth(), button.getHeight(), button.getRoundness());
  
  fill(255);
  textAlign(CENTER, CENTER); /* hacky solution --> should be based on rectangle rather than canvas */
  text(button.getText(), button.getCenterX(), button.getCenterY());
 
 
}

void mouseClicked() {
  System.out.println("click");
  button.intersect(mouseX, mouseY);
}




