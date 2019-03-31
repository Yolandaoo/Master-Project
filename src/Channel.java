/**
 * This class is to implement the channels that processes communicate through
 */
public class Channel{
    private String name; //the name of a channel
    private int arity; // how many things are passed along it
    private boolean bound; //whether the name of the channel is bounded
    //the getter of name
    public String getName() {
        return name;
    }
    //the setter of name
    public void setName(String name) {
        this.name = name;
    }
    //the getter of arity
    public int getArity() {
        return arity;
    }
    //the setter of arity
    public void setArity(int arity) {
        this.arity = arity;
    }
    //whether or not is this name bounded
    public boolean isBound() {
        return bound;
    }
    //the setter of bound
    public void setBound(boolean bound) {
        this.bound = bound;
    }
    //the default constructor of class Channel
    Channel(){
        this.name = "";
        this.arity = 0;
        this.bound = false;
    }
    //the constructor of class Channel with parameters
    Channel(String n, int a, boolean b){
        this.name = n;
        this.arity = a;
        this.bound = b;
    }
}