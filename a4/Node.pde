public class Node {
	private String id;
	public float x, y;
    private float radius;

	public Node(String id) {
        this.id = id;
        radius = 4;
    }

	public void draw() {
        strokeWeight(2);
        fill(200, 60, 60);
        stroke(100, 30, 30);
		ellipse(x, y, radius * 2, radius * 2);
	}

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}
}