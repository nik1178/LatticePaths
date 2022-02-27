package remaster;

import java.util.*;

import javax.swing.*;

import java.awt.*;
//import java.awt.event.*;

public class CustomizeFrame extends JFrame{

    JButton pathToggleButton = new JButton("Toggle path");
    JButton backgroundButton = new JButton("Background color");
    JButton gridButton = new JButton("Grid color");
    JButton leadColorButton = new JButton("Lead color");
    JButton startColorButton = new JButton("Start color");
    JButton fadeColorButton = new JButton("Fade color");
    JButton pathColorButton = new JButton("Path color");
    CustomizeFrame(){

        pathToggleButton.setFocusable(false);
        pathToggleButton.addActionListener(
            (e) -> {
                MyRunnable.doPathLine = MyFrame.flipBoolean(MyRunnable.doPathLine);
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        MyRunnable.pathLines.clear();
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );

        backgroundButton.setFocusable(false);
        gridButton.setFocusable(false);
        leadColorButton.setFocusable(false);
        startColorButton.setFocusable(false);
        fadeColorButton.setFocusable(false);
        pathColorButton.setFocusable(false);

        this.add(pathToggleButton);
        this.add(backgroundButton);
        this.add(gridButton);
        this.add(leadColorButton);
        this.add(startColorButton);
        this.add(fadeColorButton);
        this.add(pathColorButton);

        this.getContentPane().setBackground(Color.black);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(200,620));
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.pack();
    }

    void setBackgroundColor(Color color){
        this.getContentPane().setBackground(color);
    }
}