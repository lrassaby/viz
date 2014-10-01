public class AxisChart {
    protected Table data;
    protected String[] categories;
    protected float maxY;
    protected int[] margins = {80, 30, 120, 100};
    protected Point origin, topyaxis, rightxaxis;

    AxisChart (Table data, String[] categories) {
        this.data = data;
        maxY = data.getRow(0).getInt(categories[1]);
        for (TableRow row : data.rows()) {
            int rowweight = row.getInt(categories[1]);
            if (rowweight > maxY) {
                maxY = rowweight;
            }
        }
        this.categories = categories;
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]); 
        rightxaxis = new Point(width - margins[2], height - margins[3]);
    }

 
    void drawAxes(color c) {
        strokeWeight(2);
        stroke(c);
        fill(c);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y - 5);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }

    protected void drawLabels(color c) {
        stroke(c);
        fill(c);
        textSize(16); 
        textAlign(RIGHT, CENTER); 
        /* X labels */
        // X-axis label
        makeText(categories[0], rightxaxis.x, rightxaxis.y + 70, 0);
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        strokeWeight(sectionWidth * 0.8);
        strokeCap(SQUARE);
        for (int i = 0; i < data.getRowCount(); i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = origin.y + 10;
            makeText(data.getRow(i).getString(categories[0]), x, y, -HALF_PI / 2);
        }
        /* Y labels */
        // Y-axis label
        textSize(16); 
        makeText(categories[1], topyaxis.x - 60, topyaxis.y + 50, -HALF_PI);

        // Y value labels
        textSize(12);
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int increment;
        try {
            increment = int(25/abs(ratio));
        } catch (Exception e) {
            increment = 30;
        }
        for (int i = 0; i <= maxY * 1.03; i+= increment) {
            makeText(Integer.toString(i), origin.x - 10, int(i * ratio + origin.y), 0);
        }
    }


};