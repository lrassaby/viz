// TODO: additional requirements
import javax.swing.*;

int[] margins = {80, 65, 30, 100}; // left, top, right, bottom
Point origin, 
      topyaxis, 
      rightxaxis;
String hovertext;
Dimensions buttondim;
TransitionChart chart;

// To create a new chart just change the strings in here 
String[] chart_texts = {"Bar Chart", "Line Chart", "Pie Chart"};

Button[] buttons = new Button[chart_texts.length]; 
Point[] buttonpos = new Point[chart_texts.length];
boolean hover;

void setup () {
  frame.setResizable(true);
  size(700, 700);

  origin = new Point(margins[0], height - margins[3]);
  topyaxis = new Point(margins[0], margins[1]); 
  rightxaxis = new Point(width - margins[2], height - margins[3]);
  buttondim = new Dimensions(100, 30);


  for (int i = 0; i < buttons.length; i++) {
    buttonpos[i] = new Point(width - buttondim.w - margins[2] - (buttons.length - i - 1) * (buttondim.w + 10), margins[1] - 50);
    buttons[i] = new Button(buttonpos[i], buttondim, 7, color(50, 200, 160), chart_texts[i]);
  }

  String file = null;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 
  try {
      file = JOptionPane.showInputDialog(frame, "Input file (type csv)", "data.csv");
  } catch (Exception e) {
      println("Process cancelled.");
      exit();
  }
  if (file == null) {
      println("Process cancelled.");
      exit();
  }
  Table data = loadTable(file, "header");

  chart = new TransitionChart(data, origin, topyaxis, rightxaxis);
}


void draw() {

  background(240, 240, 240);

  origin.setXY(margins[0], height - margins[3]);
  topyaxis.setXY(margins[0], margins[1]);
  rightxaxis.setXY(width - margins[2], height - margins[3]);

  chart.setAxes(origin, topyaxis, rightxaxis);
  chart.draw();

  String chart_type = chart.getChartType();

  for (int i = 0; i < buttons.length; i++) {
    buttonpos[i].setXY(width - buttondim.w - margins[2] - (buttons.length - i - 1) * (buttondim.w + 10), margins[1] - 50);
    if (chart_texts[i] == chart_type) {
      buttons[i].setColor(40, 190, 100);
    } else {
      buttons[i].setColor(50, 200, 160);
    }
    buttons[i].draw();
  }

  if (hover) {
    fill(255, 0, 0);
    textSize(20);
    // chart.highlightOnHover();
    fill(255, 0, 0);
    if (mouseX < (width/2)) {
      textAlign(LEFT, CENTER);
      text(hovertext, mouseX, mouseY - 20);
    } else {
      textAlign(RIGHT, CENTER);
      text(hovertext, mouseX, mouseY - 20);
    }
  }
}

void mouseClicked() {
  for (int i = 0; i < buttons.length; i++) {
    buttons[i].intersect(mouseX, mouseY);
    boolean selected = buttons[i].getIsect();
    if (selected) { 
      buttons[i].setSelected(false);
      if (!chart.inTransition()) {
        chart.setChartType(chart_texts[i]);
      }
    }
  }
}



