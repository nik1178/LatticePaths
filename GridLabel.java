

import java.awt.*;

import javax.swing.*;

public class GridLabel extends JLabel implements Runnable{

    @Override
    public void run() {
        //repaint only once, as we only need the main grid once. The rest is dealt with my MyRunnable
        repaint();
    }

    GridLabel(){
        //made it 1 pixel bigger than it has to be, so the right most and bottom most lines are visible
        this.setPreferredSize(new Dimension(501,501));
    }

    int gridSize;
    int drawX=0;
    int drawY=0;
    static Color lineColor = Color.white;
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

        drawStartGrid(gridSize, g2D);
        
    }

    public void drawStartGrid(int gridSize, Graphics2D g2D){
        g2D.setPaint(lineColor);
        //draw gridSize+1 horizontal lines and gridSize+1 vertical lines. The spacing is based on the LINE_SIZE variable
        for(int i=0; i<=gridSize; i++){
            int x1 = 0;
            int x2 = 500;
            int y1 = (int) Math.round(i*LINE_SIZE);
            int y2 = y1;
            g2D.drawLine(x1, y1, x2, y2);
        }
        for(int i=0; i<=gridSize; i++){
            int y1 = 0;
            int y2 = 500;
            int x1 = (int) Math.round(i*LINE_SIZE);
            int x2 = x1;
            g2D.drawLine(x1, y1, x2, y2);
        }
    }

}
