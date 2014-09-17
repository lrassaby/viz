public class Node {
    public String name = null;
    public Node parent = null;
    public int size;
    private static final int XMIN = 0, XMAX = 1, YMIN = 2, YMAX = 3;
    public ArrayList<Node> children = new ArrayList<Node>();
    boolean isLeaf;
    public Node(String nm, int sz, boolean lf) {
      name = nm;
      size = sz;
      isLeaf = lf;
    }
    // draw function here, recursing on children
    void draw (int[] corners) { // xmin, xmax, ymin, ymax
      // copying children so that popping from arraylist doesn't affect the node
      ArrayList children_copy = new ArrayList(children);
      drawElements(children_copy, corners, size);
    }

    private void drawElements(ArrayList<Node> elements, int[] corners, int total_magnitude) {
      float width = corners[XMAX] - corners[XMIN];
      float height = corners[YMAX] - corners[YMIN];
      float canvas_size = width * height;
      float VA_ratio = total_magnitude / canvas_size;
      Node to_draw = elements[0];

      // fill row or column
      while () {
        if (width > height) {
            float tempheight = height, tempwidth = (to_draw.size / total_magnitude) * width;
        } else {
            float tempheight = height, tempwidth = (to_draw.size / total_magnitude) * width;
            float aspect_ratio = width / height;
        }
        float aspect_ratio = width / height;
      }

      // recurse on the remaining elements

    }

    public void makeRectangle() {

    }


}

