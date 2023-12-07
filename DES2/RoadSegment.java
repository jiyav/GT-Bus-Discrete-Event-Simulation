import java.util.HashMap;
import java.util.Map;

//RoadSegement is a child of RouteNode
public class RoadSegment extends RouteNode {
    //inherits id instance variable from RouteNode abstract class

    /*
     * distance of the road segment in miles
     */
    
    private double distance;

    /*
     * all road segments in any bus route share the same speed limit of 15 miles/hour
     */
    private static int speedLimit = 15;

    /*
    * key: hour of the day
    * value: traffic index at that hour. Normally, time to travel a road segment is 
    * is just speedLimit/distance, but 
    * tells createPassengers() how many passengers to generate based on the current hour range the 
    * simulation clock time is in
    * 
    * passengers are NOT created at an hourly basis, but are rather created/spawned every time an
    * ArriveAtBusStopEvent is processed, and the # of passengers generated are based on this map
    * 
    * a higher value for an hour key would inject passengers at a greater density
    * into the simulation in that hour
    */
    private static Map<Integer, Double> trafficCond = new HashMap<>();

    /*--------------------------------------------constructor-------------------------------------- */
    public RoadSegment(String id, double distance) {
        super(id);
        this.distance = distance;
    }
    
    //no init() or utility functions in RoadSegment.java


    /*-------------------------------getters and setters--------------------------------- */

    public static Map<Integer, Double> getTrafficCond() {
        return trafficCond;
    }

    public static void setTrafficCond(Map<Integer, Double> trafficCond) {
        RoadSegment.trafficCond = trafficCond;
    }


    public static int getSpeedLimit() {
        return speedLimit;
    }

    public static void setSpeedLimit(int speedLimit) {
        RoadSegment.speedLimit = speedLimit;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}