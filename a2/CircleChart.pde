public class CircleChart {
    protected Table data;
    protected String[] categories;
    protected float[] angles;
    protected color[] colors;
    protected ColorGenerator colorgenerator;
    protected int[] margins = {100, 150, 220, 100};
    protected Point origin, topyaxis, rightxaxis;
    protected float maxY;
    protected float ratio;
    protected float const_angle;
    protected int superMaxY = 0;
    protected int total_magnitude = 0;

    CircleChart (Table data, String[] categories) {
        setData(data, categories);
    }

    void setData(Table data, String[] categories) {
        colorgenerator = new ColorGenerator();
        this.data = data;
        this.categories = categories;
        for (int i = 0; i < data.getRowCount(); i++) {
            total_magnitude += data.getRow(i).getInt(categories[1]);
        }
     
        const_angle = (360 / (float)(data.getRowCount())); 
        int colorcount = max(data.getRowCount(), categories.length);
        colors = new color[colorcount];
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

        this.angles = new float[data.getRowCount()];
        for (int i = 0; i < data.getRowCount(); i++) {
            angles[i] = (float(data.getRow(i).getInt(categories[1])) / total_magnitude) * 360;
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
};