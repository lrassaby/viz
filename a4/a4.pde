import javax.swing.*;
Controller controller;
//Network network;

void setup() {
    frame.setResizable(true);
    size(1000, 800);
    frameRate(60);
    float [] net_canvas = {0, 0, 800, 700};
    String filename = null;
    try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
    e.printStackTrace();
    } 

    try {
      filename = JOptionPane.showInputDialog(frame, "Input file", "test.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }
    if (filename == null) {
      println("Process cancelled.");
      exit();
    }
    controller = new Controller(filename);
    //network = new Network(model, net_canvas);
}
