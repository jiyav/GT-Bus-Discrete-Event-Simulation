import java.util.ArrayList;
import java.util.Map;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;

//BusStop is a child of the RouteNode abstract class
public class BusStop extends RouteNode {

    /*---------------------Instance variables unique to each Bus Stop instance---------------- */
    //has a String id that it inherits from RouteNode

    /*
     * This bus stop's popularity score as a probability between 0 and 1
     */
    private double popularityScore;

    /*
     * set to true when bus stop is being occupied by another bus, false when it is not occupied
     */
    private boolean isOccupied;

    /*
     * to track the number of passengers at the bus stop at a given time
     */
    private int numPassengers;

    /*
     * For each bus type that goes to this bus stop, tracks the time that it arrived
     * Useful data collection point to extract bus frequency info
     */
    public Map<String, ArrayList<Integer>> arrivalTimeMap = new HashMap<>(); //each stop will have its arrivalTimeMaps for each allowed bus
    
    /*
     * a map of all the passengers at the stop
     * Key: the bus type that the passenger is waiting for at the stop
     * Value: list of passengers waiting for that bus type
     */
    public Map<String, ArrayList<Passenger>> passenger = new HashMap<>(); //should hold the passenger's bus


    /*------------------Static/Class-Level Variables shared by all Bus Stop instances------------ 
     * These are reset/emptied at the end of each simulation so the previous values are not carried over
    */

    /*
     * A map of the allowed bus types that can go to this stop
     * Key is the specific Bus Stop instance, value is an arraylist of bus types (string values)
     * For example, the CRC bus stop allows the Red, Green, and Gold buses to run on it since those 
     * bus types include this bus stop in its route
     * 
     */
    public static Map<BusStop, ArrayList<String>> allowedBuses = new HashMap<>(); //each stop will have its allowed buses
    
    /*
     * Contains all the bus stops created in the Driver program, as an arraylist
     * Each time a new bus stop is created, the init() function must be invoked, and it will add that bus stop to this list
     */
    public static ArrayList<BusStop> allBusStops = new ArrayList<>();

    /*
     * Contains all the bus stops created in the Driver program, as a map. 
     * Key is the bus stop id, and the value is the Bus STop
     * Each time a new bus stop is created, the init() function must be invoked, and it will add the bus stop id and its corresponding bus instance to this map
     */
    public static Map<String, BusStop> allBusStopsMap = new HashMap<>(); 

    /*
     * An arraylist of all of the bus instances popularity scores. 
     * init() will add the most recently created bus stop's popularity score to this list
     * the indices in allPopularityScores correspond with the indices in 'allBusStops' list 
     */
    public static ArrayList<Double> allPopularityScores = new ArrayList<>();
    
    /*-----------------------------------------constructors-------------------------------------- */
    

    /*
     * Constructor that assigns the bus stop's id and popularity score
     * number of passengers at a bus stop is set to 0
     */
    public BusStop(String id, double popularityScore) {
        super(id);
        this.popularityScore = popularityScore;
        numPassengers = 0;   
    }

    /*-------------------------------------inits and utility methods----------------------------- */
    

    /*
     * Must be invoked each time a bus stop is created in the Driver program
     * - Adds the bus stop instance to Bus Stop class's 'allBusStops' arraylist,
     *  'allBusStopsMap' hashMap, 'allowedBuses' hashMap, 'allBusStops', and 
     * 'allPopularityScores' static variables
     * 
     */
    public void init() {
        allBusStopsMap.put(this.id, this);
        allowedBuses.put(this, new ArrayList<>());
        allBusStops.add(this);
        allPopularityScores.add(this.popularityScore); 

    }

    /*
     * after a passenger is created, adds the passenger to this bus stop instance's 'passenger' map
     * Also incremements numPassengers of the bus stop
     */
    public void addPassenger(Passenger p) {
       passenger.get(p.getBus()).add(p); 
       numPassengers++;
    }

    /*-----------------------------getters and setters------------------------------------ */

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String,ArrayList<Integer>> getArrivalTimeMap() {
        return this.arrivalTimeMap;
    }

    public void setArrivalTimeMap(Map<String,ArrayList<Integer>> arrivalTimeMap) {
        this.arrivalTimeMap = arrivalTimeMap;
    }

    public boolean isIsOccupied() {
        return this.isOccupied;
    }

    public boolean getIsOccupied() {
        return this.isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }



    public static void setAllPopularityScores(ArrayList<Double> allPopularityScores) {
        BusStop.allPopularityScores = allPopularityScores;
    }

    public static ArrayList<Double> getAllPopularityScores() {
        return allPopularityScores;
    }

    public static Map<String, BusStop> getAllBusStopsMap() {
        return allBusStopsMap;
    }

    public static void setAllBusStopsMap(Map<String, BusStop> allBusStopsMap) {
        BusStop.allBusStopsMap = allBusStopsMap;
    }

    public static ArrayList<BusStop> getAllBusStops() {
        return allBusStops;
    }

    public static void setAllBusStops(ArrayList<BusStop> allBuses) {
        BusStop.allBusStops = allBuses;
    }

    public void setNumPassengers(int numPassengers) {
        this.numPassengers = numPassengers;
    }

    public int getNumPassengers() {
        return numPassengers;
    }


    public void setPopularityScore(double popularityScore) {
        this.popularityScore = popularityScore;
    }

    public double getPopularityScore() {
        return popularityScore;
    }
    
    public void setarrivalTimeMaps(Map<String, ArrayList<Integer>> arrivalTimeMap) {
        this.arrivalTimeMap = arrivalTimeMap;
    }

    public Map<String, ArrayList<Integer>> getarrivalTimeMaps() {
        return arrivalTimeMap;
    }

    public static void setAllowedBuses(Map<String, ArrayList<RouteNode>> routes) {
        //System.out.println(routes);
        for (Map.Entry<String, ArrayList<RouteNode>> entry : routes.entrySet()) {
            String value = entry.getKey();
            //System.out.println(value);
            ArrayList<RouteNode> route = entry.getValue();
            //System.out.println(route.toString());
            ArrayList<String> arr = new ArrayList<>();
            BusStop bs = null;
            for (int i = 0; i < route.size(); i++) {
                if (route.get(i) instanceof BusStop) {
                    bs = (BusStop)route.get(i);
                    if (allowedBuses.get(bs) == null) {
                        allowedBuses.put(bs, new ArrayList<>());
                    } else {
                        arr = allowedBuses.get(bs);
                        arr.add(entry.getKey());
                        allowedBuses.put(bs, arr);
                    }
                    //arr.add(entry.getKey());//add this bus's type (from routes) to the stop's list
                    //put this key value pair into bus stop map          
                }
            }

            //allowedBuses.put((BusStop)bs, arr);
        
        }
    }

    public static Map<BusStop, ArrayList<String>> getAllowedBuses() {
        // for (Map.Entry<BusStop, ArrayList<String>> entry : allowedBuses.entrySet()) {
        //     BusStop key = entry.getKey();
        //     ArrayList<String> value = entry.getValue();
        //     System.out.print("For this bus stop, " + key.getId() + " these are the allowed bus types:");
        //     for (int i = 0; i < value.size(); i++) {
        //         System.out.print(value.get(i)+ ", ");
        //     }
        //     System.out.println();
            
        // }
        return allowedBuses;
    }

    public Map<String,ArrayList<Passenger>> getPassenger() {
        return this.passenger;
    }

    public void setPassenger(Map<String,ArrayList<Passenger>> passenger) {
        this.passenger = passenger;
    }

    
}