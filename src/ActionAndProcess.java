/**
 * This class is the connection between action and process
 */
public class ActionAndProcess {
    private Action action; //the relative action
    private Process process; //the relative process
    //the getter of action
    public Action getAction() {
        return action;
    }
    //the setter of action
    public void setAction(Action action) {
        this.action = action;
    }
    //the getter of process
    public Process getProcess() {
        return process;
    }
    //the setter of process
    public void setProcess(Process process) {
        this.process = process;
    }
    //the default constructor of class ActionAndProcess
    ActionAndProcess(){
        this.action = new Action();
        this.process = new Process();
    }
    //the constructor of class ActionAndProcesswith parameters
    ActionAndProcess(Action a, Process p){
        this.action = a;
        this.process = p;
    }
}
