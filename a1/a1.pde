import java.util.*;

int[] margins = {50, 50, 50, 50}; // left, top, right, bottom
Node root;
HashMap tree;

void draw () {
  background(255, 255, 255);
  root.draw(new Canvas(margins[0], margins[1],width - margins[2], height - margins[3]);
}

void setup () {
  frame.setResizable(true);
  size(700, 700);
  
  readInput("hierarchy2.shf");
  root = getRoot(tree);
  int tree_size = preprocessTree(root);

  
  //hashTest();
  root.draw();
}

void readInput(String filename) {
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


Node getRoot(HashMap tree) {
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

int preprocessTree(Node root) {

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

void hashTest() {
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






