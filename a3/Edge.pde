public class Edge {
	public Node a; 
	public Node b;
	public float optimal_length;
	public final float SPRING_MULTIPLE = 400;

	public Edge(Node a, Node b, float optimal_length) {
		this.a = a;
		this.b = b;
		this.optimal_length = optimal_length;
	}

	public void draw() {
		line(a.x, a.y, b.x, b.y);
	}
	
	public float hookesForce() {
		return  SPRING_MULTIPLE * (a.distance(b) - optimal_length);
	}
}