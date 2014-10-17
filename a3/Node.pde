public class Node {
	public ArrayList<Edge> edges;
	public ArrayList<Node> nodes;
	public String label;
	public float x, y, mass, x_velocity, y_velocity; 
	public float  x_acceleration, y_acceleration, system_mass;
	public float radius;
	public boolean hover;
	public final float PROPORTION = 100;
	public final float UPDATE_MULTIPLE = 1;

	public Node(String label, float mass, float system_mass) {
		this.label = label;
		this.mass = mass;
		this.system_mass = system_mass;
		hover = false;
		x_velocity = 0;
		y_velocity = 0;
		x_acceleration = 0;
		y_acceleration = 0;
		edges = new ArrayList<Edge>();
		nodes = new ArrayList<Node>();
		radius = sqrt(((mass / system_mass) * PROPORTION));
		x = random(radius, width - radius);
		y = random(radius, height - radius);
	}

	public void draw() {
		ellipse(x, y, radius, radius);
	}

	public float total_energy() {
		return 0;
	}

	public float coulombs_law(Node node) {

	}

	public boolean intersect(Node node) {

	}

	public void update() {
		x += (x_velocity * (1 / frameRate)) * UPDATE_MULTIPLE;
		y += (y_velocity * (1 / frameRate)) * UPDATE_MULTIPLE;
		x_velocity += (x_acceleration * (1 / frameRate)) * UPDATE_MULTIPLE;
		y_velocity += (y_acceleration * (1 / frameRate)) * UPDATE_MULTIPLE;
	}

	private void readInput(String filename) {
      tree = new HashMap();
      String lines[] = loadStrings(filename);

      /* takes in leaves */
      int num_leaves = parseInt(lines[0]);
      for (int i = 1; i <= num_leaves; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the node
        // temp[1] is its size
        tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true, this));
      }

      /* take in relationships */
      int num_rels = parseInt(lines[num_leaves+1]);
      for (int i = num_leaves + 2; i < num_rels + num_leaves + 2; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the parent
        // temp[1] is the name of the child
        if (!(tree.containsKey(temp[0]))) { // tree doesn't have the parent
          tree.put(temp[0], new Node(temp[0], 0, false, this)); // add the parent
        }
        if (!(tree.containsKey(temp[1]))) { // tree doesn't have the child
          tree.put(temp[1], new Node(temp[1], 0, false, this)); // add the child (size 0)
        }

        /* add the child to the parent and the parent to the child */
        Node par = (Node)tree.get(temp[0]);
        Node chi = (Node)tree.get(temp[1]);
        par.c = colors.generate();
        
        par.children.add(chi);
        chi.parent = par;
      }
    }
}