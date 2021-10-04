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
    @Override
    public void run() {
        //constantly looping the algorithm for when it should be called. If the algorithm is stopped or it finishes it calls the startButtonStop() method so the button properly chagnes
        //if the algorithm stops it also resets all the coordinates back to 0 so it will start from the beginning when the user pressed "Start"
        /* while(!Thread.interrupted()){ */
            
            algo(0, 0);
            counter=0;
            shouldReturn=true;
            if(stopButtonCounter==1) MyFrame.startButtonStop();
            stopButtonCounter=0;
            x1=0;
            x2=0;
            y1=0;
            y2=0;
        /* } */
    }

    double LINE_SIZE = GridLabel.LINE_SIZE;
    public void algo(int vertical, int horizontal)
    {
        //System.out.println(shouldReturn);
        if(shouldReturn) return;
        repaint();
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
            
            repaint();
            algo(vertical, horizontal+1);

            x1=horizontal;
            y1=y2;
            x2=horizontal+1;
            
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

            repaint();
            algo(vertical+1, horizontal);
        }
    }

    //hash maps of the coordinates where the line has already been. These are used to redraw all the red lines
    static HashMap<String,Integer> hashx1 = new HashMap<>();
    static HashMap<String,Integer> hashx2 = new HashMap<>();
    static HashMap<String,Integer> hashy1 = new HashMap<>();
    static HashMap<String,Integer> hashy2 = new HashMap<>();
    @Override
    public synchronized void paint(Graphics g){
        
        LINE_SIZE = GridLabel.LINE_SIZE;
        super.paint(g);
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        int tempx1 = (int)(this.x1*LINE_SIZE);
        int tempy1 = (int)(this.y1*LINE_SIZE);
        int tempx2 = (int)(this.x2*LINE_SIZE);
        int tempy2 = (int)(this.y2*LINE_SIZE);

        //if the coordinates are already in the hasmaps they are skipped, otherwise they are saved into the hasmaps so they can be drawn later
        //we don't save all the coordinates so that we don't have to draw the same line multiple times for no reason
        if(!(hashx1.containsKey(tempx1+""+tempx2+""+tempy1+""+tempy2))){
            hashx1.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempx1);
            hashx2.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempx2);
            hashy1.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempy1);
            hashy2.put(tempx1+""+tempx2+""+tempy1+""+tempy2,tempy2);
        }

        if(counter==MyFrame.gridSize+1) {//g2D.drawLine(500, 0, 500, 500);
            hashx1.put("rightLine",500);
            hashx2.put("rightLine",500);
            hashy1.put("rightLine",0);
            hashy2.put("rightLine",500);
        }        
        
        //draws all the red lines (the lines we have already visited) from the coordinates in the hasmaps
        g2D.setPaint(Color.red);
        g2D.setStroke(new BasicStroke(3));
        for(String i : hashx1.keySet()){
            tempx1 = hashx1.get(i);
            tempy1 = hashy1.get(i);
            tempx2 = hashx2.get(i);
            tempy2 = hashy2.get(i);
            g2D.drawLine(tempx1, tempy1, tempx2, tempy2);
        }
        //g2D.drawLine(x1, y1, x2, y2);



        //draws the cyan line aka. the current line from the current coordinates
        tempx1 = (int)(this.x1*LINE_SIZE);
        tempy1 = (int)(this.y1*LINE_SIZE);
        tempx2 = (int)(this.x2*LINE_SIZE);
        tempy2 = (int)(this.y2*LINE_SIZE);
        g2D.setPaint(Color.cyan);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawLine(tempx1, tempy1, tempx2, tempy2);
        
        //System.out.println("hm " +x1 + " " + x2);
    }
}