public class RoseChart extends CircleChart {

    RoseChart(Table data, String[] categories) {
        super(data, categories);
    }


    void draw (float transition_completeness, Transition transition) {
        strokeWeight(1);

        float ratio = float(origin.y - topyaxis.y) / maxY;
        float superRatio = float(origin.y - topyaxis.y)  / superMaxY;
        float angle = 0;

        switch(transition) {
            case NONE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int diam = 0;
                    for (int j = 1; j < categories.length; j++) {
                        diam += int(data.getRow(i).getInt(categories[j]) * superRatio);
                    }
                    for (int j = categories.length - 1; j >= 1; j--) {
                        fill(colors[j - 1]);
                        arc(width/2 - 50, height/2, diam, diam, radians(angle), radians(angle+const_angle), PIE);
                        diam -= int(data.getRow(i).getInt(categories[j]) * superRatio);
                    }
                    angle += const_angle;
                }
                break;
            case ROSETOPIE:
            case PIETOROSE:
                if (transition_completeness < 0.5) {
                    for (int i = 0; i < angles.length; i++) {
                        float angle_increment = radians(const_angle);
                        fill(colors[0]);
                        float diam = serp(data.getRow(i).getInt(categories[1]) * ratio, data.getRow(i).getInt(categories[1]) * superRatio, transition_completeness * 2);
                        arc(width/2 - 50, height/2, diam, diam, angle, angle+angle_increment, PIE);
                        angle += angle_increment;
                    }
                } else {
                    for (int i = 0; i < data.getRowCount(); i++) {
                        int diam = 0;
                        for (int j = 1; j < categories.length; j++) {
                            diam += int(data.getRow(i).getInt(categories[j]) * superRatio);
                        }
                        for (int j = categories.length - 1; j >= 1; j--) {
                            fill(colors[j - 1]);
                            arc(width/2 - 50, height/2, diam, diam, radians(angle), radians(angle+const_angle), PIE);
                            diam -= int(data.getRow(i).getInt(categories[j]) * superRatio);
                        }
                        angle += const_angle;
                    }
                }

                break;
        }
    }
};
