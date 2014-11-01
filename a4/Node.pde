public class Node {
	private String id;
	public float x, y;
    private float radius;
    public ArrayList<Edge> edges;

	public Node(String id) {
        this.id = id;
        radius = 4;
        edges = new ArrayList();
    }

	public void draw() {
        strokeWeight(2);
        fill(200, 60, 60);
        stroke(100, 30, 30);
		ellipse(x, y, radius * 2, radius * 2);
	}

	public void add_edge(Edge edge) {
		edges.add(edge);
	}

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}
}