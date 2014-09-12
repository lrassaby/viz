int[] margins = {120, 30, 30, 120}; // left, top, right, bottom
Button button = new Button(new Point(width - 50, height + 20), new Dimensions(70, 30), 7, color(255, 153, 51), "Bar Chart");
Linechart linechart = new Linechart();
Table data;

void setup () {
  frame.setResizable(true);
  size(700, 700);
 
  data = loadTable("data.csv");
}



  // draw 2 lines
  // w/ labels
  // rotate axes with matrix transforms: pushMatrix, rotate(HALF_PI)
  // 
  // For animataion use frameRate() to throttle frames
  // lerp (linear interpolation)
  // use wide line rather than rectangle

void draw() {
  background(200, 200, 200);

  linechart.setAxes(new Point(margins[0], height - margins[3]), 
                    new Point(margins[0], margins[1]), 
                    new Point(width - margins[2], height - margins[3]));
  linechart.drawAxes();
  linechart.draw();
  // Hover: on mouse move do stuff
  // background(255, 255, 255);
  // Button button = buttons[currbutton];
  
}

void mouseClicked() {
  button.intersect(mouseX, mouseY);
}




