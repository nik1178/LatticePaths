package remaster;

import javax.swing.*;

import java.awt.*;
//import java.awt.event.*;

public class MyFrame extends JFrame /* implements ChangeListener */{

    
    JButton submitButton;
    static JButton startButton = new JButton("Start");
    JTextField submitField;
    //static JSlider delaySlider;

    static JButton delayButton = new JButton("Submit Delay in milliseconds");
    static JTextField delayTextField  = new JTextField();

    ImageIcon icon = new ImageIcon("icon.png");

    static JTextField howManyPathsField = new JTextField("0");
    static int gridSize = 1;
    static int howManyPaths = 0;
    static boolean startGridDrawn = false;
    static int pressedStart = 0;
    static int horizontal = 1;
    static int vertical = 0;
    int startCounter = 0;

    //make new class and thread for the algorithm that calculates the path that it needs to take.
    static MyRunnable algoRunnable = new MyRunnable();
    static Thread algoThread = new Thread(algoRunnable);
    //Timer timer = new Timer(500,this);

    MyFrame(){

        /* delaySlider = new JSlider(0,2500);
        delaySlider.setPreferredSize(new Dimension(350,20));
        delaySlider.setBackground(Color.BLACK);
        delaySlider.addChangeListener(this); */
        delayTextField.setPreferredSize(new Dimension(40,20));

        delayButton.setFocusable(false);
        delayButton.addActionListener(
            (e) -> {
                try{
                    //set delay for thread.sleep() method in MyRunnable
                    MyRunnable.DELAY = Integer.parseInt(delayTextField.getText());
                } catch(Exception e2){
                    System.out.println(e2);
                }
            }
        );

        //make new object from GridLabel class. The class is used for drawing the basic grid.
        GridLabel gridLabel1 = new GridLabel();
        //start new thread for GridLabel class
        Thread drawThread = new Thread(gridLabel1);
        drawThread.start();
        //give it low priority because why not. Didn't test if it does anything or not
        drawThread.setPriority(1);

        submitButton = new JButton("Submit grid size");
        submitButton.setFocusable(false);
        submitButton.addActionListener(
            (e) -> {
                startButtonStop();
                //after stopping the algorithm, wait 0.1s so the algorithm truly stops and doesn't acidentally draw new red lines after the old ones have been cleared
                try {
                    Thread.sleep(100);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                //clear all the red lines
                MyRunnable.hashx1.clear();
                MyRunnable.hashy1.clear();
                MyRunnable.hashx2.clear();
                MyRunnable.hashy2.clear();
                //restart how many paths have been calculated
                howManyPaths=0;
                //try getting an integer. Try-catch added if someone inputs a String
                try{
                    gridSize=Integer.parseInt(submitField.getText());
                } catch(Exception e5){
                    System.out.println(e5);
                }
                //System.out.println(gridSize);
                /* startGridDrawn = false; */
                //call the paint method in GridLabel so it draws the grid again based on the new grid size
                repaint();
            }
        );
        

        startButton.setFocusable(false);

        startButton.addActionListener(
            (e) -> {
                //the button is toggleable so we check how many times it's been pressed
                if(pressedStart%2==0){
                    pressedStart++; //increment the amount of times it's been pressed. Would have probably been better to just set it to "1"
                    //System.out.println("Wha..?");
                    //clear all the red lines
                    MyRunnable.hashx1.clear();
                    MyRunnable.hashy1.clear();
                    MyRunnable.hashx2.clear();
                    MyRunnable.hashy2.clear();
                    //set amount of calculated paths to 0
                    howManyPaths=0;
                    //change the text of the button to "stop", so it's clear that it's function changed
                    startButton.setText("Stop");
                    /* GridLabel.stop=false; */
                    /* algoThread.start(); */
                    //another counter is used to truly see if it's the first time the button has been pressed. If it is, it start up the thread that runs the algorithm
                    algoThread = new Thread(algoRunnable);
                    algoThread.start();
                    //tells the algorithm not to exit. Best way I could find to start and stop the method
                    algoRunnable.shouldReturn=false;
                    /* algo(vertical,horizontal);
                    timer.start(); */
                } else{
                    /* startButtonStop(); */
                    //call method to execute what the button should do if it has been pressed while it says "Stop". Used a method because I needed to call this code from different classes
                    startButtonStop();
                    /* try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } */
                    /* algoThread.interrupt(); */
                }
            }
        );

        submitField = new JTextField();
        submitField.setPreferredSize(new Dimension(40,20));

        howManyPathsField.setEditable(false);
        howManyPathsField.setPreferredSize(new Dimension(100,20));

        this.add(submitButton);
        this.add(submitField);
        /* this.add(delaySlider); */
        //this.add(gridLabel1);

        this.add(delayButton);
        this.add(delayTextField);

        this.add(algoRunnable);
        this.add(startButton);
        this.add(howManyPathsField);

        this.setIconImage(icon.getImage());
        this.getContentPane().setBackground(Color.black);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(600,200,0,0);
        this.setPreferredSize(new Dimension(550,620));
        this.setLayout(new FlowLayout());
        this.pack();
        this.setVisible(true);

    }

    public static void startButtonStop(){
        //restart the amount of times it's been pressed to 0. I didn't just increment it, because sometimes this function accidentally gets called twice and that would screw with the true amount of times it's been pressed
        pressedStart=0;
        //tells the while loop in the MyRunnable class that it should stop calling this method
        algoRunnable.stopButtonCounter=0;
        startButton.setText("Start");
        //System.out.println("yup, you were right");
        //tells the algorithm in MyRunnable to stop running by exiting as soon as it enters
        algoRunnable.shouldReturn=true;
        algoThread.interrupt();
    }

    /* @Override
    public void stateChanged(ChangeEvent e) {
        algoRunnable.DELAY = delaySlider.getValue();
    } */

    /* int horizontalSetback = 1;
    @Override
	public void actionPerformed(ActionEvent e) {
		
        /* howManyPathsField.setText(howManyPaths + "");
        repaint(); */
        /* if(vertical==gridSize && horizontal==gridSize){
            vertical=0;
            horizontal-=horizontalSetback;
            horizontalSetback++;
        }
        if(horizontal<gridSize){
            horizontal++;
        } else {
            howManyPaths++;
            horizontal--;
            vertical++;
        }
        
		if(horizontal==0){
            System.out.println("OIIIII!!");
            startButtonStop();
            howManyPaths*=2;
            pressedStart++;
            howManyPathsField.setText(howManyPaths + "");
        } /* repaint(); */
	/* } */
/*     public void algo(int vertical, int horizontal)
    {
        System.out.println(Thread.currentThread().getPriority() + " this");
        if(horizontal<gridSize)
        {
            repaint();
            algo(vertical, horizontal+1);
        }
        else howManyPaths++;
        if(vertical<gridSize && horizontal<gridSize)
        {
            algo(vertical+1, horizontal);
        }
    } */
}
