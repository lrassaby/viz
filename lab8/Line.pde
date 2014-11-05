/* holds two endpoints of the line and its class */
public class Line {
	Point a;
	Point b;
	int classification;

	Line(Point a, Point b, int classification) {
		this.a = a;
		this.b = b;
		this.classification = classification;
	} 

	public void draw(color c, boolean hovered, ArrayList<Line> lines) {
			strokeWeight(1);
			if (hovered) {
				stroke(255, 0, 0);
				for (Line l : lines) {
					line(l.a.x, l.a.y, l.b.x, l.b.y);
				}
			}
			else {
				stroke(c, 120);
			}
		line(a.x, a.y, b.x, b.y);
	}
};
