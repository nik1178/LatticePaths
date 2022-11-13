

import java.util.ArrayList;

import javax.swing.*;

import java.awt.*;
//import java.awt.event.*;
import java.math.BigInteger;

public class MyFrame extends JFrame /* implements ChangeListener */{

    
    JButton submitGridButton;
    static JButton startButton = new JButton("Start");
    JTextField submitField;
    //static JSlider delaySlider;

    static JButton delayButton = new JButton("Submit Delay in milliseconds");
    static JTextField delayTextField  = new JTextField("100");

    JButton instantButton = new JButton("Fast");
    JTextField instantTextField = new JTextField(); 

    JButton customizeButton = new JButton("Customize");

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

    CustomizeFrame customizeFrame = new CustomizeFrame();

    static Dimension windowDimension = new Dimension(550,620);
    static Color backgroundColor = Color.black;

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
                    int newGridSize = Integer.parseInt(submitField.getText());
                    if(newGridSize>0){
                        this.gridSize=newGridSize;
                    }
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
                    //set amount of calculated paths to 0
                    howManyPaths=0;
                    algoThread.interrupt();
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
                //String text = arrayToBignum(divide(factorial(2*gridSize) , multiply(factorial(gridSize) , factorial(gridSize)) ) ); 
                BigInteger num = BigInteger.valueOf(MyFrame.gridSize);
                String text = ( (bigIntFactorial(num.multiply(BigInteger.valueOf(2)))).divide(bigIntFactorial(num).multiply(bigIntFactorial(num))) ).toString();
                instantTextField.setText(text);
                print("fin");
            }
        );
        instantTextField.setEditable(false);
        instantTextField.setPreferredSize(new Dimension(100,20));

        customizeButton.setFocusable(false);
        customizeButton.addActionListener(
            (e) -> {
                customizeFrame.setVisible(true);
                customizeFrame.setLocation(this.getX()+(int)this.windowDimension.getWidth(), this.getY());
            }
        );

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

        this.add(customizeButton);

        this.setIconImage(icon.getImage());
        this.getContentPane().setBackground(backgroundColor);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(windowDimension);
        this.setLayout(new FlowLayout());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //Customize frame-----------------------------------------------
        customizeFrame.setLocation(this.getX()+(int)this.windowDimension.getWidth(), this.getY());

        customizeFrame.backgroundButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        Color color = JColorChooser.showDialog(null, "Background color", MyFrame.backgroundColor);
                        if(color!=null){
                            this.getContentPane().setBackground(color);
                            this.backgroundColor = color;
                            customizeFrame.setBackgroundColor(color);
                        }
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
        customizeFrame.gridButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        Color color = JColorChooser.showDialog(null, "Grid color", GridLabel.lineColor);
                        if(color!=null) GridLabel.lineColor = color;
                        repaint();
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
        customizeFrame.leadColorButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        Color color = JColorChooser.showDialog(null, "Lead color", MyRunnable.leadColor);
                        if(color!=null) MyRunnable.leadColor = color;
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
        customizeFrame.startColorButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        Color color = JColorChooser.showDialog(null, "Start color", MyRunnable.initColor);
                        if(color!=null) MyRunnable.initColor = color;
                        if(!algoRunnable.shouldReturn){
                            if(MyRunnable.lines.containsKey("rightLine")){
                                MyRunnable.lines.get("rightLine").color = color;
                                MyRunnable.lines.get("rightLine").resetDoubles();
                            }
                        }
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
        customizeFrame.fadeColorButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        Color color = JColorChooser.showDialog(null, "Fade color", MyRunnable.fadeColor);
                        if(color!=null) MyRunnable.fadeColor = color;
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
        customizeFrame.pathColorButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        Color color = JColorChooser.showDialog(null, "Path color", MyRunnable.pathColor);
                        if(color!=null) MyRunnable.pathColor = color;
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
        customizeFrame.fadeSpeedButton.addActionListener(
            (e) -> {
                boolean succeeded = false;
                while(!succeeded){
                    try{
                        try{
                            MyRunnable.colorChangeSpeed=Double.parseDouble(customizeFrame.fadeSpeedField.getText());
                        }catch(Exception e3){
                            System.out.println("Not a number");
                        }
                        succeeded=true;
                    }catch(Exception e2){}
                }
            }
        );
    }

    public static void startButtonStop(){
        //restart the amount of times it's been pressed to 0. I didn't just increment it, because sometimes this function accidentally gets called twice and that would screw with the true amount of times it's been pressed
        pressedStart=0;
        //tells the while loop in the MyRunnable class that it should stop calling this method
        algoRunnable.stopButtonBoolean=false;
        startButton.setText("Start");
        //tells the algorithm in MyRunnable to stop running by exiting as soon as it enters
        algoRunnable.shouldReturn=true;
    }


    public static boolean flipBoolean(boolean b){
        if(b) b=false;
        else b=true;
        return b;
    }

    //Math----------------------------------------------------

    BigInteger bigIntFactorial(BigInteger num){
        BigInteger counter = BigInteger.valueOf(1);
        BigInteger result = BigInteger.valueOf(1);
        while(!counter.toString().equals(num.add(BigInteger.valueOf(1)).toString())){
            result = result.multiply(counter);
            counter = counter.add(BigInteger.valueOf(1));
        }
        return result;
    }

    /* int[] factorial(int n){
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

    static int[] checkOverflow(int num){
        int[] arr = {num};
        return checkOverflow(arr);
    }
    //Turn the array into an arrayList and send it to the other checkOverflow, then turn the result back into an array and return it
    static int[] checkOverflow(int[] arr){
        return convertList(checkOverflow(convertList(arr)));
    }
    static ArrayList<Integer> checkOverflow(ArrayList<Integer> al){
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
                //i--;
            }
        }
        return al;
    }
    int[] multiply(int[] arr1, int num){
        int[] arr2 = makeArray(num);
        return multiply(arr1, arr2);
    }
    int[] multiply(int[] arr1, int[] arr2){
        //Primary school multiplication into lines
        
        //120*120
        //  120
        //   240
        //    000
        //-------
        //  14400

        int[][] results = new int[arr2.length][arr1.length*2+1];
        for(int i=0; i<arr2.length; i++){
            for(int j=0; j<arr1.length;j++){
                //Multiply each result by the amount it needs to be offset in the basic multiplication
                results[i][j+i] = arr1[j]*arr2[i];
            }
            results[i] = checkOverflow(results[i]);
        }

        //Add the lines together
        int[] result = new int[results[0].length];
        for(int i=0; i<results[0].length; i++){
            for(int j=0; j<results.length; j++){
                result[i] += results[j][i];
            }
            result = checkOverflow(result);
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
        if(result.length==0){
            result = new int[] {0};
        }
        return result;
    }

    int[] divide(int[] arr1, int[] arr2){
        //multiply arr2 until it's bigger or equal to arr1. The amount of times you had to do that is your result. I ain't doing decimal places, don't need 'em
        //^Doesn't seem like I did this method. I did the basic OŠ method
        //if the second num is longer than the first num, it's obviously bigger and can't be divided
        if(arr2.length>arr1.length){
            return new int[] {0};
        }
        if(arr2.length==1 && arr2[0]==0){
            return new int[] {-1};
        }

        print(arr1);
        print(arr2);

        int[] result = new int[arr1.length];
        ArrayList<Integer> remainder = new ArrayList<>();

        for(int i=arr1.length-1; i>=0; i--){
            //Keep adding the next digit of the first number to the remainder until it's bigger than the second number
            //Don't know why it's called remeainder, it should be called a subnumber or something
            remainder.add(0, arr1[i]);
            //if(remainder.size()<firstLength-2)continue;

            //Multiply the result, because every repetition means adding a new digit to the result like in the OŠ method
            result = multiply(result, 10);

            //subtract the second number from the remainder as many times as possible.  We are checking if temp[0] is bigger or equal to 0, because the checkNegativeOverflow method will return a -1 in that spot if the result is negative
            int[] temp = subtract(convertList(remainder), arr2);
            while(temp[0]>=0){
                remainder = subtract(remainder, convertList(arr2));
                result[0]++;
                //result = checkOverflow(result);
                temp = subtract(temp, arr2);

            }
        }
        print("-----");
        result = checkOverflow(result);
        result = trim(result);
        return result;
    }

    int[] subtract(int[] arr1, int[] arr2){
        return convertList( subtract(convertList(arr1), convertList(arr2)) );
    }
    ArrayList<Integer> subtract(ArrayList<Integer> al1, ArrayList<Integer> al2){
        //Primary school subtraction
        
        //  124124
        // - 41212
        // -------
        //   82912
       
        if(al1.size()<al2.size()) return convertList(new int[] {-1});

        int[] result = new int[al1.size()];
        for(int i=0; i<al1.size(); i++){
            int subtraction = 0;
            if(i<al2.size()){
                subtraction = al2.get(i);
            }

            //Maybe useless: += because: If it overflows into the negative, we will subtract from the next space, giving us a negative num in a not yet used position.
            result[i]+=al1.get(i) - subtraction;
        }
        result = checkNegativeOverflow(result);
        result = trim(result);
        return convertList(result);
    }

    int[] checkNegativeOverflow(int[] arr){
        for(int i=0; i<arr.length; i++){
            if(arr[i]<0){
                if(arr.length<=i+1){
                    return new int[] {-1};
                }
                arr[i+1]--;
                arr[i]+=10;
            }
        }
        return arr;
    } */

    /* static int[] makeArray(int n){
        return checkOverflow(n);
    } */

    static int[] convertList(ArrayList<Integer> al){
        return listToArray(al);
    }
    static ArrayList<Integer> convertList(int[] arr){
        return arrayToList(arr);
    }

    static ArrayList<Integer> arrayToList(int[] arr){
        ArrayList<Integer> result = new ArrayList<>();
        for(int i=0; i<arr.length; i++){
            result.add(arr[i]);
        }
        return result;
    }

    static int[] listToArray(ArrayList<Integer> al){
        int[] result = new int[al.size()];
        for(int i=0; i<al.size(); i++){
            result[i]=al.get(i);
        }
        return result;
    }

    static String arrayToBignum(int[] arr){
        StringBuilder sb = new StringBuilder("");
        for(int i=arr.length-1; i>=0; i--){
            sb.append(arr[i]);
        }
        return sb.toString();
    }
    static String arrayToBignum(int[] arr, boolean b){
        StringBuilder sb = new StringBuilder("");
        for(int i=arr.length-1; i>=0; i--){
            sb.append(arr[i]);
            sb.append(",");
        }
        return sb.toString();
    }

    static void print(ArrayList<Integer> al){
        print(convertList(al));
    }
    static void print(int[] arr){
        print(arrayToBignum(arr));
    }
    static void print(int[] arr, boolean b){
        print(arrayToBignum(arr, true));
    }

    static void print(String s){
        System.out.println(s);
    }
    
    static void print(int n){
        System.out.println(n);
    }
}
