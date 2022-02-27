package remaster;

import java.util.*;

import javax.swing.*;

import java.awt.*;
//import java.awt.event.*;

public class MyFrame extends JFrame /* implements ChangeListener */{

    
    JButton submitGridButton;
    static JButton startButton = new JButton("Start");
    JTextField submitField;
    //static JSlider delaySlider;

    static JButton delayButton = new JButton("Submit Delay in milliseconds");
    static JTextField delayTextField  = new JTextField();

    JButton instantButton = new JButton("Instant");
    JTextField instantTextField = new JTextField(); 


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

        submitGridButton = new JButton("Submit grid size");
        submitGridButton.setFocusable(false);
        submitGridButton.addActionListener(
            (e) -> {
                startButtonStop();
                algoRunnable.shouldReturn=true;
                //try getting an integer. Try-catch added if someone inputs a String
                try{
                    gridSize=Integer.parseInt(submitField.getText());
                } catch(Exception e5){
                    System.out.println(e5);
                }
                algoRunnable.breakAnimation = true;
                try {
                    algoThread.join();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                MyRunnable.lines.clear();
                //call the paint method in GridLabel so it draws the grid again based on the new grid size
                repaint();
            }
        );
        

        startButton.setFocusable(false);

        startButton.addActionListener(
            (e) -> {
                //the button is toggleable so we check how many times it's been pressed
                if(pressedStart%2==0){
                    pressedStart=1; //increment the amount of times it's been pressed. Would have probably been better to just set it to "1"
                    //clear all the red lines
                    //MyRunnable.lines.clear();
                    //set amount of calculated paths to 0
                    howManyPaths=0;
                    algoThread.interrupt();
                    /* try{
                        algoThread.join();
                    } catch(Exception e5){
                        System.out.println("Couldn't join algoThread");
                    } */
                    //change the text of the button to "stop", so it's clear that it's function changed
                    startButton.setText("Stop");
                    //another counter is used to truly see if it's the first time the button has been pressed. If it is, it start up the thread that runs the algorithm
                    algoThread = new Thread(algoRunnable);
                    algoThread.start();
                    //tells the algorithm not to exit. Best way I could find to start and stop the method
                    algoRunnable.shouldReturn=false;
                } else{
                    //call method to execute what the button should do if it has been pressed while it says "Stop". Used a method because I needed to call this code from different classes
                    startButtonStop();
                }
            }
        );

        instantButton.setFocusable(false);
        instantButton.addActionListener(
            (e) -> {
                String text = arrayToBignum(divide(factorial(2*gridSize) , multiply(factorial(gridSize) , factorial(gridSize)) ) ); 
                instantTextField.setText(text);
            }
        );
        instantTextField.setEditable(false);
        instantTextField.setPreferredSize(new Dimension(100,20));

        submitField = new JTextField();
        submitField.setPreferredSize(new Dimension(40,20));

        howManyPathsField.setEditable(false);
        howManyPathsField.setPreferredSize(new Dimension(100,20));

        this.add(submitGridButton);
        this.add(submitField);
        /* this.add(delaySlider); */
        //this.add(gridLabel1);

        this.add(delayButton);
        this.add(delayTextField);

        this.add(algoRunnable);
        this.add(startButton);
        this.add(howManyPathsField);

        this.add(instantButton);
        this.add(instantTextField);

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
        algoRunnable.stopButtonBoolean=false;
        startButton.setText("Start");
        //System.out.println("yup, you were right");
        //tells the algorithm in MyRunnable to stop running by exiting as soon as it enters
        algoRunnable.shouldReturn=true;
        //algoThread.interrupt();
        //MyRunnable.lines.clear();
        //algoRunnable.repaint();
    }

    int[] factorial(int n){
        if(n<0){
            int[] x = {0};
            return x;
        }

        ArrayList<Integer> al = new ArrayList<>();
        al.add(1);
        while(n>0){
            for(int i=0; i<al.size(); i++){
                int currentNum = al.get(i) * n;
                al.set(i, currentNum);
            }
            al = checkOverflow(al);
            n--;
        }

        int[] result = new int[al.size()];
        for(int i=0; i<al.size(); i++){
            result[i] = al.get(i);
        }
        return result;
    }

    int[] checkOverflow(int num){
        int[] arr = {num};
        return checkOverflow(arr);
    }
    //Turn the array into an arrayList and send it to the other checkOverflow, then turn the result back into an array and return it
    int[] checkOverflow(int[] arr){
        ArrayList<Integer> al = new ArrayList<>();
        for(int i=0; i<arr.length; i++){
            al.add(arr[i]);
        }
        
        al = checkOverflow(al);

        int[] result = new int[al.size()];
        for(int i=0; i<al.size(); i++){
            result[i] = al.get(i);
        }
        return result;
    }
    ArrayList<Integer> checkOverflow(ArrayList<Integer> al){
        //Check for each num in the array if it's bigger than 10. If it is, add the tens digit to the next num in the array
        for(int i=0; i<al.size(); i++){
            int currentNum = al.get(i);
            if(currentNum>=10){
                int overflow = currentNum/10;
                currentNum %= 10;
                al.set(i, currentNum);
                //If there is no next num in the array, add a 0 and add it to that
                if(i==al.size()-1){
                    al.add(0);
                }
                al.set(i+1,al.get(i+1)+overflow);
            }
        }
        return al;
    }
    int[] multiply(int[] arr1, int num){
        int[] arr2 = {num};
        return multiply(arr1, arr2);
    }
    int[] multiply(int[] arr1, int[] arr2){
        //Primary school multiplication into lines
        /*
        120*120
          120
           240
            000
        -------
          14400
        */
        int[][] results = new int[arr2.length][arr1.length*2+1];
        for(int i=0; i<arr2.length; i++){
            for(int j=0; j<arr1.length;j++){
                //Multiply each result by the amount it needs to be offset in the basic multiplication
                results[i][j] = arr1[j]*arr2[i]*(int)Math.pow(10,i);
            }
            results[i] = checkOverflow(results[i]);
        }

        //Add the lines together
        int[] result = new int[results[0].length];
        for(int i=0; i<results[0].length; i++){
            for(int j=0; j<results.length; j++){
                result[i] += results[j][i];
            }
            checkOverflow(result);
        }

        result = trim(result);
        return result;
    }

    //remove any zeroes in the front of a largenum array
    int[] trim(int[] arr){
        int lastNumIndex=0;
        for(int i=arr.length-1; i>=0; i--){
            if(arr[i]!=0){
                lastNumIndex=i+1;
                break;
            }
        }

        int[] result = new int[lastNumIndex];
        for(int i=0; i<lastNumIndex; i++){
            result[i]=arr[i];
        }
        return result;
    }

    int[] divide(int[] arr1, int[] arr2){
        //multiply arr2 until it's bigger or equal to arr1. The amount of times you had to do that is your result. I ain't doing decimal places, don't need 'em
        //if the second num is longer than the first num, it's obviously bigger and can't be divided
        if(arr2.length>arr1.length){
            return new int[] {0};
        }
        if(arr2.length==1 && arr2[0]==0){
            return new int[] {-1};
        }

        int multiplier = 1;
        int[] arr2copy = arr2.clone();
        while(arr2copy.length<=arr1.length){
            multiplier++;
            arr2copy = multiply(arr2, multiplier);
            if(arr2copy.length<arr1.length){
                continue;
            }
            //If the two numbers are the same length, check if arr2copy is bigger than arr1, if it is, reduce the multiplier by 1 and return it
            boolean shouldContinue = false;
            for(int i=arr2copy.length-1; i>=0;i--){
                if(arr2copy[i]>arr1[i]){
                    return checkOverflow(multiplier-1);
                }else if(arr2copy[i]<arr1[i]){
                    shouldContinue = true;
                    break;
                }
            }
            if(shouldContinue) continue;
            //if we got out of the for loop without calling continue, it means the two numbers are identical. Return the multiplier
            return checkOverflow(multiplier);
        }
        //If it escaped the while loop, it means that arr2copy length is bigger than arr1 length, so the second num got too big, meaning we reduce the multiplier by 1
        return checkOverflow(multiplier-1);
    }

    String arrayToBignum(int[] arr){
        StringBuilder sb = new StringBuilder("");
        for(int i=arr.length-1; i>=0; i--){
            sb.append(arr[i]);
        }
        return sb.toString();
    }
}
