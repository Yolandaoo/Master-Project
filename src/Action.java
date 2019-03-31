/**
 * This class is to implement the actions that trigger different processes and input or out put channels
 */
public class Action {
    private Channel channel; //the channel of a action
    private Direction direction; //the direction of the action
    private Channel[] privateChannels; //the channel array of the action, which stores the channels pass through the action
    //the getter of direction
    public Direction getDirection() {
        return direction;
    }
    //the setter of direction
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    //the getter of channel
    public Channel getChannel() {
        return channel;
    }
    //the setter of channel
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    //the getter of the private channels' array
    public Channel[] getPrivateChannels() {
        return privateChannels;
    }
    //the setter of the private channels' array
    public void setPrivateChannels(Channel[] privateChannels) {
        this.privateChannels = privateChannels;
    }
    //the default constructor of class Action
    Action(){
        this.channel = new Channel();
        this.direction = Direction.TAU;
        this.privateChannels = null;
    }
    //the constructor of class Action with parameters
    Action(Channel c, Direction d, Channel[] p){
        this.channel = c;
        this.direction = d;
        this.privateChannels = p;
    }
    //a function to make a judgement that whether the direction of this action is "IN"
    public boolean isInput(){
        if(this != null){
            if(this.getDirection().equals(Direction.IN)){//System.out.println("!!!!!!!!!!this: "+this.getChannel().getName());
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }
    //a function to make a judgement that whether the direction of this action is "OUT"
    public boolean isOutput(){
        if(this != null){
            return (this.getDirection().equals(Direction.OUT));
        }else{
            return false;
        }
    }
    //a function to make a judgement that whether the direction of this action is "TAU"
    public boolean isTau(){
        if(this != null){
            return (this.getDirection().equals(Direction.TAU));
        }else{
            return false;
        }
    }

    // one is an input and the other is a matching output
    public static boolean isMatch(Action a, Action b) {
        // should be the same channel
        if((a != null) && (b != null)){
            if(a.getChannel().getName() != b.getChannel().getName())
                return false;
            // should be one input and one output
            if((a.isInput() && b.isOutput()) || (a.isOutput() && b.isInput())){
                // should refer to the same number of channels being transmitted
                return (a.getChannel().getArity() == b.getChannel().getArity());
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

}
