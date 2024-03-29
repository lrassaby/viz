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
    private Table data;
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {100, 80, 20, 200}; // left, top, right, bottom
    private String[] lines;
    private String[] orig_categories; // in order of how we want them
    private String[] categories; // in order of how we want them
    private int count;
    private String hovertext;
    private Button config_switcher;
    // private Button display_switcher;
    private Point config_switcher_xy;
    private Dimensions buttondim;
    // private Point display_switcher_xy;
    private int currentdisplay;
    private int currpermutation;
    private ArrayList<String[]> permutations;
    private int level;
    private ColorGenerator colors;
    private int clickedcount;
  
    BarTree (String filename) {
      colors = new ColorGenerator();
      count = 0;
      data = loadTable(filename, "header");
      lines = loadStrings(filename);
      categories = lines[0].split(",");
      for (int i = 0; i < categories.length; i++) { // trim whitespace
        categories[i] = categories[i].trim();
      }
      orig_categories = categories.clone();
      parseData();
      root = getRoot(tree);
      preprocessTree(root);
      buttondim = new Dimensions(170, 30);
      config_switcher_xy = new Point(width - buttondim.w - margins[2], margins[1]);
      // display_switcher_xy = new Point(width - buttondim.w - margins[2] - buttondim.w - 20, margins[1]);
      config_switcher = new Button(config_switcher_xy, buttondim, 7, color(255, 153, 51), "Change category order");;
      // display_switcher = new Button(display_switcher_xy, buttondim, 7, color(255, 153, 51), "Change display field");;
      /* sorry for this disgusting permutation code... */
      currpermutation = 0;
      currentdisplay = 0;
      permutations = new ArrayList<String[]>();
      String[] nonsizecats = Arrays.copyOfRange(categories, 0, categories.length - 1);
      calculatePermuatations(nonsizecats, 0, permutations);
      for (int i = 0; i < permutations.size(); i++) {
        permutations.set(i, Arrays.copyOf(permutations.get(i), permutations.get(i).length + 1));
        permutations.get(i)[permutations.get(i).length - 1] = categories[categories.length - 1];
      }
      currentdisplay = 0;
      clickedcount = 0;
    }

   private void calculatePermuatations(String[] cats, int index, ArrayList<String[]> ret){
    if(cats.length - index == 1) {
        ret.add(cats.clone());
    } else {
        for(int i = index; i < cats.length; i++){
            swap(cats, index, i);
            calculatePermuatations(cats, index+1, ret);
            swap(cats, index, i);
        }
    }
  }

    private void swap(String[] cats, int i1, int i2){
        String toswap = cats[i1];
        cats[i1] = cats[i2];
        cats[i2] = toswap;
    }


    public void setHoverText(String hovertext) {
      this.hovertext = hovertext;
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
            config_switcher.intersect(mouseX, mouseY);
            // display_switcher.intersect(mouseX, mouseY);
            if (config_switcher.getIsect()) {
                currpermutation = (currpermutation + 1) % permutations.size();
                setCategories(permutations.get(currpermutation));
                parseData();
                root = getRoot(tree);
                preprocessTree(root);
                config_switcher.setSelected(false);
                clicked = false;
            // } else if (display_switcher.getIsect()) {
                currentdisplay = (currentdisplay + 1) % (categories.length - 1);
                // display_switcher.setSelected(false);
                parseData();
                root = getRoot(tree);
                preprocessTree(root);
                clicked = false;
            } 
            clickedcount++;
            if (clickedcount > 10) {
              clickedcount = 0;
              clicked = false;
            }
        }

        float bar_width = canvas.w / root.children.size();
        if (root.children.size() != 0) {
          float ratio = canvas.h / root.children.get(0).size;

          for (int i = 0; i < root.children.size(); i++) {
              int x = PApplet.parseInt(canvas.x + bar_width * (i + 0.1f));
              int y = PApplet.parseInt(canvas.y + canvas.h - root.children.get(i).size * ratio);
              Canvas childcanvas = new Canvas(x, y, bar_width * 0.8f, canvas.y + canvas.h - y);
              root.children.get(i).draw(childcanvas);
              if (clicked && mouseX >= childcanvas.x && mouseX <= childcanvas.x + childcanvas.w && 
                mouseY >= childcanvas.y && mouseY <= childcanvas.y + childcanvas.h) {
                respondToClick();
                clicked = false;
              }
              if (root.children.size() > i) {
                String text = null;
                if (root.children.get(i).category != null) {
                  text = root.children.get(i).category;
                } else if (root.children.get(i).displaystring != null) {
                  text = root.children.get(i).displaystring;
                }
                if (text != null) {
                  makeText(text, PApplet.parseInt(x + bar_width * 0.4f), PApplet.parseInt(canvas.y + canvas.h + 10), true);
                }
              }
          }

          int increment;
          try {
              increment = PApplet.parseInt(25/abs(ratio));
          } catch (Exception e) {
              increment = 30;
          }
          if (root.children.size() > 0) {
            int currsize = root.children.get(0).size;
            for (int i = 0; i < currsize * 1.03f; i+= increment) {
                makeText(Integer.toString(i), PApplet.parseInt(canvas.x - 10), PApplet.parseInt(-i * ratio + canvas.y + canvas.h), false);
            }
          }
        } else {
          root.draw(canvas);
        }
       

        if (hovertext != null && mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
            drawHoverText();
        } 
        config_switcher_xy.setXY(width - buttondim.w - margins[2], margins[1] - 50);
        config_switcher.draw();
        // display_switcher_xy.setXY(width - buttondim.w - margins[2] - buttondim.w - 20, margins[1] - 50);
        // display_switcher.draw();
        drawCategories();
        drawAxes(canvas);
    }


    private void makeText(String str, int x, int y, boolean vert) {      
        textAlign(RIGHT, CENTER);
        if (vert) {
            pushMatrix();
            translate(x, y);
            rotate(-HALF_PI);
            translate(-x, -y);
            text(str, x, y);
            popMatrix();
        } else {
            text(str, x, y);
        }
    }

    public void drawAxes(Canvas canvas) {
        strokeWeight(2);
        line(canvas.x, canvas.y + canvas.h, canvas.x, canvas.y - 10);
        line(canvas.x, canvas.y + canvas.h, canvas.x + canvas.w, canvas.y + canvas.h);
    }

    private void drawCategories() {
      fill(0, 0, 0);
      textSize(12);
      textAlign(LEFT, CENTER);
      String categoriesText = "Categories: ";
      for (int i = 0; i < categories.length; i++) {
        categoriesText += categories[i];
        if (i != categories.length - 1) {
          categoriesText += ", ";
        }
      }
      text(categoriesText, margins[0] + 20, margins[1] - 42);
      if (level == categories.length) {
        text("Current level: Item", margins[0] + 20, margins[1] - 57);
      } else {
        text("Current level: " + categories[level], margins[0] + 20, margins[1] - 57);
      }
    }

    private void drawHoverText() {
      fill(0, 0, 0);
      textAlign(LEFT, CENTER);
      textSize(12);
      text(hovertext, margins[0] + 20, margins[1] - 27);

    }

    public void levelUp() {
        if (root.parent != null) {
            root = root.parent;
            level--;
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
                    level++;
                }
            }
        }
    }

    private Table convertToTable(Iterable<TableRow> t)
    {
      Table new_table = new Table();
      for (int i = 0; i < orig_categories.length; i++) {
        new_table.addColumn(orig_categories[i]); 
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
        if (cats.length == 1) { // leaf level 
          for (TableRow row : t.rows()) {
            Node newchild = new Node(Integer.toString(count++), "" /*row.getString(orig_categories[currentdisplay]) */, row.getInt(cats[0]), true, this);
            tree.put(newchild.name, newchild);
            newchild.hovertext = "(";
            for (int i = 0; i < categories.length; i++) {
              newchild.hovertext += row.getString(categories[i]);
              if (i != categories.length - 1) {
                newchild.hovertext += ", ";
              }
            }
            newchild.hovertext += ")";
            newchild.parent = root;
            root.children.add(newchild);
          }
        } else if (cats.length > 1) { // not leaf level
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
            child.category = s;
            if (child != null) {
              child.parent = root;
              root.children.add(child);
            }
          }
        }
        root.spacing = 0;
        root.name = Integer.toString(count++);
        root.displaystring = "";
        root.c = colors.generate();
        root.sqchart = this;
        tree.put(root.name, root); // add to the hash tree
      }

      return root;
    }


    private void parseData() {
      level = 0;
      tree = new HashMap();
      root = convertTable(data, categories);
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
public class Button {
  boolean isect;
  Point pos; 
  Dimensions dim;
  float roundness;
  int c;
  String text;
 

  public void draw() {
      strokeWeight(2);
      fill(c); 
      rect(pos.x, pos.y, dim.w, dim.h, roundness); 

      fill(0);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(text, pos.x + dim.w / 2, pos.y + dim.h / 2);
  }

  
  Button(Point pos, Dimensions dim, float roundness, int c, String text) { /* height and width of button */
     this.isect = false;
     this.pos = pos;
     this.dim = dim;
     this.roundness = roundness;
     this.c = c;
     this.text = text;
  }

  
  public void intersect (int mousex, int mousey) {
    /* if it's within x + width and y + height */
    if (mousex <= (pos.x + dim.w) && mousex >= pos.x && mousey <= (pos.y + dim.h) && mousey >= pos.y) {
      isect = true;
    }
    else {
      isect = false;
    }
  }
  
  public void setSelected (boolean s) {
    isect = s;
  }

  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public void setSize (Dimensions dim) {
    this.dim = dim;
  }
  
  public void setColor (int r, int g, int b) {
    c = color (r, g, b);
  }

  public void setColorObject (int c) {
    this.c = c;
  }
  
  
  public void setColor (int c) {
    this.c = c;
  }
  
  public void setText (String text) {
     this.text = text; 
  }

  public boolean getIsect() {return isect;}
  public Point getPos() {return pos;}
  public Dimensions getDim() {return dim;}
  public int getColor() {return c;}
  public float getRoundness() {return roundness;}
  public String getText() {return text;}
};


public class CSVTree implements SquarifiedChart {
    private Table data;
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 80, 20, 20}; // left, top, right, bottom
    private String[] lines;
    private String[] orig_categories; // in order of how we want them
    private String[] categories; // in order of how we want them
    private int count;
    private String hovertext;
    private Button config_switcher;
    private Button display_switcher;
    private Point config_switcher_xy;
    private Dimensions buttondim;
    private Point display_switcher_xy;
    private int currentdisplay;
    private int currpermutation;
    private ArrayList<String[]> permutations;
    private int level;
    private ColorGenerator colors;

  
    CSVTree (String filename) {
      colors = new ColorGenerator();
      count = 0;
      data = loadTable(filename, "header");
      lines = loadStrings(filename);
      categories = lines[0].split(",");
      for (int i = 0; i < categories.length; i++) { // trim whitespace
        categories[i] = categories[i].trim();
      }
      orig_categories = categories.clone();
      parseData();
      root = getRoot(tree);
      preprocessTree(root);
      buttondim = new Dimensions(170, 30);
      config_switcher_xy = new Point(width - buttondim.w - margins[2], margins[1]);
      display_switcher_xy = new Point(width - buttondim.w - margins[2] - buttondim.w - 20, margins[1]);
      config_switcher = new Button(config_switcher_xy, buttondim, 7, color(255, 153, 51), "Change category order");;
      display_switcher = new Button(display_switcher_xy, buttondim, 7, color(255, 153, 51), "Change display field");;
      /* sorry for this disgusting permutation code... */
      currpermutation = 0;
      currentdisplay = 0;
      permutations = new ArrayList<String[]>();
      String[] nonsizecats = Arrays.copyOfRange(categories, 0, categories.length - 1);
      calculatePermuatations(nonsizecats, 0, permutations);
      for (int i = 0; i < permutations.size(); i++) {
        permutations.set(i, Arrays.copyOf(permutations.get(i), permutations.get(i).length + 1));
        permutations.get(i)[permutations.get(i).length - 1] = categories[categories.length - 1];
      }
      currentdisplay = 0;
    }

   private void calculatePermuatations(String[] cats, int index, ArrayList<String[]> ret){
    if(cats.length - index == 1) {
        ret.add(cats.clone());
    } else {
        for(int i = index; i < cats.length; i++){
            swap(cats, index, i);
            calculatePermuatations(cats, index+1, ret);
            swap(cats, index, i);
        }
    }
  }

    private void swap(String[] cats, int i1, int i2){
        String toswap = cats[i1];
        cats[i1] = cats[i2];
        cats[i2] = toswap;
    }


    public void setHoverText(String hovertext) {
      this.hovertext = hovertext;
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
            config_switcher.intersect(mouseX, mouseY);
            display_switcher.intersect(mouseX, mouseY);
            if (config_switcher.getIsect()) {
                currpermutation = (currpermutation + 1) % permutations.size();
                setCategories(permutations.get(currpermutation));
                parseData();
                root = getRoot(tree);
                preprocessTree(root);
                config_switcher.setSelected(false);
                clicked = false;
            } else if (display_switcher.getIsect()) {
                currentdisplay = (currentdisplay + 1) % (categories.length - 1);
                display_switcher.setSelected(false);
                parseData();
                root = getRoot(tree);
                preprocessTree(root);
                clicked = false;
            } else if (mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
                respondToClick();
            } else {
                clicked = false;
            }

        }
        root.draw(canvas);
        if (hovertext != null && mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
            drawHoverText();
        } 
        config_switcher_xy.setXY(width - buttondim.w - margins[2], margins[1] - 50);
        config_switcher.draw();
        display_switcher_xy.setXY(width - buttondim.w - margins[2] - buttondim.w - 20, margins[1] - 50);
        display_switcher.draw();
        drawCategories();
    }


    private void drawCategories() {
      fill(0, 0, 0);
      textSize(12);
      textAlign(LEFT, CENTER);
      String categoriesText = "Categories: ";
      for (int i = 0; i < categories.length; i++) {
        categoriesText += categories[i];
        if (i != categories.length - 1) {
          categoriesText += ", ";
        }
      }
      text(categoriesText, margins[0] + 20, margins[1] - 42);
      if (level == categories.length) {
        text("Current level: Item", margins[0] + 20, margins[1] - 57);
      } else {
        text("Current level: " + categories[level], margins[0] + 20, margins[1] - 57);
      }
    }

    private void drawHoverText() {
      fill(0, 0, 0);
      textAlign(LEFT, CENTER);
      textSize(12);
      text(hovertext, margins[0] + 20, margins[1] - 27);
    }



    public void levelUp() {
        if (root.parent != null) {
            root = root.parent;
            level--;
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
                    level++;
                }
            }
        }
    }

    private Table convertToTable(Iterable<TableRow> t)
    {
      Table new_table = new Table();
      for (int i = 0; i < orig_categories.length; i++) {
        new_table.addColumn(orig_categories[i]); 
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
        if (cats.length == 1) { // leaf level 
          for (TableRow row : t.rows()) {
            Node newchild = new Node(Integer.toString(count++), row.getString(orig_categories[currentdisplay]), row.getInt(cats[0]), true, this);
            tree.put(newchild.name, newchild);
            newchild.hovertext = "(";
            for (int i = 0; i < categories.length; i++) {
              newchild.hovertext += row.getString(categories[i]);
              if (i != categories.length - 1) {
                newchild.hovertext += ", ";
              }
            }
            newchild.hovertext += ")";
            newchild.parent = root;
            root.children.add(newchild);
          }
        } else if (cats.length > 1) { // not leaf level
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
        root.c = colors.generate();
        root.sqchart = this;
        tree.put(root.name, root); // add to the hash tree
      }

      return root;
    }


    private void parseData() {
      level = 0;
      tree = new HashMap();
      root = convertTable(data, categories);
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
public class Color {
    int r, g, b;
    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public void setRGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
};

// see http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
public class ColorGenerator {
    private float golden_ratio_conjugate = 0.618033988749895f;
    private float h = 0.5f;
    public Color generate() {
        h += golden_ratio_conjugate;
        h %= 1;
        return hsvToRGB(h, 0.70f, 0.90f);
    }
    public Color hsvToRGB (float h, float s, float v) {
        float h_i = PApplet.parseInt(h*6);
        float f = h*6 - h_i;
        float p = v * (1 - s);
        float q = v * (1 - f*s);
        float t = v * (1 - (1 - f) * s);
        if (h_i == 0) {
            return new Color(PApplet.parseInt(v * 256), PApplet.parseInt(t * 256), PApplet.parseInt(p * 256));
        } else if (h_i == 1) {
            return new Color(PApplet.parseInt(q * 256), PApplet.parseInt(v * 256), PApplet.parseInt(p * 256));
        } else if (h_i == 2) {
            return new Color(PApplet.parseInt(p * 256), PApplet.parseInt(v * 256), PApplet.parseInt(t * 256));
        } else if (h_i == 3) {
            return new Color(PApplet.parseInt(p * 256), PApplet.parseInt(q * 256), PApplet.parseInt(v * 256));
        } else if (h_i == 4) {
            return new Color(PApplet.parseInt(t * 256), PApplet.parseInt(p * 256), PApplet.parseInt(v * 256));
        } else if (h_i == 5) {
            return new Color(PApplet.parseInt(v * 256), PApplet.parseInt(p * 256), PApplet.parseInt(q * 256));
        }
        return new Color(0, 0, 0);
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

public class Point {
    int x, y;
    String disp;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setDisp(String disp) {
        this.disp = disp;
    }
};

public class Dimensions {
    int w, h;
    Dimensions(int w, int h) {
        this.w = w;
        this.h = h;
    }
    public void setWH(int w, int h) {
        this.w = w;
        this.h = h;
    }
};

public class Node {
  public SquarifiedChart sqchart;
  public String name = null;
  public String displaystring = null; // if display string should be different from name
  public String hovertext = null;
  public Node parent = null;
  public int size;
  public boolean intersect = false;
  public Color c;
  public String category;

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
  public float spacing = 4;
  

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
        if (c != null) {
          fill(c.r, c.g, c.b);
        } else {
          fill(200, 200, 255);
        }
        n.intersect = true;
        if (hovertext != null) {
          tree.setHoverText(hovertext);
        }
      } else {
        fill(230);
        n.intersect = false;
      }


      stroke(0);
      strokeWeight(1);
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
    public void setHoverText(String hovertext);
    public void levelUp();
};

public class Tree implements SquarifiedChart {
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 20, 20, 20}; // left, top, right, bottom
    private String hovertext;
    private ColorGenerator colors;

    Tree (String filename) {
        colors = new ColorGenerator();
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
        if (hovertext != null && mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
            drawHoverText();
        } 
    }

    private void drawHoverText() {
      fill(0, 150, 150);
      textSize(20);
      if (mouseX < (width/2)) {
        textAlign(LEFT, CENTER);
        text(hovertext, mouseX, mouseY - 10);
      } else {
        textAlign(RIGHT, CENTER);
        text(hovertext, mouseX, mouseY - 10);
      }
    }

    public void setHoverText(String hovertext) {
      this.hovertext = hovertext;
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
        par.c = colors.generate();
        
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
