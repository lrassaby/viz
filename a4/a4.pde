import javax.swing.*;
Controller controller;
Network network;
Temporal temporal;
Categorical categorical;
Selection selection;


void setup() {
    frame.setResizable(true);
    size(900, 800);
    frameRate(60);
    String filename = null;
    try { 
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
      e.printStackTrace();
    } 

    try {
      filename = JOptionPane.showInputDialog(frame, "Input file", "data_aggregate.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }
    if (filename == null) {
      println("Process cancelled.");
      exit();
    }
    network = new Network();
    temporal = new Temporal();
    categorical = new Categorical();
    controller = new Controller(filename, network, temporal, categorical);
    selection = new Selection(network, temporal, categorical);
}

void draw() {
  background(255, 255, 255);
  if (mousePressed) {
    selection.update();
  }
  selection.modifyViews();
  controller.draw();
  selection.draw();
}

void mouseClicked() {
  if (selection.active()) {
    selection.disable();
  }
}

void mouseReleased() {
  selection.setFixed();
}