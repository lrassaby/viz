
/**
 * @author fyang@cs.tufts.edu
 * @since 2014-Aug-19
 * This is the entry of your color scheme
 */

 int margin = 20, marginLeft = 20, marginRight = 20, marginTop = 80, marginBottom = 20;

/**
 * the number of colors in your color scheme
 */
int numberOfColor = 8;

/**
 * the display size of your matrix
 */
int matrixWidth = 70, matrixHeight = 60;

int iWidth = numberOfColor * matrixWidth + marginLeft+ marginRight ;
int iHeight = numberOfColor * matrixHeight + marginTop + marginBottom;

/**
 * an importnt constant in helping detect unexcepted cases
 */
final int BADRETRUN = MIN_INT;

/**
 * change these two colors for needs
 */
color textInTop = color(0);
color textInDiag = color(0);

/**
 * your color scheme
 */
ColorScheme cs = null;

void setup(){
    size(iWidth, iHeight);
    textAlign(LEFT);
    textSize(11);
    frame.setTitle("COMP150VIZ, Lab4 ColorScheme");
    example();
}

void example(){
    // how to initialize a color scheme   
    Color[] colorsrr = new Color[numberOfColor];
    colorsrr[0] = new Color("RGB", 251,135,173); //70, 48, 0
    colorsrr[1] = new Color("RGB", 111,54,39);   //30, 25, 15
    colorsrr[2] = new Color("RGB", 117,124,36);  //50, -15, 45
    colorsrr[3] = new Color("RGB", 50,57,80);    //35, -10, 0
    colorsrr[4] = new Color("RGB", 4,163,143);   //50, -40, 10
    colorsrr[5] = new Color("RGB", 114,9,83);    //25, 48, 0
    colorsrr[6] = new Color("RGB", 20,45,138);    //30, 0, -20
    colorsrr[7] = new Color("RGB", 2,60,20);
   
    cs = new ColorScheme(colorsrr, "RGB");
    
    // print the distance matrix in current color space
    cs.printDistance();
    
    // convert the current color scheme to the other color space
    cs.toSpace("CIELAB");
    // print the string representation for a single color
    println(cs.colors[0].toString());
    println();
    // print the distance matrix
    cs.printDistance();
    println();
    
    // how to get a color in color scheme
    Color c = cs.getColor(0);
    // or
    c = cs.colors[0]; 
    println("1. " + c.toString());
  
    // how to get a channel value of a color
    println("2. " + cs.getColorChannel(0, 0));
    // or
    println("2. " + cs.colors[0].channels[0]);
    println();

    // how to get the distance between two colors in current color space  
    println("3. " + cs.getDistance(0, 1));
    // or
    println("3. " + cs.getColor(0).distance(cs.getColor(1)));
    println();
          
    // how to increase/decrease the value in a color channel
    boolean flag = cs.getColor(0).increaseChannel(0, 1); 
    println("4. " + flag + " in changing the channel"); // if flag == false, fail in increasing the value
    // do not recommend, you have to check the bound yourself
    cs.colors[0].channels[0].value += 1; 
    // or 
    cs.getColor(0).channels[0].value += -1;
    println();
    
    // convert the whole color scheme to another color space
    cs.toSpace("RGB");
    cs.printColors();
    println();

    // convert the whole color scheme to another color space
    cs.toSpace("CIELAB");
    cs.printColors();
    println();

    // draw on the screen
    cs.display();
}

