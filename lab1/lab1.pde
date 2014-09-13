int[] margins = {80, 30, 30, 100}; // left, top, right, bottom
Point origin, 
      topyaxis, 
      rightxaxis,
      buttonpos;
Dimensions buttondim;
Chart chart;
Button button;

void setup () {
  frame.setResizable(true);
  size(700, 700);
  chart = new Chart();

  origin = new Point(margins[0], height - margins[3]);
  topyaxis = new Point(margins[0], margins[1]); 
  rightxaxis = new Point(width - margins[2], height - margins[3]);
  buttondim = new Dimensions(70, 30);
  buttonpos = new Point(width - buttondim.w - margins[2], margins[1]);

  button = new Button(buttonpos, buttondim, 7, color(255, 153, 51), "Bar Chart");
  Table data = loadTable("data.csv", "header");
  chart.setup(data, origin, topyaxis, rightxaxis);
}


void draw() {

  background(200, 200, 200);

  origin.setXY(margins[0], height - margins[3]);
  topyaxis.setXY(margins[0], margins[1]);
  rightxaxis.setXY(width - margins[2], height - margins[3]);

  chart.setAxes(origin, topyaxis, rightxaxis);
  chart.draw();

  buttonpos.setXY(width - buttondim.w - margins[2], margins[1]);
  button.draw();
}

void mouseClicked() {
  button.intersect(mouseX, mouseY);
  boolean selected = button.getIsect();
  if (selected) { 
    // click!
    chart.toggleChartSelection();
    if (chart.barchartSelected) {
      button.setText("Line Chart");
    } else {
      button.setText("Bar Chart");
    }
    button.setSelected(false);
  } 
}

void mouseMoved() {
  
}





