public class RoseChart extends CircleChart {

    RoseChart(Table data, String[] categories) {
        super(data, categories);
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
                    for (int j = categories.length - 1; j >= 1; j--) {
                        fill(colors[j - 1]);
                        arc(width/2 - 50, height/2, diam, diam, radians(start_angle), radians(start_angle+angle), PIE);
                        diam -= int(data.getRow(i).getInt(categories[j]) * ratio);
                    }
                    start_angle += angle;
                }
                break;
            case ROSETOPIE:
            case PIETOROSE:
                break;
        }
    }
};
