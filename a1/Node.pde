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
      ArrayList<Node> side = new ArrayList<Node>(elements.get(0));
      ArrayList<Rect> oldSide = NULL;
      ArrayList<Rect> newSide = NULL;
      boolean worse = false;
      int i = 1;

      do {
        oldSide = newSide;
        newSide = assembleSide(canvas.size/total_magnitude, canvas.shortside, side);
        if (oldSide != NULL) {
          Rect temp_new = newSide.get(newSide.size - 2);
          Rect temp_old = oldSide.get(oldSide.size - 1); 
          if (temp_new.getAspectRatio() < temp_old.getAspectRatio())
            worse = true;
        }
        i++;
      }
      while (!worse);

    }
    //ratio is the proportion of the canvas that the side should take up
    private ArrayList<Rect> assembleSide(float ratio, float shortside, ArrayList<Node> nodes) {
         
    }
}


      // float VA_ratio = canvas.size/total_magnitude;
      // float used_magnitude = 0;
      // boolean worse = false;
      // Node c1 = elements.get(index);
      // float c1_ratio = 0;
      // c1.d_short = canvas.short_side;
      // c1.d_long = (float)c1.size*VA_ratio/canvas.short_side;
      // c1_ratio = c1.d_short/c1.d_long;
      // used_magnitude = c1.size;

      // int i = index + 1;
      // while (!worse) {
      //   Node c2 = elements.get(i);
        
        
        
        
      //   i++;
      // }
