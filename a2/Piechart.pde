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
        strokeWeight(1);
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);

        float angle = 0;
        float ratio = float(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs((rightxaxis.x - origin.x) / data.getRowCount());

        switch(transition) {
            case NONE:
                for (int i = 0; i < angles.length; i++) {
                    fill(colors[i]);
                    arc(width/2 - 50, height/2, (min(height, width - 120) - 40), (min(height, width - 120) - 40), angle, angle+radians(angles[i]), PIE);
                    angle += radians(angles[i]);
                }
                break;
            case BARTOPIE:
            case PIETOBAR:
                for (int i = 0; i < angles.length; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    if (transition_completeness < 0.5) {
                        fill(lerpColor(color(0, 0, 0), colors[i], transition_completeness * 2));
                    } else {
                        fill(colors[i]);
                    }
                    int arcx, arcy, diam;
                    float startr, endr;
                    if (transition_completeness > 0.5) {
                        arcx = int(lerp(x, width/2 - 50, (transition_completeness - 0.5) * 2));
                        arcy = int(lerp(origin.y, height/2, (transition_completeness - 0.5) * 2));
                    } else {
                        arcx = x;
                        arcy = origin.y;
                    }
                   
                    if (transition_completeness < 0.5) {
                        diam = int(lerp(origin.y - y, (min(height, width - 120) - 40) / 2, transition_completeness * 2)) * 2;
                    } else {
                        diam = (min(height, width - 120) - 40);
                    }

                    if (transition_completeness < 0.5) {
                        startr = lerp(3 * HALF_PI - 0.01, 3 * HALF_PI - radians(angles[i]) / 2, transition_completeness * 2);
                        endr = lerp(3 * HALF_PI + 0.01, 3 * HALF_PI + radians(angles[i]) / 2, transition_completeness * 2);
                    } else {
                        startr = lerp(3 * HALF_PI - radians(angles[i]) / 2, angle, (transition_completeness - 0.5) * 2);
                        endr = lerp(3 * HALF_PI + radians(angles[i]) / 2, angle+radians(angles[i]), (transition_completeness - 0.5) * 2);
                    }
                    arc(arcx, arcy, diam, diam, startr, endr, PIE);
                    angle += radians(angles[i]);
                }
                break;
            case LINETOPIE:
            case PIETOLINE:
                for (int i = 0; i < angles.length; i++) {
                    int yval = data.getRow(i).getInt(categories[1]);
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(yval * ratio);
                    if (transition_completeness < 0.5) {
                        fill(lerpColor(color(0, 0, 0), colors[i], transition_completeness * 2));
                    } else {
                        fill(colors[i]);
                    }
                    int arcx, arcy, diam;
                    float startr, endr;
                    if (transition_completeness > 0.5) {
                        arcx = int(lerp(x, width/2 - 50, (transition_completeness - 0.5) * 2));
                        arcy = int(lerp(y, height/2, (transition_completeness - 0.5) * 2));
                    } else {
                        arcx = x;
                        arcy = y;
                    }
                   
                    // d = sqrt(A/pi) * 2;
                   
                    float default_diam = (min(height, width - 120) - 40);
                    float midway_diam = default_diam * sqrt(((float(yval)/maxY))/ PI);
                    if (transition_completeness < 0.5) {
                        diam = int(lerp(12, midway_diam, transition_completeness * 2));
                    } else {
                        diam = int(lerp(midway_diam, default_diam, (transition_completeness - 0.5) * 2.0));
                    }

                    if (transition_completeness < 0.5) {
                        startr = lerp(angle - PI, angle, transition_completeness * 2);
                        endr = lerp(angle+radians(angles[i]) + PI, angle+radians(angles[i]), transition_completeness * 2);
                    } else {
                        startr = angle;
                        endr = angle+radians(angles[i]);
                    }

                    arc(arcx, arcy, diam, diam, startr, endr, PIE);
                    angle += radians(angles[i]);
                }
                break;
        }
    }
};
