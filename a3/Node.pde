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

      
}
