function Chart(p, data, originx, originy, topyaxisx, topyaxisy, rightxaxisx, rightxaxisy) {
    var linechart;
    //var origin, topyaxis, rightxaxis;
    //var minY, maxY;
    var i = 0;
    /*datapoints = new FruitCoun[data.getRowCount()];       
    for (TableRow row : data.rows()) {
        datapoints[i++] = new FruitCount(row.getString("Name"), parseInt(row.getString("Number")));
    }*/
    /* initialize */
    linechart = new Linechart(p, data, originx, originy, topyaxisx, topyaxisy, rightxaxisx, rightxaxisy);
    /*var minY = data[0][1];
    var maxY = data[0][1];
    for (var i = 1; i < data.length; i++) {
        if (data[i][1] < minY) minY = data[i][1];
        if (data[i][1] > maxY) maxY = data[i][1];
    }*/
    var minY = 5;
    var maxY = 10;

    this.drawLabels = function(){
        p.fill(0);
        p.textSize(16); 
        p.textAlign(p.RIGHT, p.CENTER); 
        /* X labels */
        // X-axis label
        this.makeText("Episode", rightxaxisx, rightxaxisy + 70, false);
        // X value labels
        p.textSize(12); 
        var sectionWidth = Math.abs(((rightxaxisx - originx) / data.length));
        p.strokeWeight(sectionWidth * 0.8);
        p.strokeCap(p.SQUARE);
        for (var i = 0; i < data.length; i++) {
            var x = originx + sectionWidth * i + sectionWidth / 2 + (sectionWidth * 0.1);
            var y = originy + 10;
            this.makeText(data[i][0], x, y, true);
        }
        /* Y labels */
        // Y-axis label
        p.textSize(16); 
        this.makeText("Rating", topyaxisx - 60, topyaxisy + 50, true);

        // Y value labels
        p.textSize(12);
        var ratio = ((topyaxisy - originy)) / maxY;
        var increment;
        try {
            increment = (25/Math.abs(ratio));
        } catch (err) {
            increment = 30;
        }
        for (var i = 0; i <= maxY; i+= increment) {
            this.makeText(i, originx - 10, i * ratio + originy, false);
        }
    }

    this.makeText = function(str, x, y, vert) {      
        if (vert) {
            p.pushMatrix();
            p.translate(x, y);
            p.rotate(-p.HALF_PI);
            p.translate(-x, -y);
            p.text(str, x, y);
            p.popMatrix();
        } else {
            p.text(str, x, y);
        }
    }

    this.draw = function() {
        this.drawAxes();
        this.drawLabels();
        linechart.draw();
        //this.drawLabels();
          //  linechart.setData(datapoints, origin, topyaxis, rightxaxis);
            //linechart.draw();
    }

    this.drawAxes = function() {
        p.strokeWeight(2);
        p.line(originx, originy, topyaxisx, topyaxisy);
        p.line(originx, originy, rightxaxisx, rightxaxisy);
    }
};

function Linechart(p, data, originx, originy, topyaxisx, topyaxisy, rightxaxisx, rightaxisy) {
    var minY;
    var maxY;
    var origin, topyaxis, rightxaxis;
    var radius = 2;
    /*int dataHovered;
    this.data = data;
    this.originx = originx;
    this.originy = originy;
    this.topyaxisx = topyaxisx;
    this.topyaxisy = topyaxisy;
    this.rightxaxisx = rightxaxisx;
    this.rightxaxisy = rightxaxisy;*/
    /*minY = data[0][1];
    maxY = data[0][1];
    for (var i = 1; i < data.length; i++) {
        if (data[i][1] < minY) minY = data[i][1];
        if (data[i][1] > maxY) maxY = data[i][1];
    }*/
    minY = 5;
    maxY = 10;

    this.draw = function() {
        p.strokeWeight(2);
        var ratio = (topyaxisy - originy) / maxY;
        var sectionWidth = Math.abs(((rightxaxisx - originx) / data.length));
        var prevx = (originx + sectionWidth / 2) + (sectionWidth * 0.1);
        var prevy = (data[0][1] * ratio) + originy;
        p.ellipse(prevx, prevy, radius, radius);
        for (var i = 1; i < data.length; i++) {
            var x = originx + sectionWidth * i + sectionWidth / 2 + (sectionWidth * 0.1);
            var y = (data[i][1] * ratio) + originy;
            p.line(prevx, prevy, x, y);
            prevx = x;
            prevy = y;
            p.ellipse(prevx, prevy, radius, radius);
        }
    }
};
