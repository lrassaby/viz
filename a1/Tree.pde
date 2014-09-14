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
}
