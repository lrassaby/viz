function Linechart (p, originx, originy, data, topyaxisx, topyaxisy, rightxaxisx, rightxaxisy) {
    minY = data[0].count;
    maxY = data[0].count;
    for (var i = 1; i < data.length; i++) {
        if (data[i].count < minY) minY = data[i].count;
        if (data[i].count > maxY) maxY = data[i].count;
    }
    this.originx = originx;
    this.originy = originy;
    this.topyaxisx = topyaxisx;
    this.topyaxisy = topyaxisy;
    this.rightxaxisx = rightxaxisx;
    this.rightxaxisy = rightxaxisy;
    this.isect = false;
    this.radius = 1;
    this.dataHovered = 0;
    this.data = data;
    this.numData = data.length;

    //find min/max?

    this.intersect = function(mousex, mousey) {
        var ratio = (this.topyaxisy - this.origin.y) / this.maxY;
        var sectionWidth = abs(((this.rightxaxisx - this.origin.x) / this.data.length));
        strokeWeight(sectionWidth * 0.8);
        
        for (var i = 0; i < this.numData; i++) {
            var x = this.origin.x + sectionWidth * i + sectionWidth / 2 + (sectionWidth * 0.1);
            var y = (this.data[i].count * ratio) + this.origin.y + 4;
            
            if ((mousex - x) * (mousex - x) + (mousey - y) * (mousey - y) < (radius + 1) * (radius + 1)) {
                this.dataHovered = i;
                return "(" + this.data[i].fruit + ", " + this.data[i].count + ")";
            }
        }
        return "";
    }

    this.draw = function() {
        p.strokeWeight(2);
        var ratio = (this.topyaxisy - this.originy) / this.maxY;
        var sectionWidth = Math.abs(((this.rightxaxisx - this.originx) / this.numData));
        var prevx =  (this.originx + sectionWidth) / 2 + (sectionWidth * 0.1);
        var prevy =  (this.data[0].count * ratio) + this.originy;
        this.drawCircle(prevx, prevy, 10);
        for (var i = 1; i < this.numData; i++) {
            var x = this.originx + sectionWidth * i + sectionWidth / 2 + sectionWidth * 0.1;
            var y = data[i].count * ratio + this.originy;
            p.line(prevx, prevy, x, y);
            prevx = x;
            prevy = y;
            this.drawCircle(prevx, prevy, this.radius * 2);
        }
    }
 
    this.drawCircle = function(x, y, diameter) {
        p.ellipse(x, y, diameter, diameter);
    }

    this.highlightOnHover = function() {
        var ratio = (this.topyaxisy - this.originy) / this.maxY;
        var sectionWidth = abs(((this.rightxaxisx - this.originx) / this.numData));
        var prevx = origin.x + sectionWidth / 2;
        var prevy =  (this.data[0].count * ratio) + this.originy;
        var x = this.originx + sectionWidth * this.dataHovered + sectionWidth / 2 + (sectionWidth * 0.1);
        var y = (this.data[dataHovered].count * ratio) + this.originy;
            
        prevx = x;
        prevy = y;
        fill(255, 255, 0);
        stroke(255, 255, 0);
        drawCircle(prevx, prevy, this.radius * 2);
        stroke(0);
        fill(0);
    }
};
