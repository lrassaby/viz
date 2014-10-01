import javax.swing.*;

TransitionChart chart;
ButtonGroup buttons;


void setup () {
  frame.setResizable(true);
  size(900, 700);
  frameRate(60);

  String filename = null;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 
  try {
      filename = JOptionPane.showInputDialog(frame, "Input file (type csv)", "data.csv");
  } catch (Exception e) {
      println("Process cancelled.");
      exit();
  }
  if (filename == null) {
      println("Process cancelled.");
      exit();
  }
  
  Table data = loadTable(filename, "header");
  String[] lines = loadStrings(filename);
  String[] categories = lines[0].split(",");

  String[] chart_texts = {"Bar Chart", "Line Chart", "Pie Chart"};
  buttons = new ButtonGroup(chart_texts);
  chart = new TransitionChart(data, categories);
  chart.setChartType(chart_texts[0]);
  buttons.setSelection(chart_texts[0]);
}


void draw() {
  background(240, 240, 240);

  chart.draw();
  buttons.draw();
}

void mouseClicked() {
  String clicked = buttons.getClicked();
  if (chart.setChartType(clicked)) {
    buttons.setSelection(clicked);
  }
}


