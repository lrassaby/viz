public class Temporal {
  private ArrayList<Box> boxes;

  public Temporal () {
  }
  public void draw() {
    // labels
    for (Box b : boxes) {
        b.draw();
    }
  }

  public void set_boxes(ArrayList<Box> boxes) {
    this.boxes = boxes;
  }
}