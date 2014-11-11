public class NodeSystem {
  private ArrayList<Big_Node> big_nodes;
  private ArrayList<Edge> edges;
  private float total_energy;


  public NodeSystem(String filename) {
    big_nodes = new ArrayList<Big_Node>();
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
    for (Big_Node n : big_nodes) {
      n.draw(false);
      energy += n.energy;
    }
    total_energy = energy;
    fill(40, 58, 127);
    text("Total energy: " + total_energy, 10, 15); 
  }  

  public void update() {
    for (Big_Node n : big_nodes) {
      n.update();
    }
  }

  /*public void mousePressed() {
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
  }*/
  


  private void readInput(String filename) {
    HashMap nodes_map = new HashMap();
    String lines[] = loadStrings(filename);
    int num_nodes = parseInt(lines[0]);
    int curr_line = 1;
    
    /* add nodes to arraylist */
    for (int i = 1; i <= num_nodes; i++) {
      String[] temp = split(lines[curr_line], ',');
      curr_line ++;
      // temp[0] is the name of the node
      // temp[1] is the # of names in the cluster
      // temp[2] is the # of inner relationships

      Big_Node n = new Big_Node(temp[0], parseInt(temp[1]), num_nodes);
      for (int j = 0; j < parseInt(temp[1]); j++) {
        Node l = new Node(lines[curr_line + j]);
        n.addName(l);
        nodes_map.put(lines[curr_line + j], l);
      }
      curr_line += parseInt(temp[1]);

      for (int j = 0; j < parseInt(temp[2]); j++) {
        String[] temp2 = split(lines[curr_line + j], ',');
        // temp[0], temp[1] are the names of the nodes
        // temp[2] is the weight of the edge
        Node brother = (Node)nodes_map.get(temp2[0]);
        Node sister = (Node)nodes_map.get(temp2[1]);
        n.addEdge(brother, sister, parseInt(temp2[2]));
      }
      curr_line += parseInt(temp[2]);
      
      nodes_map.put(temp[0], n);
      big_nodes.add(n);
    }

    /* add edges to arraylist */
    for (int i = curr_line; i < lines.length; i++) {
      String[] temp = split(lines[i], ',');
      // temp[0], temp[1] are the names of the nodes
      // temp[2] is the optimal length of the spring
      Node brother = (Node)nodes_map.get(temp[0]);
      Node sister = (Node)nodes_map.get(temp[2]);
      Edge e = new Edge(brother, sister, 1);
      brother.addEdge(e);
      sister.addEdge(e);
      edges.add(e);
    }
    /* add all nodes to each node */
    for (int i = 0; i < num_nodes; i++) {
      Big_Node n = big_nodes.get(i);
      n.big_nodes = big_nodes;
      for (int j = i + 1; j < num_nodes; j++) {
        Big_Node b = big_nodes.get(j);
        D_Edge e = new D_Edge(n, b, 15);
        n.addDummyEdge(e);
        b.addDummyEdge(e);
      }
    }
  }

}


/*

  public void test() {
    for (Big_Node n : big_nodes) {
      println("ID: " + n.id);
      println("Children: ");
      for (Node l : n.nodes) {
        println(" "+l.id+ ", with siblings: ");
        for (Pair p : l.inner_edges) {
          Node t =(Node)(p.first);
          println("  "+t.id+", "+p.second);
        }
      }
      println();
      println();
    }
    println("Outer Edges:");
    for (Edge e : edges) {
      println(e.a.id + ", " + e.b.id);
    }

*/

