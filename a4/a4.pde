import javax.swing.*;
Controller controller;
Network network;
Temporal temporal;

void setup() {
    frame.setResizable(true);
    size(1000, 800);
    frameRate(60);
    float [] net_canvas = {0, 0, 800, 600};
    float [] temp_canvas = {0, 700, 800, 200};
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
    network = new Network(net_canvas);
    temporal = new Temporal(temp_canvas);
    controller = new Controller(filename, network, temporal);

}

void draw() {
  controller.draw();
}
