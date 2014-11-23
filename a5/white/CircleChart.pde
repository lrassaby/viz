public class CircleChart {
    protected Data data;
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
    protected float total_magnitude = 0;

    CircleChart (Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        setData(data, categories, chartLeftX, chartLeftY, chartSize);
    }

    void setData(Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        colorgenerator = new ColorGenerator();
        this.data = data;
        this.categories = categories;
        maxY = data.getValue(0);
        for (int i = 0; i < NUM; i++) {
            total_magnitude += data.getValue(i);
            if (data.getValue(i) > maxY) {
              maxY = data.getValue(i);
            }
        }

        const_angle = (360 / (float)(NUM));
        int colorcount = NUM;
        colors = new color[colorcount];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorgenerator.generate();
        }
        margins[0] = 10 + int(chartLeftX);
        margins[1] = int(chartLeftY) + 20; 
        margins[2] = width - int(chartSize) - int(chartLeftX) + 10;
        margins[3] = height - int(chartSize) - int(chartLeftY) + 10;
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]);
        rightxaxis = new Point(width - margins[2], height - margins[3]);

        
        /*for (TableRow row : data.rows()) {
            int rowweight = row.getInt(categories[1]);
            if (rowweight > maxY) {
                maxY = rowweight;
            }
        }*/

        this.angles = new float[NUM];
        for (int i = 0; i < NUM; i++) {
            angles[i] = ((data.getValue(i) / total_magnitude) * 360.0);
        }

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

        float temp = height / 2  - 40;
        ratio = ((maxY) / temp);
    }
};
