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

    public DataPoint[] data = null;

    Data(int marked1, int marked2) {
        // NUM is a global varibale in support.pde
        data = new DataPoint[NUM];

        for (int i = 0; i < NUM; i++) {
            data[i] = new DataPoint(random(2.0, 10.0), false);
            if (i == marked1 || i == marked2) {
                data[i].setMark(true);
            }
        }
    }

    float getValue(int i) {
        return data[i].value;
    }

    boolean getMark(int i) {
        return data[i].isMarked();
    }

}
