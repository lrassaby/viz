import javax.swing.*;
NodeSystem diagram;

void setup() {
    frame.setResizable(true);
    size(1000, 800);
    frameRate(60);

    String filename = null;
    try { 
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
        e.printStackTrace();
    } 

    try {
      filename = JOptionPane.showInputDialog(frame, "Input file", "data.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }
    if (filename == null) {
      println("Process cancelled.");
      exit();
    }
    diagram = new NodeSystem(filename);
}

void draw() {
    background(255, 255, 255);
    diagram.draw();
    diagram.update();
}

void mousePressed() {
    diagram.mousePressed();
}

void mouseReleased() {
    diagram.mouseReleased();
}