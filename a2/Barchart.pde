public class Barchart extends AxisChart {
    private float weight = 0.8;

    Barchart(Table data, String[] categories) {
        super(data, categories);
    }

    void draw () {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        drawAxes();
        drawLabels();
        drawData();
    }


    void drawData() {
        float ratio = float(topyaxis.y - origin.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        strokeWeight(sectionWidth * weight);
        stroke(0);
        strokeCap(SQUARE);
        for (int i = 0; i < data.getRowCount(); i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = int(data.getRow(i).getInt(categories[1]) * ratio) + origin.y;
            line(x, origin.y, x, y);
        }
    }
};