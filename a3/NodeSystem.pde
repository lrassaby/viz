public class NodeSystem {
  private ArrayList<Node> nodes;
  private ArrayList<Edge> edges;
  private float total_energy;

  public NodeSystem(String filename) {
    nodes = new ArrayList<Node>();
    edges = new ArrayList<Edge>();
    readInput(filename);
    total_energy = 0;
  }

  public void draw() {
    for (Edge e : edges) {
      e.draw();
    }
    float energy = 0;
    Node hovernode = null;
    Node selectednode = null;
    for (Node n : nodes) {
      boolean hover = n.intersect(), selected = n.isSelected();
      if (hover) {
        hovernode = n;
        n.draw(hover);
      } else if (selected) {
        selectednode = n;
        n.draw(selected);
      } else {
        n.draw(false);
      }
      energy += n.energy;
    }
    if (selectednode != null) {
      selectednode.drawHoverBox();
    } else if (hovernode != null) {
      hovernode.drawHoverBox();
    }

    total_energy = energy;
    fill(40, 58, 127);
    text("Total energy: " + total_energy, 10, 15); 
  }  

  public void update() {
    for (Node n : nodes) {
      n.update();
    }
  }

  public void mousePressed() {
    for (Node n : nodes) {
      if (mousePressed) {
        if(n.intersect()) {
          n.setSelected(true);
        } else {
          n.setSelected(false);
        }
      } else {
        n.setSelected(false);
      }
    }
  }

  public void mouseReleased() {
    for (Node n : nodes) {
      n.setSelected(false);
    }
  }
  


  private void readInput(String filename) {
    HashMap nodes_map = new HashMap();
    String lines[] = loadStrings(filename);
    int total_mass = 0;
    int num_nodes = parseInt(lines[0]);
    int num_edges = parseInt(lines[num_nodes + 1]);
    
    /* calculate total mass */
    for (int i = 1; i <= num_nodes; i++) {
      String[] temp = split(lines[i], ',');
      total_mass += parseInt(temp[1]);
    }
    /* add nodes to arraylist */
    for (int i = 1; i <= num_nodes; i++) {
      String[] temp = split(lines[i], ',');
      // temp[0] is the name of the node
      // temp[1] is its mass
      Node n = new Node(temp[0], parseInt(temp[1]), total_mass);
      nodes_map.put(temp[0], n);
      nodes.add(n);
    }

    /* add edges to arraylist */
    for (int i = num_nodes + 2; i <= num_nodes + num_edges + 1; i++) {
      String[] temp = split(lines[i], ',');
      // temp[0], temp[1] are the names of the nodes
      // temp[2] is the optimal length of the spring
      Node brother = (Node)nodes_map.get(temp[0]);
      Node sister = (Node)nodes_map.get(temp[1]);
      Edge e = new Edge(brother, sister, float(parseInt(temp[2])));
      brother.addEdge(e);
      sister.addEdge(e);
      edges.add(e);
    }
    /* add all nodes to each node */
    for (Node n : nodes) {
      n.nodes = nodes;
    }
  }
}
