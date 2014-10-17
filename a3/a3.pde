NodeSystem diagram;

void setup() {
    frame.setResizable(true);
    size(1000, 800);
    diagram = new NodeSystem("data.csv");
}

void draw() {
    diagram.draw();
}