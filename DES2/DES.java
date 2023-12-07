import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.Comparator;

/*Example of Simulation Driver program. 
 * compile all *.java files in the github using 'javac *.java'. Then run DES.java using 'java DES.java'
*/
public class DES {
    public static void main(String[] args) {
        //set up bus stops, road segments, and traffic lights. make sure to invoke init() for each stop
        BusStop stopA = new BusStop("S1", 0.3);
        stopA.init();
        BusStop stopB = new BusStop("S2", 0.2);
        stopB.init();
        BusStop stopC = new BusStop("S3", 0.6);
        stopC.init();
        RoadSegment r1 = new RoadSegment("R1", 0.7);
        RoadSegment r2 = new RoadSegment("R2", 0.6);
        RoadSegment r3 = new RoadSegment("R3", 0.3);
        RoadSegment r4 = new RoadSegment("R4", 0.3);
        RoadSegment r5 = new RoadSegment("R5", 0.5);
        RoadSegment r6 = new RoadSegment("R6", 0.6);
        TrafficLight t1 = new TrafficLight("T1");
        TrafficLight t2 = new TrafficLight("T2");
        TrafficLight t3 = new TrafficLight("T3");

        //set up routes such that there is a road segment alternating between other nodes
        ArrayList<RouteNode> blueRoute = new ArrayList<>(Arrays.asList(stopA, r1, t1, r2, stopB, r3, t2, r4, stopC, r5, t3, r6));
        ArrayList<RouteNode> redRoute = new ArrayList<>(Arrays.asList(stopC, r4, t2, r3, stopB, r2, t1, r1, stopA, r6, t3, r5));
        Map<String, ArrayList<RouteNode>> routes = new HashMap<>(); 
        routes.put("Blue", blueRoute);
        routes.put("Red", redRoute);
        Bus.setRoutes(routes);

        //invoke the Bus utility setBusStops() method
        Bus.setBusStops();

        //invoke the Bus Stop utility setAllowedBuses() method
        BusStop.setAllowedBuses(routes);  
        
        //create buses. make sure to invoke init() on each bus
        Bus bus1 = new Bus("bluebus1","Blue", blueRoute);
        bus1.init(bus1.getBusType());
        Bus bus2 = new Bus("redbus1", "Red", redRoute);
        bus2.init(bus2.getBusType());
        Bus bus3 = new Bus("redbus2", "Red", redRoute);
        bus3.init(bus3.getBusType());
        
        //create road traffic index map
        Map<Integer, Double> trafficIndex = new HashMap<Integer, Double>(); 
            trafficIndex.put(7, 0.0);//7am
            trafficIndex.put(8, 0.5);
            trafficIndex.put(9, 0.5);
            trafficIndex.put(10, 1.0);
            trafficIndex.put(11, 1.0);
            trafficIndex.put(12, 1.0);
            trafficIndex.put(13, 1.0);
            trafficIndex.put(14, 0.5);
            trafficIndex.put(15, 0.5);
            trafficIndex.put(16, 1.0);
            trafficIndex.put(17, 2.0);
            trafficIndex.put(18, 2.5);
            trafficIndex.put(19, 2.5);//7pm    
        RoadSegment.setTrafficCond(trafficIndex);

        //create passenger traffic map
        Map<Integer, Integer> passengerTraffic = new HashMap<>(); 
            passengerTraffic.put(7, 10);//7am
            passengerTraffic.put(8, 10);
            passengerTraffic.put(9, 10);
            passengerTraffic.put(10, 15);
            passengerTraffic.put(11, 15);
            passengerTraffic.put(12, 10);
            passengerTraffic.put(13, 10);
            passengerTraffic.put(14, 10);
            passengerTraffic.put(15, 15);
            passengerTraffic.put(16, 20);
            passengerTraffic.put(17, 10);
            passengerTraffic.put(18, 15);
            passengerTraffic.put(19, 10);//7pm    
            Passenger.setPassengerTraffic(passengerTraffic);

            //start time is 7am, so @ 420 minutes
            int time = 420;

            //let's invoke the createPassengers() method to inject a decent amount of passengers at the
            //start of the simulaton
            ArrayList<BusStop> allBusStops = BusStop.getAllBusStops();
            for (int i = 0; i < allBusStops.size(); i++) {
                ArriveAtBusStopEvent.createPassengers(time);
            }

            // for (int j = 0; j < allBusStops.size(); j++) {
            //     Map<String, ArrayList<Passenger>> initialPass = allBusStops.get(j).passenger;

                // for (Map.Entry<String, ArrayList<Passenger>> entry : initialPass.entrySet()) {
                //     String busType = entry.getKey();
                //     ArrayList<Passenger> value = entry.getValue();
                //     System.out.print("For this bus stop, " + allBusStops.get(j).getId() + " these are Passengers ");
                //     for (int i = 0; i < value.size(); i++) {
                //         System.out.println("Passenger with ID " + value.get(i).getId() + "riding on Bus " + value.get(i).getBus());
                //     }
                //     System.out.println();
                    
                // }

           // }


           //schedule an ArriveAtBusStopEvent for each Bus
            System.out.println(Bus.getAllBuses().get(0));
            for (int i = 0; i < Bus.getAllBuses().size(); i++) {
                System.out.println(Bus.getAllBuses().get(i).getId()+ "\'s FIRST STOP ASSIGNED: " + Bus.getAllBuses().get(i).getCurrStop());
                SimulationExecutive.schedule(new ArriveAtBusStopEvent(Bus.getAllBuses().get(i), (BusStop) Bus.getAllBuses().get(i).getCurrStop(), time));
            }


            //run the simulation
            SimulationExecutive.simulation();

            //export data into a .csv file
            DataExport.writeBusStopDataToCSV(allBusStops, "BusStops.csv");   
            ArrayList<ArrayList<Passenger>> deadPassengers = Passenger.getDeadPassengers();
            DataExport.writePassengerDataToCSV(deadPassengers, "Passengers.csv");

            RouteVisualizer.visualizeRoute(redRoute, "Red Route", Bus.getAllBuses().size());

    }
 }