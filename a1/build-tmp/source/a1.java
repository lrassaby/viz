import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class a1 extends PApplet {



int[] margins = {50, 50, 50, 50}; // left, top, right, bottom
Node root;
HashMap tree;

public void draw () {
  background(255, 255, 255);
  root.draw(new Canvas(margins[0], margins[1], width - margins[2] - margins[0], height - margins[3] - margins[1]));
}

public void setup () {
  frame.setResizable(true);
  size(700, 700);
  
  readInput("hierarchy2.shf");
  root = getRoot(tree);
  int tree_size = preprocessTree(root);
}

public void readInput(String filename) {
  tree = new HashMap();
  String lines[] = loadStrings(filename);

  /* takes in leaves */
  int num_leaves = parseInt(lines[0]);
  for (int i = 1; i <= num_leaves; i++) {
    String[] temp = split(lines[i], ' ');
    // temp[0] is the name of the node
    // temp[1] is its size
    tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true));
  }

  /* take in relationships */
  int num_rels = parseInt(lines[num_leaves+1]);
  for (int i = num_leaves + 2; i < num_rels + num_leaves + 2; i++) {
    String[] temp = split(lines[i], ' ');
    // temp[0] is the name of the parent
    // temp[1] is the name of the child
    if (!(tree.containsKey(temp[0]))) { // tree doesn't have the parent
      tree.put(temp[0], new Node(temp[0], 0, false)); // add the parent
    }
    if (!(tree.containsKey(temp[1]))) { // tree doesn't have the child
      tree.put(temp[1], new Node(temp[1], 0, false)); // add the child (size 0)
    }

    /* add the child to the parent and the parent to the child */
    Node par = (Node)tree.get(temp[0]);
    Node chi = (Node)tree.get(temp[1]);
    
    par.children.add(chi);
    chi.parent = par;
  }
}


public Node getRoot(HashMap tree) {
  Node root = null;
  for (Object value : tree.values()) {
      Node v = (Node)value;
      Node p = (Node)v.parent;
      if (p != null) {
      } else {
        root = v;
      }
  }
  return root;
}

public int preprocessTree(Node root) {

  // base case: we're at a leaf
  if (root.isLeaf) {
    return root.size;
  } 

  // recursive case: we have children
  int tree_size = 0;
  for (Object childobj : root.children) {
    Node child = (Node)childobj;
    tree_size += preprocessTree(child);
  }
  root.size = tree_size;
  // sort children by size
  Collections.sort(root.children, new Comparator<Node>() {
    @Override
    public int compare(Node a, Node b) {
      return Integer.compare(b.size, a.size);
    }
  });

  return tree_size;
}

public void hashTest() {
  Node root = null;
  for (Object value : tree.values()) {
      Node v = (Node)value;
      println("Name: " + v.name);
      println("Size: " + v.size);
      Node p = (Node)v.parent;
      if (p != null) {
        println("Parent: " + p.name);
      } else {
        root = v;
      }
      println("Children: ");
      if (!(v.isLeaf)) {
        for (int i = 0; i < v.children.size(); i++) {
          Node c = (Node)v.children.get(i);
          println(c.name + " of size " + c.size);
        }
      }
      println("");
  }
}






public class Rect {
    float d_short, d_long;
    String name;

    Rect(float d_short, float d_long, String name) {
        this.d_short = d_short;
        this.d_long = d_long;
        this.name = name;
    }
    // value between 0 and 1, where closer to 1 is more square
    public float getAspectRatio() {
        float aspect_ratio = d_short / d_long;
        if (aspect_ratio > 1) {
            aspect_ratio = 1 / aspect_ratio;
        }
        return aspect_ratio;
    }

    public float size() {
        return d_short * d_long;
    }

};

public class Canvas {
  float x, y, w, h;
  Canvas(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  public float size() {
    return w * h;
  }
  public float getShortSide() {
    return w <= h ? w : h;
  }
  public float getLongSide() {
    return w <= h ? h : w;
  }
};
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
  private float spacing = 2;
  

  // draw function for first element
  public void draw (Canvas canvas) { // xmin, xmax, ymin, ymax
    ArrayList<Rect> root_arraylist = new ArrayList<Rect>();
    root_arraylist.add(new Rect(canvas.getShortSide(), canvas.getLongSide(), name));
    drawSide(root_arraylist, canvas);
    drawChildren(canvas);
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
            side.get(i).draw(new Canvas(x + spacing, y + spacing, w - 2 * spacing, h - 2 * spacing));
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

      fill(230);
      rect(x, y, w, h);

      fill(0);
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
