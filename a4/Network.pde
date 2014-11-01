public class Network {
  private ArrayList<Edge> edges;
  private HashMap nodes;
  private float[] canvas;

  public Network (float[] canvas) {
  	this.canvas = canvas;
  }

  public void draw() {
    resize();
    for (Edge e : edges) {
      e.draw();
    }
    for (Object key: nodes.keySet()) {
      Node n = (Node)(nodes.get(key));
      n.draw();
    } 
  }

  public void add_edges (ArrayList<Edge> edges) {
  	this.edges = edges;
  }

  public void add_nodes (HashMap nodes) {
    this.nodes = nodes;
  }

  private void resize () {
    float center_x = width/2;
    float center_y = height/2;
    float margin = 20;
    float radius = min(width, height)/2 - margin;

    float nodeangle = 0, serverangle = 0;
    int servercount = 2;
    int nodecount = nodes.size() - servercount;
    int count = 0;
    for (Object key : nodes.keySet()) {
      Node n = (Node)(nodes.get(key));
      if (count != 9 && count != 17) {
        n.x = center_x + cos(nodeangle) * radius;
        n.y = center_y + sin(nodeangle) * radius;
        nodeangle += (2 * PI) / nodecount;
      } else {
        n.x = center_x + cos(nodeangle) * radius/2;
        n.y = center_y + sin(nodeangle) * radius/2;
        serverangle += (2 * PI) / servercount;
      }
      count += 1;
    }
    for (Edge e : edges) {
      Node s = (Node)(nodes.get(e.source));
      Node d = (Node)(nodes.get(e.dest));
      e.x1 = s.x;
      e.y1 = s.y;
      e.x2 = d.x;
      e.y2 = d.y;
    }
  }
}