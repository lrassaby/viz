import javax.swing.*;
Controller controller;
Network network;
Temporal temporal;

void setup() {
    frame.setResizable(true);
    size(800, 800);
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
    controller = new Controller(filename, network, temporal);
}

void draw() {
  background(255, 255, 255);
  controller.draw();
}
