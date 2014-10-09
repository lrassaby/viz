class Data {
    class DataPoint {
        private float value = -1;
        private boolean marked = false;

        DataPoint(float f, boolean m) {
            this.value = f;
            this.marked = m;
        }

        boolean isMarked() {
            return marked;
        }

        void setMark(boolean b) {
            this.marked = b;
        }

        float getValue() {
            return this.value;
        }
    }

    private DataPoint[] data = null;
    private final int MARKED1 = 4;
    private final int MARKED2 = 9;

    Data() {
        // NUM is a global varibale in support.pde
        data = new DataPoint[NUM];

        for (int i = 0; i < NUM; i++) {
            data[i].value = random(0, 100.0);
            if (i == MARKED1 || i == MARKED2)
                data[i].marked = true;
            else
                data[i].marked = false;
        }
    }

    float getValueAt(int i) {
        return data[i].value;
    }

    boolean getMarkAt(int i) {
        return data[i].isMarked();
    }

}
