package aljazVersion;

import java.awt.*;

import javax.swing.*;

public class GridLabel extends JLabel implements Runnable{

    @Override
    public void run() {
        //repaint everything constantly. Made so I don't have to call it at specific times, since the grid has to be painted constantly anyway
        while(true) repaint();
    }

    GridLabel(){
        //made it 1 pixel bigger than it has to be, so the right most and bottom most lines are visible
        this.setPreferredSize(new Dimension(501,501));
    }

    int gridSize;
    int drawX=0;
    int drawY=0;
    static double LINE_SIZE; //calculate how long each line from a single square has to be
    int counter = 1;
    /* static boolean stop = true; */
    
    @Override
    public void paint(Graphics g){
        super.paint(g); //paints everything below this component (the background color of the whole frame)
        MyFrame.howManyPathsField.setText(MyFrame.howManyPaths + ""); //constantly sets the correct number of how many paths have been found
        gridSize = MyFrame.gridSize;
        LINE_SIZE = 500.0/gridSize;
        MyFrame.vertical=0;
        MyFrame.horizontal=1;
        Graphics2D g2D = (Graphics2D) g;

        /* if(!(MyFrame.startGridDrawn)) */ drawStartGrid(gridSize, g2D);
        
        /* if(counter%2==0) g2D.drawLine(0, 0, 100, 100);
        else g2D.drawLine(0, 0, 100, 0);
        counter++; */
        /* g2D.setPaint(Color.cyan);
        g2D.drawLine(drawX, drawY, (int) Math.round(drawX+LINE_SIZE), drawY);
        if(!stop){
            drawX+=LINE_SIZE;
            System.out.println(drawX);
            if(drawX==500){
                drawX-=LINE_SIZE;
                drawY+=LINE_SIZE;
                g2D.drawLine(drawX, (int) Math.round(drawX-LINE_SIZE), drawX, drawY);
            }
            if(drawY==500){
                drawY=0;
                drawX-=LINE_SIZE*counter;
                counter++;
            }
            if(drawX==0) stop=true;
        } */
        
    }

    public void drawStartGrid(int gridSize, Graphics2D g2D){
        //draw the grid with simple instructions, hopefully I'm not so dumb in the future as to not know how this works
        //the grid is drawn one small line segment at a time, because the beginning vision of how I would code this was different from the end product.
        //I could have just drawn 21 vertical and 21 horizontal lines and it would have worked just as well
        g2D.setPaint(Color.white);
        /* for(int i=0; i<gridSize; i++){
            for(int j=0; j<gridSize; j++){
                //g2D.setPaint(new Color(0x000000+(i+j)*255*255*255/2/gridSize));
                //System.out.println(g2D.getPaint());
                int x1 = (int) Math.round(j*LINE_SIZE);
                int x2 = (int) Math.round((j+1)*LINE_SIZE);
                int y1 = (int) Math.round(i*LINE_SIZE);
                int y2 = y1;
                //System.out.println(x1);
                g2D.drawLine(x1, y1, x2, y2);
                x1 = (int) Math.round(i*LINE_SIZE);
                x2 = x1;
                y1 = (int) Math.round(j*LINE_SIZE);
                y2 = (int) Math.round((j+1)*LINE_SIZE);
                g2D.drawLine(x1, y1, x2, y2);
            }
        } */
        //draw 21 horizontal lines and 21 vertical lines. The spacing is based on the LINE_SIZE variable
        for(int i=0; i<=gridSize; i++){
            int x1 = 0;
            int x2 = 500;
            int y1 = (int) Math.round(i*LINE_SIZE);
            int y2 = y1;
            //System.out.println(x1);
            g2D.drawLine(x1, y1, x2, y2);
        }
        for(int i=0; i<=gridSize; i++){
            int y1 = 0;
            int y2 = 500;
            int x1 = (int) Math.round(i*LINE_SIZE);
            int x2 = x1;
            //System.out.println(x1);
            g2D.drawLine(x1, y1, x2, y2);
        }
        //draw the bottom most line and right most line all in one to avoid adding confusing if statements to previous for loops be
        /* g2D.drawLine(500, 0, 500, 500);
        g2D.drawLine(0, 500, 500, 500); */
        /*         try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */
        /*         try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */

        /* MyFrame.startGridDrawn = true; */
    }

}
