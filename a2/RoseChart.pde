public class RoseChart {
    private Table data;
    private String[] categories;
    private color[] colors;
    private ColorGenerator colorgenerator;
    private int[] margins = {100, 150, 220, 100};
    private Point origin, topyaxis, rightxaxis;
    private float maxY;
    private float ratio;
    private float angle;
    private int superMaxY = 0;
    private String[] Radii;
    private int total_magnitude = 0;

    RoseChart(Table data, String[] categories) {
        setData(data, categories);
    }

    void setData(Table data, String[] categories) {
        colorgenerator = new ColorGenerator();
        this.data = data;
        this.categories = categories;
        for (int i = 0; i < data.getRowCount(); i++) {
        	total_magnitude += data.getRow(i).getInt(categories[1]);
        }
     
        angle = (360 / (float)(data.getRowCount())); 

        colors = new color[data.getRowCount()];
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

        for (TableRow row : data.rows()) {
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
        }

        float temp = height/2  - 40; 
        ratio = (float(superMaxY) / temp);
    }

    void draw (float transition_completeness, Transition transition) {
        strokeWeight(1);

        float ratio = float(origin.y - topyaxis.y) / superMaxY;
        int sectionWidth = abs((rightxaxis.x - origin.x) / data.getRowCount());
        float start_angle = 0;

        switch(transition) {
            case NONE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int diam = 0;
                    for (int j = 1; j < categories.length; j++) {
                        diam += int(data.getRow(i).getInt(categories[j]) * ratio);
                    }
                    for (int j = 1; j < categories.length; j++) {
                        fill(colors[j - 1]);
                        arc(width/2 - 50, height/2, diam, diam, radians(start_angle), radians(start_angle+angle), PIE);
                        diam -= int(data.getRow(i).getInt(categories[j]) * ratio);
                    }
                    start_angle += angle;
                }
            // case ROSETOPIE:
            // case PIETOROSE:
            //     if (transition_completeness < 0.25) {
            //         int currRadius = height/2 - 40;
            //         int start = 0;

            //         for (int i = 0; i < data.getRowCount(); i++) {
            //             for (int j = 1; j < categories.length; j++) {
            //                 currRadius -= int(data.getRow(i).getInt(categories[j]) * ratio);
            //                 fill(colors[j - 1]);
            //                 float end_angle = serp(start, start+ ((float(data.getRow(i).getInt(categories[1])) / total_magnitude) * 360), transition_completeness * 4);
            //                 arc(width/2 - 50, height/2, currRadius, currRadius, radians(start), radians(start+end_angle), PIE);
            //             }
            //         }
            //     } else {
            //         float currRadius = height/2 - 40;
            //         float start = 0;
            //         float end_angle;
            //         for (int i = 0; i < data.getRowCount(); i++) {
            //             end_angle = (float(data.getRow(i).getInt(categories[1])) / total_magnitude) * 360;
            //             for (int j = 1; j < categories.length; j++) {
            //                 int finallength = int(data.getRow(i).getInt(categories[j]) * ratio);
            //                 stroke(colors[j - 1]);
            //                 if (j > 1) {
            //                     int newlength = int(serp(radius, radius - finallength, (transition_completeness - 0.25) * 4.0/3.0));
            //                     currRadius -= finallength;
            //                 } else {
            //                     arc(width/2 - 50, height/2, newlength, newlength, radians(start), radians(start+end_angle), PIE);
            //                     currRadius -= finallength;
            //                     start += end_angle;
            //                 }
            //             }
            //         }
                // }
            break;
        }
    }
};
