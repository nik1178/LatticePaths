package remaster;

//import javax.swing.*;

import java.awt.*;
//import java.awt.event.*;
import java.util.*;
public class MyRunnable extends GridLabel {

    //multipliers for new small red line location. This is later multiplied by LINE_SIZE so we get the actual coordinates
    int x1=0;
    int x2=0;
    int y1=0;
    int y2=0;
    int counter = 0;

    volatile static int DELAY = 100;

    //boolean to see if startButtonStop() method should be called or not
    volatile boolean stopButtonBoolean = false;
    //variable to see if the user wants the algorithm to stop and with that return out of all it's methods
    volatile boolean shouldReturn = false;
    //When pressing "submit", we want the animation of the colors fading to instantly stop, because we're clearing the lines anyway
    volatile boolean breakAnimation = false;

    int colorChangeSpeed = 5;
    int howManyExtraLoopsAfterEnd = 255/colorChangeSpeed;
    volatile int howManyRedUpdates = 0;

    //Add values to string first, so that they can't change partway through, due to drawing and calculation thread running seperately
    String coordinates = "";
    @Override
    public void run() {
        lines.clear();
        //ut.println("here");
        singleThreaded();
        //lines.clear();
        repaint();
    }

    public void singleThreaded(){
        //constantly looping the algorithm for when it should be called. If the algorithm is stopped or it finishes it calls the startButtonStop() method so the button properly chagnes
        //if the algorithm stops it also resets all the coordinates back to 0 so it will start from the beginning when the user pressed "Start"
        /* while(!Thread.interrupted()){ */
            
            howManyTimesRepainted=0;
            howManyRedUpdates = 0;
            counter=0;
            x1=0;
            x2=0;
            y1=0;
            y2=0;
            breakAnimation=false;
            //howManyExtraLoopsAfterEnd = 255/colorChangeSpeed;
            algo(0, 0);
            shouldReturn=true;

            //Add a few more iterations to make sure all the lines turn green
            int starthowManyRedUpdates = this.howManyRedUpdates;
            for(int i=0; i<howManyExtraLoopsAfterEnd; i++){
                if(breakAnimation){
                    System.out.println("breakanimation");
                    break;
                }
                long timeEntered = java.lang.System.currentTimeMillis();
                while(i+starthowManyRedUpdates+1>howManyRedUpdates){
                    if(java.lang.System.currentTimeMillis() - timeEntered > 50){
                        howManyRedUpdates++;
                        System.out.println("reached");
                    }
                    if(breakAnimation) break;
                }
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Thread.currentThread().interrupt();
                    //System.out.println("Interrupeted MyRunnable " + java.lang.System.currentTimeMillis());
                    break;
                }
            }

            counter=0;
            if(stopButtonBoolean) MyFrame.startButtonStop();
            stopButtonBoolean=false;
            x1=0;
            x2=0;
            y1=0;
            y2=0;
            coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
            for(String x : lines.keySet()){
                lines.get(x).color = new Color(0,255,0);
            }
        /* } */
    }

    double LINE_SIZE = GridLabel.LINE_SIZE;
    public void algo(int vertical, int horizontal)
    {
        //System.out.println(shouldReturn);
        //System.out.println("here" + java.lang.System.currentTimeMillis());
        if(shouldReturn) return;
        coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
        repaint();
        while(counter>howManyTimesRepainted) if(shouldReturn) return;
        counter++;
        stopButtonBoolean=true;
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            Thread.currentThread().interrupt();
            //System.out.println("Interrupeted MyRunnable " + java.lang.System.currentTimeMillis() + "this one");
        }

        //algorithm from previous project. It goes right on the lines until it hits a wall after which it goes 1 step left and 1 step down
        if(horizontal<MyFrame.gridSize)
        {
            //sets the coordinates of the new red line that has to be drawn to it's location on the invisible grid it is traversing
            x1=horizontal;
            y1=y2;
            x2=horizontal+1;
            
            coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
            repaint();
            algo(vertical, horizontal+1);
            if(shouldReturn) return;
            x1=horizontal;
            y1=y2;
            x2=horizontal+1;
            
            coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
            repaint();
        }
        else{
            MyFrame.howManyPaths++;
        }
        if(vertical<MyFrame.gridSize && horizontal<MyFrame.gridSize)
        {
            x2=x1;
            y2=vertical+1;
            y1=vertical;

            coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
            repaint();
            algo(vertical+1, horizontal);
        }
    }

    //This is used to determine if the program is going too fast and that the paint thread couldn't keep up with the algorithm. Using this prevents graphical bugs... mostly
    volatile int howManyTimesRepainted = 0;
    //hash maps of the coordinates where the line has already been. These are used to redraw all the red lines
    static HashMap<String,Line> lines = new HashMap<>();
    /* static HashMap<String,Integer> hashx1 = new HashMap<>();
    static HashMap<String,Integer> hashx2 = new HashMap<>();
    static HashMap<String,Integer> hashy1 = new HashMap<>();
    static HashMap<String,Integer> hashy2 = new HashMap<>();
    static HashMap<String,Color> hashColor = new HashMap<>(); */
    @Override
    public synchronized void paint(Graphics g){

        LINE_SIZE = GridLabel.LINE_SIZE;
        super.paint(g);
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        for(String x : lines.keySet()){
            if(x.equals("rightLine")) continue;
            int newBrightness = lines.get(x).color.getGreen() + colorChangeSpeed;
            if(newBrightness>=255){
                newBrightness=255;
            }
            int red = lines.get(x).color.getRed() - newBrightness;
            if(red<0) red = 0;
            lines.get(x).color = new Color(red, newBrightness, 0);
            howManyRedUpdates++;
        }

        //draws all the red lines (the lines we have already visited) from the coordinates in the hasmaps
        g2D.setStroke(new BasicStroke(3));
        for(String i : lines.keySet()){
            g2D.setPaint(lines.get(i).color);
            /* int tempx1 = lines.get(i).x1;
            int tempy1 = lines.get(i).x2;
            int tempx2 = lines.get(i).y1;
            int tempy2 = lines.get(i).y2; */
            int[] coords = lines.get(i).getCoords();
            g2D.drawLine(coords[0], coords[1], coords[2], coords[3]);
        }
        if(coordinates.equals("")) return;

        String[] coordinatesSplit = this.coordinates.split("@");
        int beginningx1 = Integer.parseInt(coordinatesSplit[0]);
        int beginningy1 = Integer.parseInt(coordinatesSplit[1]);
        int beginningx2 = Integer.parseInt(coordinatesSplit[2]);
        int beginningy2 = Integer.parseInt(coordinatesSplit[3]);

        int tempx1 = (int)(beginningx1*LINE_SIZE);
        int tempy1 = (int)(beginningy1*LINE_SIZE);
        int tempx2 = (int)(beginningx2*LINE_SIZE);
        int tempy2 = (int)(beginningy2*LINE_SIZE);
        Color tempColor = new Color(255,0,0);

        //if the coordinates are already in the hasmaps they are skipped, otherwise they are saved into the hasmaps so they can be drawn later
        //we don't save all the coordinates so that we don't have to draw the same line multiple times for no reason
        if(!(lines.containsKey(coordinates))){
            lines.put(coordinates, new Line(tempx1, tempy1, tempx2, tempy2));
            /* hashx1.put(tempx1+"{"+tempx2+"{"+tempy1+"{"+tempy2,tempx1);
            hashx2.put(tempx1+"{"+tempx2+"{"+tempy1+"{"+tempy2,tempx2);
            hashy1.put(tempx1+"{"+tempx2+"{"+tempy1+"{"+tempy2,tempy1);
            hashy2.put(tempx1+"{"+tempx2+"{"+tempy1+"{"+tempy2,tempy2); */
        }
        //We do save every color tho, because we want them to be refreshsed
        lines.get(coordinates).color = tempColor;

        if(counter==MyFrame.gridSize+1) {//g2D.drawLine(500, 0, 500, 500);
            lines.put("rightLine", new Line(500, 0, 500, 500, tempColor));
            /* hashx1.put("rightLine",500);
            hashx2.put("rightLine",500);
            hashy1.put("rightLine",0);
            hashy2.put("rightLine",500);
            hashColor.put("rightLine",tempColor); */
        }        
        
        //g2D.drawLine(x1, y1, x2, y2);



        //draws the cyan line aka. the current line from the current coordinates
        /* tempx1 = (int)(beginningx1*LINE_SIZE);
        tempy1 = (int)(beginningy1*LINE_SIZE);
        tempx2 = (int)(beginningx2*LINE_SIZE);
        tempy2 = (int)(beginningy2*LINE_SIZE); */
        g2D.setPaint(Color.cyan);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawLine(tempx1, tempy1, tempx2, tempy2);
        
        //System.out.println("hm " +x1 + " " + x2);
        coordinates = "";
        this.howManyTimesRepainted++;
    }
}
class Line {
    int x1;
    int x2;
    int y1;
    int y2;
    Color color;

    Line(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    Line(int x1, int y1, int x2, int y2, Color color){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
    }

    int[] getCoords(){
        int[] coords = {this.x1, this.y1, this.x2, this.y2};
        return coords;
    }
}