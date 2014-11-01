public class Node {
	private String id;
	public float x, y;
    private float radius;

	public Node(String id) {
        this.id = id;
        radius = 10;
    }

	public void draw() {
        strokeWeight(1);
        fill(0, 150, 200);
        stroke(0);
		ellipse(x, y, radius * 2, radius * 2);
	}

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}
}