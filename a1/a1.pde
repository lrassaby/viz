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
           // println(lines[i + newIndex]);
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
  /*
  for (int i = 0; i <= 76; i++) {
    if (i == 8) {
      i = 10;
    }
    if (i > 7 && i%10 == 7) {
      i = i + 3;
    }
    Node node = (Node)tree.get(Integer.toString(i));
    println(i);
    println("Node: " + node.name);
    println("Size: " + node.size);
    println("Leaf?: " + node.isChild);
    if (i > 0) {
    println("Parent: " + node.parent.name);}
    println("Children:");
    for (int j = 0; j < node.children.size(); j++) {
      println(node.children.get(j).name);
    }
   */
    
  }
  
  //tree = new Tree(numLeafs, numRels, leafs, size, parents, children);

}
    


  // draw 2 lines
  // w/ labels
  // rotate axes with matrix transforms: pushMatrix, rotate(HALF_PI)
  // 
  // For animataion use frameRate() to throttle frames
  // lerp (linear interpolation)
  // use wide line rather than rectangle

void draw() {
  background(200, 200, 200);

}

void mouseClicked() {

}




