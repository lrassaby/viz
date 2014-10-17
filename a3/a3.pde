NodeSystem diagram;

void setup() {
    frame.setResizable(true);
    size(1000, 800);
    background(255);
    diagram = new NodeSystem("data.csv");
}

void draw() {
    diagram.draw();
    // diagram.update();
}