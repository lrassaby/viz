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



Node root;
HashMap tree;

public void setup () {
  frame.setResizable(true);
  size(700, 700);
  
  readInput("hierarchy2.shf");
  root = getRoot(tree);
  int tree_size = preprocessTree(root);

  
  hashTest();
  // root.draw();
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

public class Line {
    Point start, end;
    String disp;
    Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    public void draw() {
        
    }
    public void setPoints(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
};
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
