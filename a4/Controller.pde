public class Controller {
  private Table table;
  private ArrayList<Edge> edges;
  private ArrayList<Box> boxes;

  public Controller(String filename) {
    table = loadTable(filename, "header");
    processTable();
  }
  
  private void processTable() {
    ArrayList<String> ips = new ArrayList();
    HashMap edges_map = new HashMap();
    HashMap boxes_map = new HashMap();
    int i;
    //create edges list
    for (i = 0; i < table.getRowCount(); i++) {
      String IP = table.getString(i, "Source IP");
      String IP2 = table.getString(i, "Destination IP");
      if (!edges_map.containsKey(IP+IP2)) {
        edges_map.put(IP+IP2, new Edge(table.getRow(i)));
      }
      else {
        Edge e = (Edge)(edges_map.get(IP+IP2));
        e.update(table.getRow(i));
      }
      Edge e = (Edge)(edges_map.get(IP+IP2));
      e.add_weight();
    }

    //create box list
    for (i = 0; i < table.getRowCount(); i++) {
      String t = table.getString(i, "Time");
      String port = table.getString(i, "Destination port");
      if (!boxes_map.containsKey(t+port)) {
        boxes_map.put(t+port, new Box(table.getRow(i)));
      }
      Box b = (Box)(boxes_map.get(t+port));
      b.add_weight();  
      b.map_edges(edges_map);
    }

    println("EDGES: ");
    for (Object key: edges_map.keySet()) {
            Edge e = (Edge)edges_map.get(key);
            println(e.source + ": " + e.dest + ", weight: " + e.weight);
            println("Corresponding boxes: ");
            for (Box b: e.boxes) {
              println(b.time + ": " + b.port); 
            }
            println();
    }

    println("BOXES: ");
    for (Object key: boxes_map.keySet()) {
            Box b = (Box)boxes_map.get(key);
            println(b.time + ": " + b.port + ", weight: " + b.weight);
            println("Corresponding edges: ");
            for (Edge e: b.edges) {
              println(e.source + ": " + e.dest);
            }
            println();
    }
  }
}
