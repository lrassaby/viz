public class Tree {
  private ArrayList<Node> leaves;
  private String[] parents;
  
  public Tree(int numLeafs, int numRels, String[] leafs,
              int[] size, String[] parents, String[] children) {
      leaves = new ArrayList<Node>();
      for (int i = 0; i < numLeafs; i++) {
        //leaves[i] = new Node();
        leaves.add(i, new Node(leafs[i], size[i]));
      }
      int j;
      for (j =0; !(leafs[0].equals(children[j])); j++) {
          
      }
      for (int i = 0; i < numRels-j; i++) {
        leaves.get(i).parent = parents[i + j];
      }
              
      for (int i = 0; i < leaves.size(); i++) {
        println(leaves.get(i).name + " " + leaves.get(i).size + " " + leaves.get(i).parent);
      }
  }
  
}

public class Node {
    public String name;
    public String parent;
    public int size;
    public Node(String nm, int sz) {
      name = nm;
      size = sz;
    }
}
