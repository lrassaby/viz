public class Node {
	public ArrayList<Edge> edges;
	public ArrayList<Node> nodes;
	public String id;
	public float x, y, mass, system_mass;
    public float velocity, x_vel_direction, y_vel_direction; 
	public float acceleration, x_accel_direction, y_accel_direction;
	public float radius;
	public boolean hover;
	public final float AREA_MULTIPLE = 100;
	public final float UPDATE_MULTIPLE = 1;
    public final float COULOMB_MULTIPLE = 1;

	public Node(String id, float mass, float system_mass) {
		this.id = id;
		this.mass = mass;
		this.system_mass = system_mass;
		hover = false;
		x_velocity = 0;
		y_velocity = 0;
		x_acceleration = 0;
		y_acceleration = 0;
		edges = new ArrayList<Edge>();
		nodes = new ArrayList<Node>();
		radius = sqrt((mass / system_mass) * AREA_MULTIPLE);
		x = random(radius, width - radius);
		y = random(radius, height - radius);
	}

	public void draw() {
		ellipse(x, y, radius, radius);
	}

	public float coulombForce(Node node) {
        return (COULOMB_MULTIPLE * node.mass * this.mass) / (distance(node) ^ 2);
	}

    public float distance(Node node) {
        sqrt((this.x - node.x) ^ 2 + (this.y - node.y) ^ 2);
    }

	public boolean intersect() {
        return sqrt((mouseX - this.x) ^ 2 + (mouseY - this.y) ^ 2) < radius;
	}

	public void update() {
        for (Node n : nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n);
                x_acceleration = (force / mass) * (n.x - this.x);
                y_acceleration = (force / mass) * (n.y - this.y);
            }
        }
        for (Edge e : edges) {
            float force = e.hookesForce(n);
            float x_component = e.a.label == this.label ? (e.a.x - e.b.x) : (e.b.x - e.a.x);
            float y_component = e.a.label == this.label ? (e.a.y - e.b.y) : (e.b.y - e.a.y);
            x_acceleration = (force / mass) * x_component;
            y_acceleration = (force / mass) * y_component;   
        }

        x_velocity += (y_acceleration * (1 / frameRate)) * UPDATE_MULTIPLE;
        y_velocity += (y_acceleration * (1 / frameRate)) * UPDATE_MULTIPLE;
		x += (x_velocity * (1 / frameRate)) * UPDATE_MULTIPLE;
		y += (y_velocity * (1 / frameRate)) * UPDATE_MULTIPLE;
		x_velocity += (x_acceleration * (1 / frameRate)) * UPDATE_MULTIPLE;
		y_velocity += (y_acceleration * (1 / frameRate)) * UPDATE_MULTIPLE;
	}
}