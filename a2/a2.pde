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
  } catch (Exception e) { 
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
  String[] chart_texts =  {"Bar Chart", "Line Chart", "Pie Chart", "Stacked Bar", "ThemeRiver", "Rose Chart"};

  buttons = new ButtonGroup(chart_texts);
  chart = new TransitionChart(data, categories);
  chart.setChartType(chart_texts[0]);
  buttons.setSelection(chart_texts[0]);

  try {
      data = loadTable(filename, "header");
      lines = loadStrings(filename);
      categories = lines[0].split(",");   
      buttons = new ButtonGroup(chart_texts);
      chart = new TransitionChart(data, categories);
      chart.setChartType(chart_texts[0]);
      buttons.setSelection(chart_texts[0]);
  } catch (Exception e) {
      println("Bad file. Process cancelled.");
      exit();
  }
}


void draw() {
  background(255, 255, 255);
  chart.draw();
  buttons.draw();
}

void mouseClicked() {
  String clicked = buttons.getClicked();
  if (clicked != chart.getChartType() && chart.setChartType(clicked)) {
    buttons.setSelection(clicked);
  }
}



