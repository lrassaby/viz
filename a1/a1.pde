import javax.swing.*;
Tree tree;

void draw () {
  background(255, 255, 255);
  tree.draw();
}

void setup () {
  frame.setResizable(true);
  size(1000, 800);

  String file;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 

  File openfile;
  file = JOptionPane.showInputDialog(frame, "Input file", "hierarchy2.shf");
  
  tree = new Tree(file);
}

void mousePressed () {
  if (mouseButton == LEFT) {
    tree.setClicked(true);
  } else if (mouseButton == RIGHT) {
    tree.levelUp();
  } 
}





