public class Piechart extends CircleChart {

    Piechart(Table data, String[] categories) {
        super(data, categories);
    }

    void draw (float transition_completeness, Transition transition) {
        strokeWeight(1);
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);

        float angle = 0;
        float ratio = float(origin.y - topyaxis.y) / maxY;
        float superRatio = float(origin.y - topyaxis.y) / superMaxY;
        int sectionWidth = abs((rightxaxis.x - origin.x) / data.getRowCount());
        int default_diam = (min(height - margins[1], width - margins[3] - margins[0]));

        switch (transition) {
        case NONE:
            for (int i = 0; i < angles.length; i++) {
                fill(colors[i]);
                arc(width / 2 - 50, height / 2, default_diam, default_diam, angle, angle + radians(angles[i]), PIE);
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
                    arcx = int(serp(x, width / 2 - 50, (transition_completeness - 0.5) * 2));
                    arcy = int(serp(origin.y, height / 2, (transition_completeness - 0.5) * 2));
                } else {
                    arcx = x;
                    arcy = origin.y;
                }

                if (transition_completeness < 0.5) {
                    diam = int(serp(origin.y - y, default_diam / 2, transition_completeness * 2)) * 2;
                } else {
                    diam = default_diam;
                }

                if (transition_completeness < 0.5) {
                    startr = serp(3 * HALF_PI - 0.01, 3 * HALF_PI - radians(angles[i]) / 2, transition_completeness * 2);
                    endr = serp(3 * HALF_PI + 0.01, 3 * HALF_PI + radians(angles[i]) / 2, transition_completeness * 2);
                } else {
                    startr = serp(3 * HALF_PI - radians(angles[i]) / 2, angle, (transition_completeness - 0.5) * 2);
                    endr = serp(3 * HALF_PI + radians(angles[i]) / 2, angle + radians(angles[i]), (transition_completeness - 0.5) * 2);
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
                    arcx = int(serp(x, width / 2 - 50, (transition_completeness - 0.5) * 2));
                    arcy = int(serp(y, height / 2, (transition_completeness - 0.5) * 2));
                } else {
                    arcx = x;
                    arcy = y;
                }

                // d = sqrt(A/pi) * 2;

                diam = default_diam;
                float midway_diam = default_diam * sqrt(((float(yval) / maxY)) / PI);
                if (transition_completeness < 0.5) {
                    diam = int(serp(12, midway_diam, transition_completeness * 2));
                } else {
                    diam = int(serp(midway_diam, default_diam, (transition_completeness - 0.5) * 2.0));
                }

                if (transition_completeness < 0.5) {
                    startr = serp(angle - PI, angle, transition_completeness * 2);
                    endr = serp(angle + radians(angles[i]) + PI, angle + radians(angles[i]), transition_completeness * 2);
                } else {
                    startr = angle;
                    endr = angle + radians(angles[i]);
                }

                arc(arcx, arcy, diam, diam, startr, endr, PIE);
                angle += radians(angles[i]);
            }
            break;
        case PIETOROSE:
        case ROSETOPIE:
            for (int i = 0; i < angles.length; i++) {
                float angle_increment = serp(radians(const_angle), radians(angles[i]), transition_completeness);
                fill(lerpColor(colors[0], colors[i], transition_completeness));
                float diam = serp(data.getRow(i).getInt(categories[1]) * ratio, default_diam, transition_completeness);
                arc(width / 2 - 50, height / 2, diam, diam, angle, angle + angle_increment, PIE);
                angle += angle_increment;
            }
            break;
        }
    }
};
