public class Network {
  private ArrayList<Edge> edges;
  private HashMap nodes;
  private float[] canvas;

  public Network (float[] canvas) {
  	this.canvas = canvas;
  }

  public void draw() {
  	for (Object key: nodes.keySet()) {
  		Node n = (Node)(nodes.get(key));
        n.draw();
    } 
  	for (Edge e : edges) {
  		e.draw();
  	}
  }

  public void add_edges (ArrayList<Edge> edges) {
  	this.edges = edges;
  	for (Edge e : edges) {
  		Node s = (Node)(nodes.get(e.source));
  		Node d = (Node)(nodes.get(e.dest));
  		e.x1 = s.x;
  		e.y1 = s.y;
  		e.x2 = d.x;
  		e.y2 = d.y;
  	}
  }

  public void add_nodes (HashMap nodes) {
  	this.nodes = nodes;
  	ArrayList<Node> r = new ArrayList();
  	for (Object key: nodes.keySet()) {
           r.add((Node)(nodes.get(key)));
    }
  	Node n1 = r.get(0);
  	Node n2 = r.get(1);
  	Node n3 = r.get(2);
  	Node n4 = r.get(3);
  	Node n5 = r.get(4);
  	Node n6 = r.get(5);
  	Node n7 = r.get(6);
  	Node n8 = r.get(7);
  	Node n9 = r.get(8);
  	Node n10 = r.get(9);
  	Node n11 = r.get(10);
  	Node n12 = r.get(11);
  	Node n13 = r.get(12);
  	Node n14 = r.get(13);
  	Node n15 = r.get(14);
  	Node n16 = r.get(15);
  	Node n17 = r.get(16);
  	Node n18 = r.get(17);
  	Node n19 = r.get(18);
  	n1.x = 10;
  	n1.y = 10;
  	n2.x = 10;
  	n2.y = 20;
  	n3.x = 40;
  	n3.y = 70;
  	n4.x = 100;
  	n4.y = 100;
  	n5.x = 200;
  	n5.y = 150;
  	n6.x = 0;
  	n6.y = 340;
  	n7.x = 500;
  	n7.y = 80;
  	n8.x = 100;
  	n8.y = 500;
  	n9.x = 500;
  	n9.y = 200;
  	n10.x = 900; //*********
  	n10.y = 243;
  	n11.x = 162;
  	n11.y = 450;
  	n12.x = 233;
  	n12.y = 459;
  	n13.x = 204;
  	n13.y = 102;
  	n14.x = 589;
  	n14.y = 23;
  	n15.x = 502;
  	n15.y = 430;
  	n16.x = 543;
  	n16.y = 294;
  	n17.x = 750;
  	n17.y = 120;
  	n18.x = 900; //*********
  	n18.y = 432;
  	n19.x = 0;
  	n19.y = 493;
  }
}