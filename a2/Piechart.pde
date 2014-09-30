public class Piechart {
    Table data;
    float[] angles;
    color[] colors;
    String[] categories;
    ColorGenerator colorgenerator;

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
    }

    void draw (float transition_completeness, Transition transition) {
        float angle = 0;
        for (int i = 0; i < angles.length; i++) {
            fill(colors[i]);
            arc(width/2 - 50, height/2, (min(height, width - 120)) - 40, (min (height, width - 120)) - 40, angle, angle+radians(angles[i]));
            angle += radians(angles[i]);
        }
    }
};
