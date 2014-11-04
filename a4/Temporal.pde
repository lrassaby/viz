public class Temporal {
  private ArrayList<Box> boxes;
  public final float margin_l = 90, margin_b = 40;
  public Temporal () {
  }
  public void draw() {
    // labels
    for (Box b : boxes) {
        b.draw();
    }
    textSize(10);
    fill(0);
    textAlign(LEFT);
    float h = (200 - margin_b)/8;
    float w = (width - margin_l)/31;
    for (int i = 0; i < 8; i++) {
      text(boxes.get(i).port, 5, i * h + (height - 200) + 15);
      line(0, (i + 1) * h + (height - 200), margin_l, (i + 1) * h + (height - 200));
    }
    for (int i = 0; i < 31; i++) {
      makeText(boxes.get(i * 8).time, int(w * i + margin_l) - 15, height-5, -HALF_PI/2);
      line(int(w * i + margin_l) - 40, height, int(w * i + margin_l), height - margin_b);
    }
    line(int(w * 31 + margin_l) - 40, height, int(w * 31 + margin_l), height - margin_b);
  }

  public void set_boxes(ArrayList<Box> boxes) {
    this.boxes = boxes;
  }
}