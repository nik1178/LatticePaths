

import java.util.*;

import javax.swing.*;

import java.awt.*;
//import java.awt.event.*;

public class CustomizeFrame extends JFrame{

    JButton pathToggleButton = new JButton("Toggle path");
    JTextField pathToggleField = new JTextField("" + MyRunnable.doPathLine);
    JButton backgroundButton = new JButton("Background color");
    JButton gridButton = new JButton("Grid color");
    JButton leadColorButton = new JButton("Lead color");
    JButton startColorButton = new JButton("Start color");
    JButton fadeColorButton = new JButton("Fade color");
    JButton pathColorButton = new JButton("Path color");
    JButton fadeSpeedButton = new JButton("Fade speed");
    JTextField fadeSpeedField = new JTextField(); 

    ImageIcon icon = new ImageIcon("icon.png");

    CustomizeFrame(){

        pathToggleButton.setFocusable(false);
        pathToggleField.setEditable(false);
        pathToggleField.setPreferredSize(new Dimension(40,20));
        pathToggleButton.addActionListener(
            (e) -> {
                MyRunnable.doPathLine = MyFrame.flipBoolean(MyRunnable.doPathLine);
                pathToggleField.setText(MyRunnable.doPathLine + "");
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        MyRunnable.pathLines.clear();
                        succeeded=true;
                    }catch(Exception e2){System.out.println("Retrying pathLines.clear() in pathToggleButton");}
                }
            }
        );


        backgroundButton.setFocusable(false);
        gridButton.setFocusable(false);
        leadColorButton.setFocusable(false);
        startColorButton.setFocusable(false);
        fadeColorButton.setFocusable(false);
        pathColorButton.setFocusable(false);
        fadeSpeedButton.setFocusable(false);

        fadeSpeedField.setPreferredSize(new Dimension(50,20));
        fadeSpeedField.setText(MyRunnable.colorChangeSpeed + "");

        this.add(pathToggleButton);
        this.add(pathToggleField);
        this.add(backgroundButton);
        this.add(gridButton);
        this.add(leadColorButton);
        this.add(startColorButton);
        this.add(fadeColorButton);
        this.add(pathColorButton);
        this.add(fadeSpeedButton);
        this.add(fadeSpeedField);

        this.setIconImage(this.icon.getImage());
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
