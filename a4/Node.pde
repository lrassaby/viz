public class Node {
	private String id;
	public float x, y;
    private float radius;
    public ArrayList<Edge> edges;
    public Boolean selected; 

	public Node(String id) {
        this.id = id;
        radius = 15;
        edges = new ArrayList();
        selected = false;
    }

	public void draw() {
        strokeWeight(1);

        if (selected) {
            fill(13, 134, 90, 255);
        } else {
            fill(5, 112, 204, 255);
        }
        
        stroke(0);
		ellipse(x, y, radius * 2, radius * 2);
	}

	public void add_edge(Edge edge) {
		edges.add(edge);
	}

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}
}