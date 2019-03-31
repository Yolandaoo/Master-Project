import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This class is to implement the processes that should be reply and perform some behaviours
 * and this class extends the class Thread, which means process is able to performs parallel
 */
public class Process extends Thread{
    private String nameP; //the name of the process
    private ActionAndProcess[] nextSteps; //the ActionAndProcess array stores the action and process pairs about next steps
    private CountDownLatch latch; //count the number of threads
    //the getter of the next steps' array
    public ActionAndProcess[] getNextSteps() {
        return nextSteps;
    }
    //the setter of the next steps' array
    public void setNextSteps(ActionAndProcess[] nextSteps) {
        this.nextSteps = nextSteps;
    }
    //the getter of the name of process
    public String getNameP() {
        return nameP;
    }
    //the setter of the name of process
    public void setNameP(String name) {
        this.nameP = name;
    }
    //the default constructor of class Process
    Process(){
        this.nameP = "";
        this.nextSteps = null;
        this.latch = null;
    }
    //the constructor of class Process with parameters
    Process(String n, ActionAndProcess[] np, CountDownLatch l){
        this.nameP = n;
        this.nextSteps = np;
        this.latch = l;
    }

    // Nil has no behaviour
    public static Process nil = new Process("Nil", null, null);
    //the static object of Process temp is used for store the result in the process of the whole behaviour
    public static Process temp = new Process();

    // the override function that contains the works need to be done in the processes
    @Override
    public void run() {
        //locking the whole class Transfer to ensure the multithreading works well
        synchronized (Transfer.class) {
            //if this is the first time that the run() is called in the processes of making a natural number
            if(this.getNameP().equals("FN")){
                // Initializing the static variable "process" of class Transfer to be a node if the name of the process is "FN"
                Transfer.process = new Numbers().node(Numbers.iChannel, Numbers.pChannel);//System.out.println("This is FN: "+Transfer.process.getNameP());
                try {//let the current thread to sleep for 100  milliseconds
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }// Initializing the static variable "process" of class Transfer  to be a zero if the name of the process is "FZ"
            else if(this.getNameP().equals("FZ")){
                Transfer.process = new Numbers().zero(Numbers.iChannel);System.out.println("This is FZ: "+Transfer.process.getNameP());
            }else{
                //if the name of this process is "N", the current result process should parallel with a node(i,p)
                if(this.getNameP().equals("N")){//System.out.println("Transfer.process of N: "+Transfer.process.getNameP());
                    temp = parallel(Transfer.process, new Numbers().node(Numbers.iChannel, Numbers.pChannel));
                    Transfer.process = temp;//System.out.println("Transfer.process of N: "+Transfer.process.getNameP());
                }//if the name of this process is "Z", the current result process should parallel with a zero(i)
                else if(this.getNameP().equals("Z")){//System.out.println("Transfer.process of Z: "+Transfer.process.getNameP());
                    temp = parallel(Transfer.process, new Numbers().zero(Numbers.iChannel));
                    Transfer.process = temp;//System.out.println("Transfer.process of Z: "+Transfer.process.getNameP());
                }
            }
        }
        Numbers.latch.countDown(); //when a thread have finished, the variable latch that used for counting the threads should minis one
    }

    //Concat of the nextStep arrays of two processes
    static ActionAndProcess[] concat(ActionAndProcess[] a, ActionAndProcess[] b){
        if(a == null && b == null){
            return null;
        }else if(a == null && b != null){
            return b;
        }else if(a != null && b == null){
            return a;
        }else{
            ActionAndProcess[] c = new ActionAndProcess[a.length+b.length];
            System.arraycopy(a,0, c, 0,a.length);
            System.arraycopy(b, 0, c, a.length, b.length);
            return c;
        }
    }

    //Add a ActionAndProcess element to the result
    static ActionAndProcess[] addNew(ActionAndProcess[] a, ActionAndProcess b){
        ActionAndProcess[] c;
        if(a == null){
            c = new ActionAndProcess[1];
            c[0] = b;
        }else{
            c = new ActionAndProcess[a.length+1];
            System.arraycopy(a,0, c, 0,a.length);
            System.arraycopy(b, 0, c, a.length, 1);
        }
        return c;
    }

    //reply to the all of the actions and stop, the same as the syntax rule prefix
    public Process replyThenDie(Channel[] inputChannels, int index, Channel[][] privateChannels){
        Process process = new Process();
        ActionAndProcess[] actionAndProcess = new ActionAndProcess[1];
        if(index != 0){
            Action action;
            if(privateChannels != null){
                action = new Action(inputChannels[index-1], Direction.OUT, privateChannels[index-1]);//the action that be replied at present
            }else{
                action = new Action(inputChannels[index-1], Direction.OUT, null);//the action that be replied at present
            }
            if((index-1) > 0){
                Channel[] input = new Channel[index-1];
                Channel[][] privateC = new Channel[index-1][];
                for(int i = 0; i < index-1; i++){
                    input[i] = inputChannels[i];
                    if(privateChannels != null){
                        privateC[i] = privateChannels[i];
                    }
                }
                return process.replyThenDie(input, index-1, privateC);
            }else{
                return process.replyThenDie(null, index-1, null);
            }

        }else{
            return nil;
        }
}

    // p | q - can do whatever p and q can
    public Process parallel(Process p, Process q) {//System.out.println("@@@@@@@@@@@");
        //to make sure the processes as the parameters of function parallel are legal
        if(p.getNameP().equals("Nil") && q.getNameP().equals("Nil")){
            return nil;
        }else if(!p.getNameP().equals("Nil") && q.getNameP().equals("Nil")){
            return p;
        }else if(p.getNameP().equals("Nil") && !q.getNameP().equals("Nil")){
            return q;
        }else{
            Process result = new Process();
            result.setNameP(p.getNameP() + "|" + q.getNameP());// set the name of the result process
            // P can do stuff and Q remains unchanged
            if(p.getNextSteps() !=  null){
                for (int i = 0; i < p.getNextSteps().length; i++) {
                    ActionAndProcess ap = p.getNextSteps()[i];
                    if(ap != null){
                        if(ap.getProcess() != null){
                            addNew(result.getNextSteps(), new ActionAndProcess(ap.getAction(), parallel(ap.getProcess(), q)));
                        }else{
                            if((i + 1) < p.getNextSteps().length ){
                                ActionAndProcess[] a = new ActionAndProcess[]{p.getNextSteps()[i+1]};
                                Process flag = new Process("", a, null);//System.out.println("!!!!!!!!!!!!!!"+ap.getAction());
                                addNew(result.getNextSteps(), new ActionAndProcess(ap.getAction(), parallel(flag, q)));
                            }
                        }
                    }
                }
            }

            // Q can do stuff and P remains unchanged
            if(q.getNextSteps() !=  null){
                for (int i = 0; i < q.getNextSteps().length; i++) {
                    ActionAndProcess aq = q.getNextSteps()[i];
                    if(aq != null){
                        if(aq.getProcess() != null){
                            addNew(result.getNextSteps(), new ActionAndProcess(aq.getAction(), parallel(p, aq.getProcess())));
                        }else{
                            if((i + 1) < q.getNextSteps().length ){
                                ActionAndProcess[] a = new ActionAndProcess[]{q.getNextSteps()[i+1]};
                                Process flag = new Process("", a, null);
                                addNew(result.getNextSteps(), new ActionAndProcess(aq.getAction(), parallel(p, flag)));
                            }
                        }
                    }
                }
            }

            // If P and Q match, they can also synchronise
            if(p.getNextSteps() != null && q.getNextSteps() != null){
                for (int i  =0; i < p.getNextSteps().length; i++) {
                    for (int j = 0; j < q.getNextSteps().length; j++){
                        ActionAndProcess ap = p.getNextSteps()[i];
                        ActionAndProcess aq = q.getNextSteps()[j];
                        if((ap != null) && (aq != null)){
                            if(Action.isMatch(ap.getAction(), aq.getAction())){
                                // P is inputting, Q is outputting
                                if (ap.getAction().isInput()) {
                                    // Q becomes Q', P becomes P', but its channels are renamed
                                    if((aq.getProcess() != null) && (ap.getAction().getPrivateChannels() != null) && (aq.getAction().getPrivateChannels() != null)){
                                        result.setNextSteps(concat(result.getNextSteps(), parallel(rename(p, ap.getAction().getPrivateChannels(), aq.getAction().getPrivateChannels()) ,
                                                aq.getProcess()).getNextSteps()));
                                    }else if((ap.getAction().getPrivateChannels() != null) && (aq.getAction().getPrivateChannels() != null)){
                                        ActionAndProcess[] a = new ActionAndProcess[]{q.getNextSteps()[i+1]};
                                        Process flag = new Process("", a, null);
                                        result.setNextSteps(concat(result.getNextSteps(), parallel(rename(p, ap.getAction().getPrivateChannels(), aq.getAction().getPrivateChannels()) ,
                                                flag).getNextSteps()));
                                    }
                                }// Q is inputting, P is outputting
                                else if (aq.getAction().isInput()) {
                                    // Q becomes Q', P becomes P', but its channels are renamed
                                    if((ap.getProcess() != null) && (ap.getAction().getPrivateChannels() != null) && (aq.getAction().getPrivateChannels() != null)){
                                        result.setNextSteps(concat(result.getNextSteps(), parallel(ap.getProcess(), rename(q, aq.getAction().getPrivateChannels(),
                                                ap.getAction().getPrivateChannels())).getNextSteps()));
                                    }else if((ap.getAction().getPrivateChannels() != null) && (aq.getAction().getPrivateChannels() != null)){
                                        ActionAndProcess[] a = new ActionAndProcess[]{p.getNextSteps()[i+1]};
                                        Process flag = new Process("", a, null);
                                        result.setNextSteps(concat(result.getNextSteps(), parallel(flag, rename(q, aq.getAction().getPrivateChannels(),
                                                ap.getAction().getPrivateChannels())).getNextSteps()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("The result: "+result.getNameP());
            return result;
        }
    }

    // p + q - make a choice between p and q
    public Process choice(Process p, Process q) {
        Process result = new Process();
        result.setNameP("(" + p.getNameP() + "+" + q.getNameP() + ")");
        result.setNextSteps(concat(p.getNextSteps(), q.getNextSteps()));
        return result;
    }

    // hide (== "new")
    public Channel hide() {
        /*ActionAndProcess[] aAndP = new ActionAndProcess[1];
        for(int j = 0; j < a.length; j++){
            a[j].getChannel().setBound(true);
            for(int i = 0; i < p.getNextSteps().length; i++){
                if(!p.getNextSteps()[i].getAction().getChannel().getName().equals(a[j].getChannel().getName())){
                    addNew(aAndP, p.getNextSteps()[i]);
                }
            }
        }
        p.setNextSteps(aAndP);*/
        //new random channels that make sure the process can communicate with each other
        Channel c = new Channel((char)(Math.random() * 26 + 'a')+"",2,true);
        c.setBound(true);
        return c;
    }

    // replicate  --  !P == P | !P
    public Process replicate(Process p) {
        return  parallel(p, replicate(p));
    }

    // rename  --  change the name of a channel
    public Process rename(Process p, Channel[] pInputChannel, Channel[] qOutputChannel) {
        for(int i = 0; i < pInputChannel.length; i++){
            //make a decision about if the input channels equals to the output channels
            if(!pInputChannel[i].equals(qOutputChannel[i])){
                //to modify the input channel
                for(int j = 0; j < p.getNextSteps().length; j++){
                    //change the related bounded names of actions
                    if(p.getNextSteps()[j].getAction().equals(pInputChannel[i]) && p.getNextSteps()[j].getAction().getChannel().isBound()==true){
                        p.getNextSteps()[j].getAction().getChannel().setName(qOutputChannel[i].getName());
                    }
                }
                //change the related channels of the action
                pInputChannel[i] = qOutputChannel[i];
            }
        }
        return p;
    }

}
