import java.util.*;

int[] margins = {120, 30, 30, 120}; // left, top, right, bottom
int numLeafs;
String[] leafs;
int[] size;
int numRels;
String[] parents;
String[] children;
HashMap tree = new HashMap();

void setup () {
  frame.setResizable(true);
  size(700, 700);
  
  readInput();
}

void readInput() {
  String lines[] = loadStrings("hierarchy2.shf");
  numLeafs = parseInt(lines[0]);
  String[] temp = new String[2];
  leafs = new String[numLeafs];
  size = new int[numLeafs];
  for (int i = 1; i <= numLeafs; i++) {
    temp = split(lines[i], ' ');
    leafs[i-1] = temp[0];

    size[i-1] = parseInt(temp[1]);
    tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true));
    
  }
  numRels = parseInt(lines[numLeafs+1]);
  parents = new String[numRels];
  children = new String[numRels];
  int newIndex = numLeafs + 2;
  for (int i = 0; i <= numRels-1; i++) {
    temp = split(lines[newIndex + i], ' ');
    parents[i] = temp[0];
    children[i] = temp[1];
    if (!(tree.containsKey(temp[0]))) {
      tree.put(temp[0], new Node(temp[0], 0, false));
    }
    if (!(tree.containsKey(temp[1]))) {
      tree.put(temp[1], new Node(temp[1], 0, false));
    }
    Node par = (Node)tree.get(temp[0]);
    Node chi = (Node)tree.get(temp[1]);
    
    par.children.add(chi);
    chi.parent = par;
  }
  hashTest();
}


void hashTest() {
  Node root = null;
  for (Object value : tree.values()) {
      Node v = (Node)value;
      println("Name: " + v.name);
      println("Size: " + v.size);
      Node p = (Node)v.parent;
      if (p != null) {
        println("Parent: " + p.name);
      }
      println("Children: ");
      if (!(v.isLeaf)) {
        for (int i = 1; i < v.children.size(); i++) {
          Node c = (Node)v.children.get(i);
          println(c.name);
        }
      }
      println("");
  }
    
}




