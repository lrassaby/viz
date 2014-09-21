import java.util.*;

public class BarTree implements SquarifiedChart {
    private Node root;
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 20, 20, 20}; // left, top, right, bottom
    private String hovertext;

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
        text(hovertext, mouseX, mouseY - 20);
      } else {
        textAlign(RIGHT, CENTER);
        text(hovertext, mouseX, mouseY - 20);
      }
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

    public void setHoverText(String hovertext) {
      this.hovertext = hovertext;
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