public class Node {
    public String name = null;
    public Node parent = null;
    public int size;
    private static final int XMIN = 0, XMAX = 1, YMIN = 2, YMAX = 3;
    public ArrayList<Node> children = new ArrayList<Node>();
    boolean isLeaf;
    public float x, y, d_short, d_long;
    public Node(String nm, int sz, boolean lf) {
      name = nm;
      size = sz;
      isLeaf = lf;
    }
    
    // draw function here, recursing on children
    void draw (Canvas canvas) { // xmin, xmax, ymin, ymax
      drawElements(children, canvas, size, 0);
    }

    private void drawElements(ArrayList<Node> elements, Canvas canvas, int total_magnitude,
                              int index) {
      float VA_ratio = canvas.size/total_magnitude;
      float used_magnitude = 0;
      boolean worse = false;
      Node c1 = elements.get(index);
      float c1_ratio = 0;
      c1.d_short = canvas.short_side;
      c1.d_long = (float)c1.size*VA_ratio/canvas.short_side;
      c1_ratio = c1.d_short/c1.d_long;
      used_magnitude = c1.size;

      int i = index + 1;
      while (!worse) {
        Node c2 = elements.get(i);
        
        
        
        
        i++;
      }
        
        
        
        /*float old_size = to_draw.size;
        int i = 1;
        float new_shortside;
        float new_longside;
        int used_magnitudes = 0;
        ArrayList<Node> draw_boxes = new ArrayList<Node>(); //boxes to be passed into makeRow or makeCol
      
      while (!worse) {
        to_draw = elements.get(i);
        new_shortside = ((float)to_draw.size * h)/((float)to_draw.size + old_size);
        new_longside = (float)to_draw.size/new_shortside;
        float new_aspect_ratio = new_shortside/new_longside;
        if (new_aspect_ratio > 1) {new_aspect_ratio = 1/new_aspect_ratio;}
        if (new_aspect_ratio < old_aspect_ratio) {worse = true;}
        else {
          old_aspect_ratio = new_aspect_ratio;
          used_magnitudes += (float)to_draw.size;
          draw_boxes.add(elements.get(i)); // add box to row
          elements.remove(i); // deletes box??
        }
      }
      
    float[] new_corners = {new_longside, w, 0, h};
      drawElements(elements, new_corners, total_magnitude - used_magnitudes);

      // recurse on the remaining elements*/

    }

    private void makeRow(ArrayList<Node> elements, int[] corners, int total_magnitude) { 
    }

    private void makeCol(ArrayList<Node> elements, int[] corners, int total_magnitude) {
    }


}

