public class Temporal {
  private ArrayList<Box> boxes;
  public final float margin_l = 90, margin_b = 20;
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
    for (int i = 0; i < 8; i++) {
      text(boxes.get(i).port, 5, i * h + (height - 200) + 15);
      line(0, (i + 1) * h + (height - 200), margin_l, (i + 1) * h + (height - 200));
    }
  }

  public void set_boxes(ArrayList<Box> boxes) {
    this.boxes = boxes;
  }
}