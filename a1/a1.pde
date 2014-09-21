import javax.swing.*;

SquarifiedChart tree;

void draw () {
  background(255, 255, 255);
  tree.draw();
}

void setup () {
  frame.setResizable(true);
  size(1000, 800);

  String file = null;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 

  Object[] possibleValues = { "Hierarchical Treemap", "Nonhierarchical Treemap", "Barchart Treemap" };
  Object selectedValue = JOptionPane.showInputDialog(null,
  "What sort of treemap?", "Input",
  JOptionPane.INFORMATION_MESSAGE, null,
  possibleValues, possibleValues[0]);
  if (selectedValue == null) {
    println("Process cancelled.");
    exit();
  } else if (selectedValue == "Hierarchical Treemap") {
    try {
      File openfile;
      file = JOptionPane.showInputDialog(frame, "Input file (type shf)", "hierarchy2.shf");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }

    if (file == null) {
      println("Process cancelled.");
      exit();
    } else {
      try {
        tree = new Tree(file);
      } catch (Exception e) {
        e.printStackTrace();    
        exit();
      }
    }
  } else if (selectedValue == "Nonhierarchical Treemap") {
    try {
      File openfile;
      file = JOptionPane.showInputDialog(frame, "Input file (type csv)", "soe-funding.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }

    if (file == null) {
      println("Process cancelled.");
      exit();
    } else {
      try {
        tree = new CSVTree(file);
      } catch (Exception e) {
        e.printStackTrace();    
        exit();
      }
    }
  } else if (selectedValue == "Barchart Treemap") {
    try {
      File openfile;
      file = JOptionPane.showInputDialog(frame, "Input file (type csv)", "soe-funding.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }

    if (file == null) {
      println("Process cancelled.");
      exit();
    } else {
      try {
        tree = new BarTree(file);
      } catch (Exception e) {
        e.printStackTrace();    
        exit();
      }
    }
  } else {
    JOptionPane.showMessageDialog(null, "Sorry, this feature hasn't been implemented.");
    exit();
  }
}

void mousePressed () {

  if (mouseButton == LEFT) {
    tree.setClicked(true);
  } else if (mouseButton == RIGHT) {
    tree.levelUp();
  } 
}





