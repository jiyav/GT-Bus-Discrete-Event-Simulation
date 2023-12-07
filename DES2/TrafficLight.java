
//TrafficLight extends the RouteNode abstract class
public class TrafficLight extends RouteNode{
    /*NOTE: traffic light's only purpose is to add time delays to reflect real world conditions
     * as close as possible. It does not change the state of passengers in any way, other than 
     * adding time to their total travel time
     * 
    */

    //true is the light is green, false if red
    private boolean greenLight; 

    /*0 minutes if light is green, or a randomly assigned number between 1-2 minutes if light is red 
    * wait time is assigned in the StopAtTrafficLightEvent.java
    */
    private int waitTime; 
    
    //constructor. assigns the traffic light a unique String id
    public TrafficLight(String id) {
        super(id);
    }
    
    // public TrafficLight(boolean greenLight, String id) {
    //     super(id);
    //     this.greenLight = greenLight;
        
    //     if (!greenLight) {
    //         waitTime = (int) (Math.random() * (2 - 1) + 1);  
    //     }
       
    // }

    /*----------------------------------getters and setters----------------------------- */
    public boolean isGreenLight() {
        return this.greenLight;
    }

    public boolean getGreenLight() {
        return this.greenLight;
    }

    public void setGreenLight(boolean greenLight) {
        this.greenLight = greenLight;
    }

    public void setWaitTime(double waitTime) { 
        this.waitTime = (int) (Math.random() * (2 - 1) + 1); 
    }

    public int getWaitTime() {
        return waitTime;
    }

}