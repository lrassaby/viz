public class Canvas {

  float x, y, w, h, size;
  float short_side;
  Canvas(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.size = w * h;
    if (w < h) {short_side = w;}
    else {short_side = h;}
  }
  
  
}

