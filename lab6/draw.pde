int canvasWidth = MIN_INT; // this would be initialized in setup
ArrayList<float[]> dataSet = new ArrayList<float[]>();

void draw() {
  clearCanvas();
  float margin = canvasWidth * .2;
  strokeWeight(1);
  stroke(0, 0, 0);
  line(margin, canvasWidth - margin, canvasWidth - margin, canvasWidth - margin);
  line(margin, margin, margin, canvasWidth - margin);

  for (int i = 0; i < dataSet.size(); i++) {
  	   float[] newPoint = dataSet.get(i);
  	   fill(150);
  	   ellipse((canvasWidth - margin - (newPoint[0]/maxPoint[0]) * (canvasWidth - 2 * margin)), (canvasWidth - margin - (newPoint[1]/maxPoint[1]) * (canvasWidth - 2 * margin)), 7, 7);
  }
}
