Tree tree;

void draw () {
  background(255, 255, 255);
  tree.draw();
}

void setup () {
  frame.setResizable(true);
  size(1000, 800);
  
  tree = new Tree("hierarchy2.shf");
}

void mousePressed () {
  if (mouseButton == LEFT) {
    tree.setClicked(true);
  } else if (mouseButton == RIGHT) {
    tree.levelUp();
  } 
}





