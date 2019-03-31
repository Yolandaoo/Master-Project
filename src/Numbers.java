import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * This class is used to generate natural numbers and it contains the main function
 * extends the class JFrame to create the frame and implements the interface ActionListener to control the functions of the system
 */
public class Numbers extends JFrame implements ActionListener{

    private JButton run, clear1,define, clear2; //four buttons to control the simulation system
    private JLabel input, pred; // two labels for helping the user to use the system
    private JTextField inputField, predField; //two text fields are used to input numbers
    private JTextArea console; //a text area for showing the details and the whole process of computing
    private Process result, predResult; //two objects of class Process for storing the result of getting numbers and predecessor
    public static CountDownLatch latch; // a object of CountDownLatch, which is used to count the number of thread and help to control the system
    public static Channel iChannel; //to store the current interface channel
    public static Channel pChannel; //to store the current private channel
    public static int inputNum; //to store the input natural number

    //the default constructor of class Numbers, and mainly used for creating the window of the system and setting the attributes
    public Numbers(){
        //setting the main attributes of the window
        setTitle("A distributed implementation of pi-calculus about natural numbers");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        setSize((int)(dim.width/1.5), (int)(dim.height/1.5));
        setLocation(new Point((int)(dim.width/3.3), dim.height/16));
        Container contentPane = this.getContentPane();

        //designate the buttons, labels and text fields to a JPanel, and then put it in the north of the frame
        JPanel p = new JPanel();
        input = new JLabel("NATURAL NUMBER : ");
        inputField = new JTextField(10);
        pred = new JLabel("THE PREDECESSOR : ");
        predField = new JTextField(10);
        p.add(input);
        p.add(inputField);
        run = addJButton(p, "RUN", this);
        clear1 = addJButton(p, "CLEAR", this);
        p.add(pred);
        p.add(predField);
        define = addJButton(p, "DEFINE", this);
        clear2 = addJButton(p, "CLEAR", this);
        contentPane.add(p, BorderLayout.NORTH);

        //designate the console, and set the number of rows, scroll type, initial text and color, and then put it in the south of the frame
        p = new JPanel();
        p.setLayout(new BorderLayout());
        console = new JTextArea();
        console.setRows(23);
        console.setLineWrap(true);
        console.setEditable(false);
        console.setText("Ready.\n");
        console.setFont(new Font("Romantic", Font.BOLD, 14));
        console.setBackground(Color.CYAN);
        JScrollPane scroll = new JScrollPane(console);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.add(scroll);
        contentPane.add(p, BorderLayout.SOUTH);
    }

    //to define what should the system do in different cases
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        //when the button named "run" be pressed
        if(source == run){
            if(!inputField.getText().isEmpty()){
                int n = Integer.parseInt(inputField.getText());//getting the value of the input text field
                //for(int n = 0; n<=100; n++){
                inputNum = n;
                console.append("The number that needs to be obtained is '" + n +"'. Working...\n");
                console.setCaretPosition(console.getText().length());
                if(n >= 0){//when the input number is a natural number
                    inputField.setEditable(false);
                    latch = new CountDownLatch(n); //new a object of CountDownLatch to count the number of the threads
                    iChannel = new Channel("i", 2, false); //to define the interface channel that used to communicate with other functions
                    result = new Process();
                    makeNumber(n, iChannel); //to call the function makeNumber for creating the process formula
                    inputField.setEditable(true); //set the text field editable
                    try {//let the thread sleep for 100 milliseconds
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        latch.await(); //waiting for all of the threads finish
                        synchronized (Transfer.class){
                            result = Transfer.process; //set the result process and then show it in the frame of system
                            console.append("THE  RESULT  PROCESS  IS : " + result.getNameP() +"\n\n");
                            console.setCaretPosition(console.getText().length());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }}else{//if there is a illegal input, print a sentence to show it
                    console.append("This is not a natural number!\n");
                    console.setCaretPosition(console.getText().length());
                }
            //}
        }else{//if there is a illegal input, print a sentence to show it
            console.append("Please input a natural number!\n");
            console.setCaretPosition(console.getText().length());
            }
        }//when the button named "clear1" be pressed
        else if(source == clear1){
            if(inputField.isEditable() == true){
                result = null;
                Transfer.process = null;
                inputField.setText("");
                console.append("Cleared!\n");
                console.setCaretPosition(console.getText().length());
            }else{
                console.append("This process is still working!\n");
                console.setCaretPosition(console.getText().length());
            }
        }//when the button named "define" be pressed
        else if(source == define){
            if(!predField.getText().isEmpty()){
                int n = Integer.parseInt(predField.getText());
                //predNum = n;
                console.append("The number that be asked for predecessor is '" + n +"'. Working...\n");
                console.setCaretPosition(console.getText().length());
                if(n >= 0){
                    predField.setEditable(false);
                    latch = new CountDownLatch(n); //used for counting the number of the threads
                    Channel i = new Channel("i", 2, false); //new the interface channel i
                    predResult = new Process();
                    predecessor(n, i);
                    predField.setEditable(true);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        latch.await(); // waiting for the end of all of the threads
                        synchronized (Transfer.class){
                            predResult = Transfer.process; //show the result process in the frame
                            console.append("THE  PREDECESSOR  PROCESS  IS : " + predResult.getNameP() +"\n\n");
                            console.setCaretPosition(console.getText().length());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    console.append("This is not a natural number!\n");
                    console.setCaretPosition(console.getText().length());
                }
            }else{
                console.append("Please input a natural number!\n");
                console.setCaretPosition(console.getText().length());
            }
        }//when the button named "clear2" be pressed
        else if(source == clear2){
            if(predField.isEditable() == true){
                predResult = null;
                Transfer.process = null;
                predField.setText("");
                console.append("Cleared!\n");
                console.setCaretPosition(console.getText().length());
            }else{
                console.append("This process is still working!\n");
                console.setCaretPosition(console.getText().length());
            }
        }
    }

    //a method for create buttons, add listener and add to panel
    private JButton addJButton(JPanel p, String s, ActionListener a) {
        JButton b = new JButton(s);
        b.addActionListener(a);
        p.add(b);
        return b;
    }

    // Zero(i) = i(zn).z^<>.Nil
    public Process zero(Channel i){
        //construct the formula of the process zero
        Process process = new Process();
        Channel[] priavteChannels = new Channel[]{ new Channel("z",0,true),
                                                    new Channel("n",1, true)};
        Action inputOnI = new Action(i, Direction.IN, priavteChannels);
        ActionAndProcess[] actionAndProcess = new ActionAndProcess[2];
        actionAndProcess[0] = new ActionAndProcess(inputOnI, null);
        actionAndProcess[1] = new ActionAndProcess(new Action(new Channel("z",0,true), Direction.OUT, null), Process.nil);
        process.setNameP("Zero");
        process.setNextSteps(actionAndProcess);
        console.append("The process performs like 'Zero'\n");
        console.setCaretPosition(console.getText().length());
        //map.put(process, 0);
        return process;
    }

    //  Node(ip) = i(zn).n^<p>.Nil
    public Process node(Channel i,Channel p){
        //construct the formula of the process node
        Process process = new Process();
        Channel[] priavteChannels = new Channel[]{ new Channel("z",0,true),
                                                    new Channel("n",1, true)};
        Action inputOnI = new Action(i, Direction.IN, priavteChannels);
        Channel[] temp1 = new Channel[]{p};
        ActionAndProcess[] actionAndProcess = new ActionAndProcess[2];
        actionAndProcess[0] = new ActionAndProcess(inputOnI, null);
        actionAndProcess[1] = new ActionAndProcess(new Action(new Channel("n",1,true), Direction.OUT, temp1), Process.nil);
        process.setNameP("Node");
        process.setNextSteps(actionAndProcess);
        console.append("The process performs like 'Node'\n");
        console.setCaretPosition(console.getText().length());
        return process;
    }

    //the function of calculating the predecessor of the input natural numbers
    public void predecessor(int n, Channel i){
        console.append("The interface channel for asking is : '" + i.getName() + "'\n");
        //generate the interface channel of the whole process, and then print the two private channels in the window of the system
        Channel[] priavteChannels = new Channel[]{ new Channel("z",0,true),
                                                    new Channel("n",1, true)};
        Action inqury = new Action(i, Direction.IN, priavteChannels);
        console.append("And the options are : '" + inqury.getPrivateChannels()[0].getName() +
                        "'  and '" + inqury.getPrivateChannels()[1].getName() + "'\n");
        console.setCaretPosition(console.getText().length());
        if(n == 0){ //if the input number is '0', then the predecessor of zero is still zero
            console.append("The interface channel is : '" + inqury.getPrivateChannels()[0].getName() + "'\n");
            console.setCaretPosition(console.getText().length());
            iChannel = inqury.getPrivateChannels()[0];
            Process temp = new Process("FZ",null, null);
            temp.start();
            console.append(temp + " : Zero\n");
            console.setCaretPosition(console.getText().length());
        }else{ //if the input number do not equals zero, then the predecessor of the number should be the process of the previous number
            iChannel = inqury.getPrivateChannels()[1]; //set the relative channel as the interface channel to talk to the predecessor
            inputNum = n-1;
            makeNumber(n-1, iChannel); // call the function makeNumber to get the previous number
        }
    }

    //a function for creating the process of a natural number and print out every single step of the process
    public void makeNumber(int n, Channel i){//System.out.println("&&&&&&&&&&&&Index: "+index);
        if(n == inputNum && inputNum != 0){ //if the input number is a positive number, and this is the first node
            pChannel = new Process().hide();
            console.append("The interface channel is : '"+i.getName()+"', the new channel is : '"+pChannel.getName()+"'\n");
            console.setCaretPosition(console.getText().length());
            Process temp = new Process("FN",null, null);
            temp.start();
            console.append(temp + " : Node\n");
            console.setCaretPosition(console.getText().length());
            iChannel = pChannel;
            makeNumber(n-1, pChannel);
        }else if(inputNum == 0){ //if the input number is a zero
            console.append("The interface channel is : '" + i.getName() + "'\n");
            console.setCaretPosition(console.getText().length());
            Process temp = new Process("FZ",null, null);
            temp.start();
            console.append(temp + " : Zero\n");
            console.setCaretPosition(console.getText().length());
        }else{ //if the input number is a positive number
            if (n == 0) {//System.out.println("&&&&&&&&&&&&Transfer: "+Transfer.process.getNameP());
                console.append("The interface channel is : '" + i.getName() + "'\n");
                console.setCaretPosition(console.getText().length());
                Process temp = new Process("Z",null, null);
                temp.start();
                console.append(temp + " : Zero\n");
                console.setCaretPosition(console.getText().length());
            }else if(n > 0){
                pChannel = new Process().hide();
                console.append("The interface channel is : '"+i.getName()+"', the new channel is : '"+pChannel.getName()+"'\n");
                console.setCaretPosition(console.getText().length());
                Process temp = new Process("N",null, null);
                temp.start();
                console.append(temp + " : Node\n");
                console.setCaretPosition(console.getText().length());
                iChannel = pChannel;
                makeNumber(n-1, pChannel);
            }
         }

    }

    //the main function of the whole project
    public static void main(String[] args){
        JFrame frm = new Numbers();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }

}
