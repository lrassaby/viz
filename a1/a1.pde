int[] margins = {120, 30, 30, 120}; // left, top, right, bottom
int numLeafs;
String[] leafs;
int[] size;
int numRels;
String[] parents;
String[] children;
Tree tree;

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
  }
  tree = new Tree(numLeafs, numRels, leafs, size, parents, children);

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




