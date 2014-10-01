public class Piechart {
    private Table data;
    private String[] categories;
    private float[] angles;
    private color[] colors;
    private ColorGenerator colorgenerator;
    private int[] margins = {80, 30, 120, 100};
    private Point origin, topyaxis, rightxaxis;
    private float maxY;

    Piechart(Table data, String[] categories) {
        setData(data, categories);
    }

    void setData(Table data, String[] categories) {
        colorgenerator = new ColorGenerator();
        this.data = data;
        this.categories = categories;
        this.angles = new float[data.getRowCount()];
        int total_magnitude = 0;
        for (int i = 0; i < data.getRowCount(); i++) {
        	total_magnitude += data.getRow(i).getInt(categories[1]);
        }
        for (int i = 0; i < data.getRowCount(); i++) {
        	angles[i] = (float(data.getRow(i).getInt(categories[1])) / total_magnitude) * 360;
        }

        colors = new color[angles.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorgenerator.generate();
        }
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]); 
        rightxaxis = new Point(width - margins[2], height - margins[3]);

        maxY = data.getRow(0).getInt(categories[1]);
        for (TableRow row : data.rows()) {
            int rowweight = row.getInt(categories[1]);
            if (rowweight > maxY) {
                maxY = rowweight;
            }
        }
    }

    void draw (float transition_completeness, Transition transition) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);

        float angle = 0;
        float ratio = float(topyaxis.y - origin.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));

        switch(transition) {
            case NONE:
                for (int i = 0; i < angles.length; i++) {
                    fill(colors[i]);
                    arc(width/2 - 50, height/2, (min(height, width - 120)) - 40, (min (height, width - 120)) - 40, angle, angle+radians(angles[i]), PIE);
                    angle += radians(angles[i]);
                }
                break;
            case BARTOPIE:
            case PIETOBAR:
                for (int i = 0; i < angles.length; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = int(data.getRow(i).getInt(categories[1]) * ratio) + origin.y;
                    fill(colors[i]);

                    int arcx = int(lerp(x, width/2 - 50, transition_completeness));
                    int arcy = int(lerp(origin.y, height/2, transition_completeness));
                    int diam = int(lerp(origin.y - y, min(height, width - 120) - 40, transition_completeness));
                    float startr = lerp(3 * HALF_PI, angle, transition_completeness);
                    float endr = lerp(3 * HALF_PI, angle+radians(angles[i]), transition_completeness);

                    arc(arcx, arcy, diam, diam, startr, endr, PIE);
                    angle += radians(angles[i]);
                }
                break;
        }
    }
};
