HashMap tree = new HashMap();

void setup () {
  frame.setResizable(true);
  size(700, 700);
  
  readInput("hierarchy2.shf");
}

void readInput(String filename) {
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
        for (int i = 0; i < v.children.size(); i++) {
          Node c = (Node)v.children.get(i);
          println(c.name);
        }
      }
      println("");
  }
}




