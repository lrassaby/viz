void makeText(String str, int x, int y, float rotation) {      
    if (rotation != 0) {
        pushMatrix();
        translate(x, y);
        rotate(rotation);
        translate(-x, -y);
        text(str, x, y);
        popMatrix();
    } else {
        text(str, x, y);
    }
}

void drawCircle(int x, int y, float diameter) {
    ellipse(x, y, diameter, diameter);
}