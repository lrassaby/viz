import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.*; 
import java.util.*; 
import java.util.*; 
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



SquarifiedChart tree;

public void draw () {
  background(255, 255, 255);
  tree.draw();
}

public void setup () {
  frame.setResizable(true);
  size(1000, 800);

  String file = null;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 

  Object[] possibleValues = { "Hierarchical Treemap", "Nonhierarchical Treemap", "Barchart Treemap" };
  Object selectedValue = JOptionPane.showInputDialog(null,
  "What sort of treemap?", "Input",
  JOptionPane.INFORMATION_MESSAGE, null,
  possibleValues, possibleValues[0]);
  if (selectedValue == null) {
    println("Process cancelled.");
    exit();
  } else if (selectedValue == "Hierarchical Treemap") {
    try {
      File openfile;
      file = JOptionPane.showInputDialog(frame, "Input file (type shf)", "hierarchy2.shf");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }

    if (file == null) {
      println("Process cancelled.");
      exit();
    } else {
      try {
        tree = new Tree(file);
      } catch (Exception e) {
        e.printStackTrace();    
        exit();
      }
    }
  } else if (selectedValue == "Nonhierarchical Treemap") {
    try {
      File openfile;
      file = JOptionPane.showInputDialog(frame, "Input file (type csv)", "soe-funding.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }

    if (file == null) {
      println("Process cancelled.");
      exit();
    } else {
      try {
        tree = new CSVTree(file);
      } catch (Exception e) {
        e.printStackTrace();    
        exit();
      }
    }
  } else if (selectedValue == "Barchart Treemap") {
    try {
      File openfile;
      file = JOptionPane.showInputDialog(frame, "Input file (type csv)", "soe-funding.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }

    if (file == null) {
      println("Process cancelled.");
      exit();
    } else {
      try {
        tree = new BarTree(file);
      } catch (Exception e) {
        e.printStackTrace();    
        exit();
      }
    }
  } else {
    JOptionPane.showMessageDialog(null, "Sorry, this feature hasn't been implemented.");
    exit();
  }
}

public void mousePressed () {

  if (mouseButton == LEFT) {
    tree.setClicked(true);
  } else if (mouseButton == RIGHT) {
    tree.levelUp();
  } 
}







public class BarTree implements SquarifiedChart {
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 20, 20, 20}; // left, top, right, bottom

    BarTree (String filename) {
        readInput(filename);
        root = getRoot(tree);
        preprocessTree(root);
    }

    public void setClicked (boolean val){
        clicked = val;
    }

    public void draw () {
        Canvas canvas = new Canvas(margins[0], margins[1], 
            width - margins[2] - margins[0], height - margins[3] - margins[1]);
        if (clicked) {
            if (mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
                respondToClick();
            } else {
                clicked = false;
            }
        }
        root.draw(canvas);
    }

    public void levelUp() {
        if (root.parent != null) {
            root = root.parent;
        }
    }

    public void setRoot(String rootname) {
        Node tryroot = (Node) tree.get(rootname);
        if (tryroot != null) {
            root = tryroot;
        }
    }

    public Node getNode(String name) {
        return (Node)tree.get(name);
    }

    private void respondToClick() {
        if (root.isLeaf) {
            clicked = false;
        } else {
            for (Node n : root.children) {
                // println(n.name);
                if (n.intersect) {
                    root = n;
                    clicked = false;
                }
            }
        }
    }

    private void readInput(String filename) {
      tree = new HashMap();
      String lines[] = loadStrings(filename);

      /* takes in leaves */
      int num_leaves = parseInt(lines[0]);
      for (int i = 1; i <= num_leaves; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the node
        // temp[1] is its size
        tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true, this));
      }

      /* take in relationships */
      int num_rels = parseInt(lines[num_leaves+1]);
      for (int i = num_leaves + 2; i < num_rels + num_leaves + 2; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the parent
        // temp[1] is the name of the child
        if (!(tree.containsKey(temp[0]))) { // tree doesn't have the parent
          tree.put(temp[0], new Node(temp[0], 0, false, this)); // add the parent
        }
        if (!(tree.containsKey(temp[1]))) { // tree doesn't have the child
          tree.put(temp[1], new Node(temp[1], 0, false, this)); // add the child (size 0)
        }

        /* add the child to the parent and the parent to the child */
        Node par = (Node)tree.get(temp[0]);
        Node chi = (Node)tree.get(temp[1]);
        
        par.children.add(chi);
        chi.parent = par;
      }
    }

    private Node getRoot(HashMap tree) {
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

    private int preprocessTree(Node root) {

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
};


public class CSVTree implements SquarifiedChart {
    private Table data;
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 20, 20, 20}; // left, top, right, bottom
    private String[] lines;
    private String[] categories; // in order of how we want them
    private int count;

    CSVTree (String filename) {
      count = 0;
      data = loadTable(filename, "header");
      lines = loadStrings(filename);
      categories = lines[0].split(",");
      for (int i = 0; i < categories.length; i++) { // trim whitespace
        categories[i] = categories[i].trim();
      }
      parseData();
      root = getRoot(tree);
      preprocessTree(root);
      //treeTest(root);
    }

    private void treeTest(Node r) {
      if (r != null) {
        println("at a node");
        for (Node child : r.children) {
          treeTest(child);
        }
      }
    }

    public void setCategories(String[] categories) {
      this.categories = categories;
    }

    public String[] getCategories() {
      return categories;
    }

    public void setClicked (boolean val){
        clicked = val;
    }

    public void draw () {
        Canvas canvas = new Canvas(margins[0], margins[1], 
            width - margins[2] - margins[0], height - margins[3] - margins[1]);
        if (clicked) {
            if (mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
                respondToClick();
            } else {
                clicked = false;
            }
        }
        root.draw(canvas);
    }

    public void levelUp() {
        if (root.parent != null) {
            root = root.parent;
        }
    }

    public void setRoot(String rootname) {
        Node tryroot = (Node) tree.get(rootname);
        if (tryroot != null) {
            root = tryroot;
        }
    }

    public Node getNode(String name) {
      try {
        return (Node)tree.get(name);
      } catch (Exception e) {
        println("could not find " + name);
      } 
      return null;
    }

    private void respondToClick() {
        if (root.isLeaf) {
            clicked = false;
        } else {
            for (Node n : root.children) {
                // println(n.name);
                if (n.intersect) {
                    root = n;
                    clicked = false;
                }
            }
        }
    }

    private Table convertToTable(Iterable<TableRow> t)
    {
      Table new_table = new Table();
      for (int i = 0; i < categories.length; i++) {
        new_table.addColumn(categories[i]); 
      }
      for (TableRow r : t) {
        new_table.addRow(r);
      }
      return new_table;
    }

    private Node convertTable(Table t, String[] cats) {
      Node root = null;
      if (t.getRowCount() > 0) {
        root = new Node();
        if (cats.length == 2) { // leaf level 
          for (TableRow row : t.rows()) {
            Node newchild = new Node(Integer.toString(count++), row.getString(cats[1]), row.getInt(cats[1]), true, this);
            tree.put(newchild.name, newchild);

            newchild.parent = root;
            root.children.add(newchild);
          }
        } else if (cats.length > 2) { // not leaf level
          String[] remainingCats = Arrays.copyOfRange(cats, 1, cats.length);

          ArrayList<String> distinct = new ArrayList<String>();
          for (TableRow row : t.rows()) {
            if (!distinct.contains(row.getString(cats[0]))) {
              distinct.add(row.getString(cats[0]));
            }
          }
          for (String s : distinct) {
            Iterable<TableRow> rows = t.findRows(s, cats[0]);
            Node child = convertTable(convertToTable(rows), remainingCats);
            if (child != null) {
              child.parent = root;
              root.children.add(child);
            }
          }
        }
        root.name = Integer.toString(count++);
        root.sqchart = this;
        tree.put(root.name, root); // add to the hash tree
      }

      return root;
    }


    private void parseData() {
      tree = new HashMap();
      root = convertTable(data, categories);

      // /* construct tree */
      // int num_leaves = parseInt(lines[0]);
      // for (int i = 1; i <= num_leaves; i++) {
      //   String[] temp = split(lines[i], ' ');
      //   // temp[0] is the name of the node
      //   // temp[1] is its size
      //   tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true, this));
      // }

      // /* take in relationships */
      // int num_rels = parseInt(lines[num_leaves+1]);
      // for (int i = num_leaves + 2; i < num_rels + num_leaves + 2; i++) {
      //   String[] temp = split(lines[i], ' ');
      //   // temp[0] is the name of the parent
      //   // temp[1] is the name of the child
      //   if (!(tree.containsKey(temp[0]))) { // tree doesn't have the parent
      //     tree.put(temp[0], new Node(temp[0], 0, false, this)); // add the parent
      //   }
      //   if (!(tree.containsKey(temp[1]))) { // tree doesn't have the child
      //     tree.put(temp[1], new Node(temp[1], 0, false, this)); // add the child (size 0)
      //   }

      //   /* add the child to the parent and the parent to the child */
      //   Node par = (Node)tree.get(temp[0]);
      //   Node chi = (Node)tree.get(temp[1]);
        
      //   par.children.add(chi);
      //   chi.parent = par;
      // }
    }

    private Node getRoot(HashMap tree) {
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

    private int preprocessTree(Node root) {

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
};
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
  public SquarifiedChart sqchart;
  public String name = null;
  public String displaystring = null; // if display string should be different from name
  public Node parent = null;
  public int size;
  public boolean intersect = false;

  private static final int XMIN = 0, XMAX = 1, YMIN = 2, YMAX = 3;
  public ArrayList<Node> children = new ArrayList<Node>();
  boolean isLeaf;
  public float x, y, d_short, d_long;
  public Node() {

  }
  public Node(String nm, int sz, boolean lf, SquarifiedChart sqc) {
    name = nm;
    size = sz;
    isLeaf = lf;
    sqchart = sqc;
  }
  public Node(String nm, String ds, int sz, boolean lf, SquarifiedChart sqc) {
    name = nm;
    displaystring = ds;
    size = sz;
    isLeaf = lf;
    sqchart = sqc;
  }
  private float spacing = 4;
  

  // draw function for first element
  public void draw (Canvas canvas) { // xmin, xmax, ymin, ymax
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

      Node n = sqchart.getNode(r.name);
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

      if (n.displaystring != null) {
        text(n.displaystring, x + w / 2, y + h / 2);
      } else {
        text(n.name, x + w / 2, y + h / 2);
      }

      if (canvas.w <= canvas.h) {
        x += w;
      } else {
        y += h;
      }
    }
  }
}
interface SquarifiedChart {
    public Node getNode(String name);
    public void draw();
    public void setClicked(boolean val);
    public void levelUp();
};

public class Tree implements SquarifiedChart {
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 20, 20, 20}; // left, top, right, bottom
    Tree (String filename) {
        readInput(filename);
        root = getRoot(tree);
        preprocessTree(root);
    }

    public void setClicked (boolean val){
        clicked = val;
    }

    public void draw () {
        Canvas canvas = new Canvas(margins[0], margins[1], 
            width - margins[2] - margins[0], height - margins[3] - margins[1]);
        if (clicked) {
            if (mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
                respondToClick();
            } else {
                clicked = false;
            }
        }
        root.draw(canvas);
    }

    public void levelUp() {
        if (root.parent != null) {
            root = root.parent;
        }
    }

    public void setRoot(String rootname) {
        Node tryroot = (Node) tree.get(rootname);
        if (tryroot != null) {
            root = tryroot;
        }
    }

    public Node getNode(String name) {
        return (Node)tree.get(name);
    }

    private void respondToClick() {
        if (root.isLeaf) {
            clicked = false;
        } else {
            for (Node n : root.children) {
                // println(n.name);
                if (n.intersect) {
                    root = n;
                    clicked = false;
                }
            }
        }
    }

    private void readInput(String filename) {
      tree = new HashMap();
      String lines[] = loadStrings(filename);

      /* takes in leaves */
      int num_leaves = parseInt(lines[0]);
      for (int i = 1; i <= num_leaves; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the node
        // temp[1] is its size
        tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true, this));
      }

      /* take in relationships */
      int num_rels = parseInt(lines[num_leaves+1]);
      for (int i = num_leaves + 2; i < num_rels + num_leaves + 2; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the parent
        // temp[1] is the name of the child
        if (!(tree.containsKey(temp[0]))) { // tree doesn't have the parent
          tree.put(temp[0], new Node(temp[0], 0, false, this)); // add the parent
        }
        if (!(tree.containsKey(temp[1]))) { // tree doesn't have the child
          tree.put(temp[1], new Node(temp[1], 0, false, this)); // add the child (size 0)
        }

        /* add the child to the parent and the parent to the child */
        Node par = (Node)tree.get(temp[0]);
        Node chi = (Node)tree.get(temp[1]);
        
        par.children.add(chi);
        chi.parent = par;
      }
    }

    private Node getRoot(HashMap tree) {
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

    private int preprocessTree(Node root) {

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
};
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
