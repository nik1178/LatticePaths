package aljazVersion;

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

    //counter to see if startButtonStop() method should be called or not
    volatile int stopButtonCounter = 0;
    //boolean that does nothing other than prevent the while loop from going into garbage collection
    volatile boolean fakeBoolean = true;
    //variable to see if the user wants the algorithm to stop and with that return out of all it's methods
    volatile boolean shouldReturn = false;

    int howManyExtraLoopsAfterEnd = 255;

    //Add values to string first, so that they can't change partway through, due to drawing and calculation thread running seperately
    String coordinates = "0@0@0@0";
    @Override
    public void run() {
        //constantly looping the algorithm for when it should be called. If the algorithm is stopped or it finishes it calls the startButtonStop() method so the button properly chagnes
        //if the algorithm stops it also resets all the coordinates back to 0 so it will start from the beginning when the user pressed "Start"
        /* while(!Thread.interrupted()){ */
            
            howManyTimesRepainted=0;
            algo(0, 0);
            shouldReturn=true;

            //Add a few more iterations to make sure all the lines turn green
            int startHowmanytimesRepainted = this.howManyTimesRepainted;
            for(int i=0; i<howManyExtraLoopsAfterEnd; i++){
                while(i+startHowmanytimesRepainted+1>howManyTimesRepainted);
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Thread.currentThread().interrupt();
                }
            }

            counter=0;
            if(stopButtonCounter==1) MyFrame.startButtonStop();
            stopButtonCounter=0;
            x1=0;
            x2=0;
            y1=0;
            y2=0;
            coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
            
        /* } */
    }

    double LINE_SIZE = GridLabel.LINE_SIZE;
    public void algo(int vertical, int horizontal)
    {
        //System.out.println(shouldReturn);
        if(shouldReturn) return;
        
        coordinates = x1+"@"+y1+"@"+x2+"@"+y2;
        repaint();
        while(counter>howManyTimesRepainted);
        counter++;
        stopButtonCounter=1;
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            Thread.currentThread().interrupt();
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

    int colorChangeSpeed = 5;

    //This is used to determine if the program is going too fast and that the paint thread couldn't keep up with the algorithm. Using this prevents graphical bugs... mostly
    volatile int howManyTimesRepainted = 0;
    //hash maps of the coordinates where the line has already been. These are used to redraw all the red lines
    static HashMap<String,Integer> hashx1 = new HashMap<>();
    static HashMap<String,Integer> hashx2 = new HashMap<>();
    static HashMap<String,Integer> hashy1 = new HashMap<>();
    static HashMap<String,Integer> hashy2 = new HashMap<>();
    static HashMap<String,Color> hashColor = new HashMap<>();
    @Override
    public synchronized void paint(Graphics g){
        
        LINE_SIZE = GridLabel.LINE_SIZE;
        super.paint(g);
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        for(String x : hashColor.keySet()){
            if(x.equals("rightLine")) continue;
            int newBrightness = hashColor.get(x).getGreen() + colorChangeSpeed;
            if(newBrightness>255){
                newBrightness=255;
            }
            int red = hashColor.get(x).getRed() - newBrightness;
            if(red<0) red = 0;
            hashColor.put(x, new Color(red, newBrightness, 0));
        }

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
        if(!(hashx1.containsKey(tempx1+""+tempx2+""+tempy1+""+tempy2))){
            hashx1.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempx1);
            hashx2.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempx2);
            hashy1.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempy1);
            hashy2.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempy2);
        }
        hashColor.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempColor);

        if(counter==MyFrame.gridSize+1) {//g2D.drawLine(500, 0, 500, 500);
            hashx1.put("rightLine",500);
            hashx2.put("rightLine",500);
            hashy1.put("rightLine",0);
            hashy2.put("rightLine",500);
            hashColor.put("rightLine",tempColor);
        }        
        
        //draws all the red lines (the lines we have already visited) from the coordinates in the hasmaps
        g2D.setStroke(new BasicStroke(3));
        for(String i : hashx1.keySet()){
            g2D.setPaint(hashColor.get(i));
            tempx1 = hashx1.get(i);
            tempy1 = hashy1.get(i);
            tempx2 = hashx2.get(i);
            tempy2 = hashy2.get(i);
            g2D.drawLine(tempx1, tempy1, tempx2, tempy2);
        }
        //g2D.drawLine(x1, y1, x2, y2);



        //draws the cyan line aka. the current line from the current coordinates
        tempx1 = (int)(beginningx1*LINE_SIZE);
        tempy1 = (int)(beginningy1*LINE_SIZE);
        tempx2 = (int)(beginningx2*LINE_SIZE);
        tempy2 = (int)(beginningy2*LINE_SIZE);
        g2D.setPaint(Color.cyan);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawLine(tempx1, tempy1, tempx2, tempy2);
        
        //System.out.println("hm " +x1 + " " + x2);
        this.howManyTimesRepainted++;
    }
}