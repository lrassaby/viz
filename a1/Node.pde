public class Node {
    public String name = null;
    public Node parent = null;
    public int size;
    public ArrayList<Node> children = new ArrayList<Node>();
    boolean isLeaf;
    public Node(String nm, int sz, boolean lf) {
      name = nm;
      size = sz;
      isLeaf = lf;
    }
    // draw function here, recursing on children
    void draw (int x_min, int x_max, int y_min, int y_max) {
      // rect(x_min, x_max, y_min, y_max);

      int height, width;
      float ratio;
      for (Node child : children) {
        // put the rectangle on the shorter side
        ratio = child.size / size;
        height = x_max - x_min, width = y_max - y_min;
        if (height > width) {
          int child_width = width;
          int child_height = height * ratio;
          int child_origin_x = x_min;
          int child_origin_y = y_max - child_height;

          // x_min = x_min; (implicit)
          // x_max = x_max;
          // y_min = y_min
          y_max = y_max - child_height;

          child.draw(child_origin_x, child_origin_y, 
            child_origin_x + child_width, child_origin_y + child_height);

        // place the rectangle on the bottom
        // TODO: add offset between sections
          // update x_min, x_max, y_min, y_max
          // do the draw call on the child
        } else {
          // place the rectangle on the left
          // update x_max, x_min, y_max, y_min
          // do the draw call on the child
        }
      }

    }
}
