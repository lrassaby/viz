int[] margins = {120, 30, 30, 120}; // left, top, right, bottom
Button button = new Button(new Point(width - 50, height + 20), new Dimensions(70, 30), 7, color(255, 153, 51), "Bar Chart");
Chart chart = new Chart();
Point origin, 
      topyaxis, 
      rightxaxis;

void setup () {
  frame.setResizable(true);
  size(700, 700);

  origin = new Point(margins[0], height - margins[3]);
  topyaxis = new Point(margins[0], margins[1]); 
  rightxaxis = new Point(width - margins[2], height - margins[3]);

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
}

void mouseClicked() {
  button.intersect(mouseX, mouseY);
}
void mouseMoved() {
  button.intersect(mouseX, mouseY);
}





