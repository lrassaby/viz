public class AxisChart {
    protected Data data;
    protected String[] categories;
    protected float maxY; // for single columned 
    protected float superMaxY; // for multi columned
    public int[] margins = new int[4];
    protected Point origin, topyaxis, rightxaxis;

    AxisChart (Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        this.data = data;
        this.categories = categories;
        maxY = NUM;
        superMaxY = NUM;
        margins[0] = 10 + int(chartLeftX);
        margins[1] = int(chartLeftY) + 20; 
        margins[2] = width - int(chartSize) - int(chartLeftX) + 10;
        margins[3] = height - int(chartSize) - int(chartLeftY) + 10;
        /*for (TableRow row : data.rows()) {
            int elemweight = row.getInt(categories[1]);
            if (elemweight > maxY) {
                maxY = elemweight;
            }
            int rowweight = 0;
            for (int i = 1; i < categories.length; i++) {
                rowweight += row.getInt(categories[i]);
            }
            if (rowweight > superMaxY) {
                superMaxY = rowweight;
            }
        }*/
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]); 
        rightxaxis = new Point(width - margins[2], height - margins[3]);
    }

 
    void drawAxes(color c) {
        strokeWeight(2);
        stroke(c);
        fill(c);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y - 15);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }

    protected void drawLabels(color c, float ratio) {
        stroke(c);
        fill(c);
        textSize(16); 
        textAlign(RIGHT, CENTER); 
        /* X labels */
        // X-axis label
        /*makeText(categories[0], rightxaxis.x, rightxaxis.y + 70, 0);*/
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / NUM));
        strokeWeight(sectionWidth * 0.8);
        strokeCap(SQUARE);
        /*for (int i = 0; i < data.getRowCount(); i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = origin.y + 10;
            makeText(data.getRow(i).getString(categories[0]), x, y, -HALF_PI / 2);
        }*/
        /* Y labels */
        // Y-axis label
        textSize(16); 
        //makeText(categories[1], topyaxis.x - 60, topyaxis.y + 50, -HALF_PI);
       
        // Y value labels
        if (ratio < 100) {
            textSize(12);
        
            int increment;
            try {
                increment = int(25/ratio);
            } catch (Exception e) {
                increment = 30;
            }
            if (increment < 1) {
                increment = 1;
            }

            float max = float(origin.y - topyaxis.y) / ratio;
            for (int i = 0; i <= max * 1.03; i+= increment) {
                //makeText(Integer.toString(i), origin.x - 10, int(-i * ratio + origin.y), 0);
            }
        } 
    }
};
