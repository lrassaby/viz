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

	public void draw(color c, boolean hovered, int j) {
			strokeWeight(1);
			if (hovered && !splines[classification - 1]) {
				stroke(255, 0, 0, 255);
				line(a.x, a.y, b.x, b.y);
			}
			else if (!hovered && !splines[classification - 1]) {
				stroke(c, 100);
				float  dist_x = a.x - b.x;
				float dist_y = a.y - b.y;
				float angle = (float)Math.atan(dist_y/dist_x);
				angle *= 180/Math.PI;
				if (angle <= max_a && angle >= min_a) {
					stroke(0, 0, 255, 255);
				}
				line(a.x, a.y, b.x, b.y);
			}
			else if (hovered && splines[classification - 1]) {
				stroke(255, 0, 0, 255);
				if (j == 0) {
					curveVertex(a.x, a.y);
					curveVertex(a.x, a.y);
				}
				else if (j == 2) {
					curveVertex(b.x, b.y);
				}
				curveVertex(b.x, b.y);
			}
			else if (!hovered && splines[classification - 1]) {
				stroke(c, 100);
				if (j == 0) {
					curveVertex(a.x, a.y);
					curveVertex(a.x, a.y);				
				}
				else if (j == 2) {
					curveVertex(b.x, b.y);
				}
		 		curveVertex(b.x, b.y);
			}
	}
};
