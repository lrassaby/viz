class ButtonGroup {
    private Button[] buttons;
    private String selection;

    ButtonGroup (String[] chart_texts) {
        selection = chart_texts[0];
        buttons = new Button[chart_texts.length];
        Dimensions buttondim = new Dimensions(90, (height - 110) / buttons.length);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(new Point(), buttondim, 7, color(255, 153, 51), chart_texts[i]);
        }
    }

    public void draw() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].dim.setWH(90, (height - 110) / buttons.length);
            buttons[i].pos.setXY(width - buttons[0].dim.w - 20, 30 + i * (buttons[i].dim.h + 10));
            if (buttons[i].getText() == selection) {
                buttons[i].setColor(40, 190, 100);
            } else {
                buttons[i].setColor(50, 200, 160);
            }
            buttons[i].draw();
        }
    }
    public String getSelection () {
        return selection;
    }

    public void setSelection (String selection) {
        this.selection = selection;
    }

    public String getClicked() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].intersect(mouseX, mouseY);
            boolean selected = buttons[i].getIsect();
            if (selected) {
                buttons[i].setSelected(false);
                return buttons[i].getText();
            }
        }
        return "";
    }
};