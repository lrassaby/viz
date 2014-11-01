public class Node {
	private ArrayList<Edge> edges;
	private ArrayList<Node> nodes;
	private String id;
	private float x, y, mass, system_mass;
    private float dx, dy;
    private boolean selected;
    private float x_velocity, y_velocity; 
	private float x_acceleration, y_acceleration;
	private float radius;
    private float energy;
	private final float AREA_MULTIPLE = 60;
	private final float UPDATE_MULTIPLE = 10;
    private final float COULOMB_MULTIPLE = 2e3;
    private final float CENTER_COERSION_MULTIPLE = 1e-2;
    private final float DAMPING = 0.95;

	public Node(String id, float mass, float system_mass) {
		this.id = id;
		this.mass = mass;
		this.system_mass = system_mass;
		x_velocity = 0;
		y_velocity = 0;
		x_acceleration = 0;
		y_acceleration = 0;
        energy = 0;
		edges = new ArrayList<Edge>();
		radius = sqrt(mass / system_mass) * AREA_MULTIPLE;
		x = random(radius, width - radius);
		y = random(radius, height - radius);
	}

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.dx = x - mouseX;
            this.dy = y - mouseY;
        }
    }

    public boolean isSelected() {
        return selected;
    }

	public void draw(boolean colored) {
        strokeWeight(2);
        if (colored) {
            fill(200, 60, 60);
            stroke(100, 30, 30);
        } else {
            fill(154, 175, 255);
            stroke(40, 58, 127);
        }
		ellipse(x, y, radius * 2, radius * 2);
	}

    public void drawHoverBox() {
        fill(180, 180, 180, 128);
        strokeWeight(0);
        rect(x - 20, y - radius - 60, 90, 50, 15);
        fill(200, 60, 60);
        text("id: " + id, x - 10, y - radius - 50, 80, 50); 
        text("mass: " + mass, x - 10, y - radius - 40, 80, 50); 
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

	public float coulombForce(Node node) {
        float force = COULOMB_MULTIPLE / pow(distance(node), 2);
        if (force != force) { // NaN
            force = 0;
        }
        return force;
	}

    public float distance(Node node) {
        return max(sqrt(pow((this.x - node.x), 2) + pow((this.y - node.y), 2)), 1);
    }

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}

	public void update() {
        float sum_force_x = 0;
        float sum_force_y = 0;

        // coulomb's law 
        for (Node n : nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n) * UPDATE_MULTIPLE;
                float dist = distance(n);
                float dir_force_x = (this.x - n.x)/dist;
                float dir_force_y = (this.y - n.y)/dist;
                sum_force_x += dir_force_x * force;
                sum_force_y += dir_force_y * force;
            }
        }

        // hooke's law
        for (Edge e : edges) {
            float force = e.hookesForce() * UPDATE_MULTIPLE;
            float dist_a = distance(e.a);
            float dist_b = distance(e.b);
            float dist = e.a.distance(e.b);

            float dir_force_x = dist_a > dist_b ? (e.a.x - e.b.x) / dist : (e.b.x - e.a.x) / dist;
            float dir_force_y = dist_a > dist_b ? (e.a.y - e.b.y) / dist : (e.b.y - e.a.y) / dist; 

            sum_force_x += dir_force_x * force;
            sum_force_y += dir_force_y * force;
        }

        /* coerce the system towards the center */
        sum_force_x += (width/2 - this.x) * CENTER_COERSION_MULTIPLE;
        sum_force_y += (height/2 - this.y) * CENTER_COERSION_MULTIPLE;

        x_acceleration = (sum_force_x / mass);
        y_acceleration = (sum_force_y / mass);
        x_velocity += x_acceleration - (1 - DAMPING) * x_velocity;
        y_velocity += y_acceleration - (1 - DAMPING) * y_velocity;
		x += x_velocity * (1 / frameRate);
		y += y_velocity * (1 / frameRate);

        if (selected) {
            x = mouseX + dx;
            y = mouseY + dy;
        }

        if (x > width - radius - 1) {
            x = width - radius;
        } else if (x < radius) {
            x = radius;
        }
        if (y > height - radius - 1) {
            y = height - radius;
        } else if (y < radius) {
            y = radius;
        }

        energy = (x_velocity * x_velocity + y_velocity * y_velocity) * mass;
	}
}

