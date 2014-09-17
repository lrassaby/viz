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
      Node to_draw = elements.get(0);
      boolean worse = false;
      if (width > height) {
            dim1 = height, dim2 = (to_draw.size / total_magnitude) * width;
      } else {
            dim1 = width, dim2 = (to_draw.size / total_magnitude) * height;      
      }
      float old_aspect_ratio = dim2 / dim1;
      if (old_aspect_ratio < 0) old_aspect_ratio = 1/old_aspect_ratio;
      
      float old_size = to_draw.size;
      int i = 1;
      float newdim1;
      float newdim2;
      int used_magnitudes = 0;
      ArrayList<Node> draw_boxes = new ArrayList<Node>(); //boxes to be passed into makeRow or makeCol
      
      while (!worse) {
        to_draw = elements.get(i);
        newdim1 = (to_draw.size * height)/(to_draw.size + old_size);
        newdim2 = to_draw.size/newdim1;
        float new_aspect_ratio = newdim1/newdim2;
        if (new_aspect_ratio > 1) {new_aspect_ratio = 1/new_aspect_ratio;}
        if (new_aspect_ratio < old_aspect_ratio) {worse = true;}
        else {
          old_aspect_ratio = new_aspect_ratio;
          used_magnitudes += to_draw.size;
          draw_boxes.add(elements.get(i)); // add box to row
          elements.remove(i); // deletes box??
        }
      }
      
      int[] new_corners = {newdim2, width, 0, height}
      drawElements(elements, new_corners, total_magnitude - used_magnitudes);

      // recurse on the remaining elements

    }

    private void makeRow(ArrayList<Node> elements, int[] corners, int total_magnitude) { 
    }

    private void makeCol(ArrayList<Node> elements, int[] corners, int total_magnitude) {
    }


}

