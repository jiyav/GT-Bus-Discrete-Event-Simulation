import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashMap;


public class Bus {

    /*---------------------Instance variables unique to each Bus instance---------------- */

    /*
     * The bus's unique id/name. Ex. "redbus1", "bluebus2", etc. 
     * Specified in the constructor as a String value, should be made unique to distinguish buses.
     */
    private String id;

    /* Contains the passengers currently on board a bus. Organized as a map 
     * The keys are the bus stops on the bus's route (represented by their String id)
     * The value is an arraylist of passengers on board who need to get off at that stop
     */
    public Map<String, ArrayList<Passenger>> passengers; 

    /* The bus's current speed.
     */
    private double speed;

    /* Keeps track of the number of loops/cycles of the route a bus has completed
     * Used to allocate breaks to the bus driver 
     * Is reset to 0 after a driver takes a break
     */
    private int numLoops; 

    /* The number of minutes a driver will take a break for
     * Can be specified in the Bus constructor, or set to a default value
     */
    private int breakTime;

    /* After now many loops/cycle around a route a driver can take his/her break
     * Can be specified in the Bus constructor, or set to default value
     */
    private int breakAfterNumLoops;

    /*
     * The bus's assigned route. The route is created in the simulation driver/environment 
     * (ex. DES.java, DESNumBusesNoTraffic.java, etc) and then assigned to the bus via init()
     * The bus uses this arraylist to keep track of where it is in the route and where to go next
     */
    private ArrayList<RouteNode> route; 

    /* The current RouteNode (Bus Stop, Traffic Light, or Road Segment) the bus is on 
     * from its assigned 'route'
     */
    private RouteNode currStop;

    /* The current index of 'route' that the Bus is on
     */
    private int currStopIndex;

    /*
     * The bus's capacity
     */
    public final int CAPACITY = 20;
    
    /*
     * The type of bus corresponds to the route it takes. (ex, Red, Blue, etc). 
     * Buses will have unique ids, but can share the same busType if there are multiple buses
     * operating on the same route
     */
    private String busType;

    /*
     * The number of empty seats on the bus
     */
    private int emptySeats;

    /*------------------Static/Class-Level Variables shared by all Bus instances------------ 
     * These are reset/emptied at the end of each simulation so the previous values are not carried over
    */

    /*
     * Contains all the bus routes (all RouteNodes) created in the Driver program
     */
    private static Map<String, ArrayList<RouteNode>> routes = new HashMap<>(); 

    /*
     * Contains only the bus stops from each route created in the Driver program
     */
    private static Map<String, ArrayList<BusStop>> busStops = new HashMap<>();

    /*
     * Contains all the buses created in the Driver program, as an arraylist
     * Each time a new bus is created, the init() function must be invoked, and it will add that bus to this list
     */
    public static ArrayList<Bus> allBuses = new ArrayList<>();

    /*
     * Contains all the buses created in the Driver program, as a map. 
     * Key is the bus id, and the value is the Bus
     * Each time a new bus is created, the init() function must be invoked, and it will add the bus id and its corresponding bus instance to this map
     */
    public static Map<String, Bus> allBusesMap = new HashMap<>();
    
    /*-----------------------------------------constructors-------------------------------------- */

    /*
     * Bus constructor
     * Defines the bus instances's id, busType, route, breakTime, breakAfterNumLoops values
     * speed and numLoops initially set to 0 for every bus instance
     * emptySeats initially set to CAPACITY since no passengers are on board yet
     * passengers is set to an empty hashMap since no passengers have boarded yet
     */
    public Bus(String id, String busType, ArrayList<RouteNode> route, 
                                int breakTime, int breakAfterNumLoops) {
        this.id = id;
        this.busType = busType;
        this.route = route;
        this.breakTime = breakTime;
        this.breakAfterNumLoops = breakAfterNumLoops;                  
        speed = 0;
        numLoops = 0;
        emptySeats = CAPACITY;
        passengers = new HashMap<>();
        
    }

    /*
     * Bus constructor
     * Same as above, but defaults breakTime to 5 minutes after every 1 loop
     */
    public Bus(String id, String busType, ArrayList<RouteNode> route) {
        this(id, busType, route, 5, 1);
        speed = 0;
        numLoops = 0;
        emptySeats = CAPACITY;
        passengers = new HashMap<>();
    }

    /*-------------------------------------inits and utility methods----------------------------- */
    
    /*
     * Must be invoked each time a bus is created in the Driver program
     * - Places the bus at its starting bus stop by initializing currStop and currStopIndex to a 
     * bus stop selected via the selectRandomBusStop() methods.
     * - Also adds the bus instance to Bus class's 'allBuses' arraylist and 'allBusesMap' hashmap static variables
     * 
     */
    public void init(String busType) {
        int randomBusStopIndex = selectRandomBusStop(busType);
        this.currStop = selectRandomBusStop(randomBusStopIndex, busType);
        System.out.println("Bus " + this + "\'s current stop index: " + route.indexOf(this.currStop) + " lies the stop " + this.currStop);
        this.currStopIndex = route.indexOf(this.currStop);
        allBuses.add(this);
        allBusesMap.put(this.id, this); 
    }

    /*
     * Static/utility method used to populate 'busStop' 
     * Essentially extracts only the BusStop objects from the route of all RouteNodes
     * Is invoked in the Driver program after routes is created and initialized
     */
    public static void setBusStops() {
        //busStops = new HashMap<>();
        for (Map.Entry<String, ArrayList<RouteNode>> entry : routes.entrySet()) {
            ArrayList<RouteNode> route = entry.getValue();
            ArrayList<BusStop> arr = new ArrayList<>();
            for (int i = 0; i < route.size(); i++) {
                if (route.get(i) instanceof BusStop) {
                    arr.add((BusStop)route.get(i));
                }
            }
            busStops.put(entry.getKey(), arr);
        }

        // print statements to help verify setBusStops() functions properly
        // for (Map.Entry<String, ArrayList<BusStop>> entry : busStops.entrySet()) {
        //     String key = entry.getKey();
        //     ArrayList<BusStop> value = entry.getValue();
        //     System.out.print("For this Bus Type " + key + " these are the allowed Bus Stops ");
        //     for (int i = 0; i < value.size(); i++) {
        //         System.out.print(value.get(i)+ ", ");
        //     }
        //     System.out.println("Next bus type ");
            
        // }

    }

    /*
     * selects and returns random index in of the specified route in 'busStop'
     * invoked in init()
     */
    public static int selectRandomBusStop(String whichRoute) {
        Random rand1 = new Random();
        ArrayList<BusStop> stops = busStops.get(whichRoute);
        int num = rand1.nextInt(stops.size());
        return num;
    }

    /*
     * given a bus stop index from the above function, this function returns the actual Bus Stop object
     * invoked in init()
     */
    public static BusStop selectRandomBusStop(int randomBusStopIndex, String whichRoute) {
        return busStops.get(whichRoute).get(randomBusStopIndex);

    }



    /*---------------------------------getters and setters----------------------------------- */

    public int getBreakTime() {
        return this.breakTime;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public int getBreakAfterNumLoops() {
        return this.breakAfterNumLoops;
    }

    public void setBreakAfterNumLoops(int breakAfterNumLoops) {
        this.breakAfterNumLoops = breakAfterNumLoops;
    }



    public int getNumLoops() {
        return this.numLoops;
    }

    public void setNumLoops(int numLoops) {
        this.numLoops = numLoops;
    }


    public int getCAPACITY() {
        return this.CAPACITY;
    }


    public ArrayList<RouteNode> getRoute() {
        return this.route;
    }

    public void setRoute(ArrayList<RouteNode> route) {
        this.route = route;
    }

    
    public static ArrayList<Bus> getAllBuses() {
        return allBuses;
    }

    public void setEmptySeats(int emptySeats) {
        this.emptySeats = emptySeats;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public static Map<String, ArrayList<BusStop>> getBusStops() {
        return busStops;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusType() {
        return busType;
    }
    public static void setRoutes(Map<String, ArrayList<RouteNode>> routes) {
        Bus.routes = routes;
    }
    public static Map<String, ArrayList<RouteNode>> getRoutes() {
        return routes;
    }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String,ArrayList<Passenger>> getPassengers() {
        return this.passengers;
    }

    public void setPassengers(Map<String,ArrayList<Passenger>> passengers) {
        this.passengers = passengers;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public RouteNode getCurrStop() {
        return this.currStop;
    }

    public void setCurrStop(RouteNode currStop) {
        this.currStop = currStop;
    }

    public int getCurrStopIndex() {
        return currStopIndex;
    }

    public void setCurrStopIndex(int currStopIndex) {
        this.currStopIndex = currStopIndex;
    }

    



    
}
        
