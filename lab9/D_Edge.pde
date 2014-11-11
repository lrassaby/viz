public class D_Edge {
	public Big_Node a; 
	public Big_Node b;
	public float optimal_length;
	public final float SPRING_MULTIPLE = 0.5e-1;

	public D_Edge(Big_Node a, Big_Node b, float optimal_length) {
		this.a = a;
		this.b = b;
		this.optimal_length = optimal_length;
	}

	public void draw() {
	    strokeWeight(2);
	    stroke(40, 58, 127);
		line(a.x, a.y, b.x, b.y);
	}
	
	public float hookesForce() {
		return SPRING_MULTIPLE * (a.distance(b) - optimal_length);
	}
}
