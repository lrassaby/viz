import java.util.*;
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
              int x = int(canvas.x + bar_width * (i + 0.1));
              int y = int(canvas.y + canvas.h - root.children.get(i).size * ratio);
              Canvas childcanvas = new Canvas(x, y, bar_width * 0.8, canvas.y + canvas.h - y);
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
                  makeText(text, int(x + bar_width * 0.4), int(canvas.y + canvas.h + 10), true);
                }
              }
          }

          int increment;
          try {
              increment = int(25/abs(ratio));
          } catch (Exception e) {
              increment = 30;
          }
          if (root.children.size() > 0) {
            int currsize = root.children.get(0).size;
            for (int i = 0; i < currsize * 1.03; i+= increment) {
                makeText(Integer.toString(i), int(canvas.x - 10), int(-i * ratio + canvas.y + canvas.h), false);
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

    void drawAxes(Canvas canvas) {
        strokeWeight(2);
        line(canvas.x, canvas.y + canvas.h, canvas.x, canvas.y);
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