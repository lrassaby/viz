public class Node {
  public Tree tree;
  public String name = null;
  public Node parent = null;
  public int size;
  public boolean intersect = false;

  private static final int XMIN = 0, XMAX = 1, YMIN = 2, YMAX = 3;
  public ArrayList<Node> children = new ArrayList<Node>();
  boolean isLeaf;
  public float x, y, d_short, d_long;
  public Node(String nm, int sz, boolean lf, Tree tr) {
    name = nm;
    size = sz;
    isLeaf = lf;
    tree = tr;
  }
  private float spacing = 3;
  

  // draw function for first element
  void draw (Canvas canvas) { // xmin, xmax, ymin, ymax
    ArrayList<Rect> root_arraylist = new ArrayList<Rect>();
    root_arraylist.add(new Rect(canvas.getShortSide(), canvas.getLongSide(), name));
    drawSide(root_arraylist, canvas);
    Canvas newcanvas = new Canvas(canvas.x + spacing, canvas.y + spacing, canvas.w - 2 * spacing, canvas.h - 2 * spacing);
    drawChildren(newcanvas);
  }


  private void drawChildren(Canvas canvas) {
    if (!isLeaf && children.size() > 0) {
      drawElements(children, canvas, size, 0);
    }
  }

  private void drawElements(ArrayList<Node> elements, Canvas canvas, int total_magnitude,
                            int index) {
    ArrayList<Node> side = new ArrayList<Node>();
    ArrayList<Rect> oldSide = null;
    ArrayList<Rect> newSide = null;
    boolean worse = false;
    float used_magnitude = 0;


    do {
      used_magnitude += elements.get(index).size; 
      side.add(elements.get(index++));
      oldSide = newSide;
      newSide = assembleSide(canvas.size()/(float)total_magnitude, canvas.getShortSide(), side, used_magnitude);
      if (oldSide != null) {
        /* does the aspect ratio get worse? */
        Rect new_rectangle = newSide.get(newSide.size() - 1); // changed from newSide.size - 2, because we're actually comparing c2's aspect ratio to c1's according to remco
        Rect old_rectangle = oldSide.get(oldSide.size() - 1); 

        if (new_rectangle.getAspectRatio() > old_rectangle.getAspectRatio()) {
          // keep the change
        } else {
          index--;
          used_magnitude -= elements.get(index).size;
          worse = true;
          drawSide(oldSide, canvas);

          // TODO: condense
          float x = canvas.x;
          float y = canvas.y;
          for (int i = 0; i < side.size() - 1; i++) {
            float w = canvas.w <= canvas.h ? oldSide.get(i).d_short : oldSide.get(i).d_long;
            float h = canvas.w <= canvas.h ? oldSide.get(i).d_long : oldSide.get(i).d_short;
            side.get(i).draw(new Canvas(x, y, w, h));
            if (canvas.w <= canvas.h) {
              x += w;
            } else {
              y += h;
            }
          }
        } 
      }

      if (index >= elements.size()) { // if the last child improves the aspect ratio
        drawSide(newSide, canvas);

        // TODO: condense
        float x = canvas.x;
        float y = canvas.y;
        for (int i = 0; i < side.size(); i++) {
          float w = canvas.w <= canvas.h ? newSide.get(i).d_short : newSide.get(i).d_long;
          float h = canvas.w <= canvas.h ? newSide.get(i).d_long : newSide.get(i).d_short;
          side.get(i).draw(new Canvas(x, y, w, h));
          if (canvas.w <= canvas.h) {
            x += w;
          } else {
            y += h;
          }
        }

        return;
      }
    }
    while (!worse);

    float x_offset = canvas.w <= canvas.h ? 0 : oldSide.get(0).d_long;
    float y_offset = canvas.w <= canvas.h ? oldSide.get(0).d_long : 0;

    Canvas newcanvas = new Canvas(canvas.x + x_offset,
                               canvas.y + y_offset,
                               canvas.w - x_offset,
                               canvas.h - y_offset);
    // println("new canvas starting at (" + newcanvas.x + ", " + newcanvas.y + "), with dimensions (" + newcanvas.w + ", " + newcanvas.h + ")");
    drawElements(elements, newcanvas, (int) total_magnitude - (int) used_magnitude, index);

  }

  //ratio is the proportion of the canvas that the side should take up
  private ArrayList<Rect> assembleSide(float ratio, float short_side, ArrayList<Node> nodes, float magnitude) {
    ArrayList<Rect> side = new ArrayList<Rect>();
    for (Node n : nodes) {
      float n_short_side = (n.size/ magnitude) * short_side;
      float n_long_side = n.size * ratio/n_short_side;

      side.add(new Rect(n_short_side, n_long_side, n.name));
    }
    return side;
  }

  private void drawSide(ArrayList<Rect> rectangles, Canvas canvas) {
    float x = canvas.x;
    float y = canvas.y;

    for (Rect r : rectangles) {
      float w = canvas.w <= canvas.h ? r.d_short : r.d_long;
      float h = canvas.w <= canvas.h ? r.d_long : r.d_short;

      Node n = tree.getNode(r.name);
      if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
        fill(200, 200, 255);
        n.intersect = true;
      } else {
        fill(230);
        n.intersect = false;
      }
      stroke(0);
      rect(x, y, w, h);

      fill(0);
      stroke(0);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(r.name, x + w / 2, y + h / 2);

      if (canvas.w <= canvas.h) {
        x += w;
      } else {
        y += h;
      }
    }
  }
}