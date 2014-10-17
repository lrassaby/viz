public class NodeSystem {
        HashMap nodes = new HashMap(); 
        ArrayList<Edge> edges = new ArrayList<Edge>();
        
        public NodeSystem(String filename) {
           readInput(filename);
        }
        
        
        private void readInput(String filename) {
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
            nodes.put(temp[0], new Node(temp[0], parseInt(temp[1]), total_mass));
          }
      
          /* add edges to arraylist */
          for (int i = num_nodes + 2; i <= num_nodes + num_edges + 1; i++) {
            String[] temp = split(lines[i], ',');
            // temp[0], temp[1] are the names of the nodes
            // temp[2] is the optimal length of the spring
            Node brother = (Node)nodes.get(temp[0]);
            Node sister = (Node)nodes.get(temp[1]);
            edges.add(new Edge(brother, sister, float(parseInt(temp[2]))));
          }
          
      }
}
