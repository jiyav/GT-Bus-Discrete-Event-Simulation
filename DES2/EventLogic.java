/*import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

@Deprecated
/*
 * this file is deprecated, all event functions have been converted to event classes, which 
 * handle the corresponding logic within the class
 * 
 * next step is event scheduling with Event objects, time, scheduler, and future event list/priority queues
 * 
 * 
 */
/*
public class EventLogic {
    //in place of schedule, we can just pass in future event list and push it to the list ourselves
    public static void stopAtTrafficLightEvent(Bus b, TrafficLight t, int stime) {
        updateStop(b);//update bus's next stop pointer
        if (t.isGreenLight()) {
            //we can drive immedietly! don't even have to wait
            schedule(driveOnRoadSegmentEvent(b, (RoadSegment)b.getCurrStop(), stime));
                
        } else {
            //have to wait at the light before driving. next stop guarenteed to be road
            schedule(driveOnRoadSegmentEvent(b, (RoadSegment)b.getCurrStop(), stime + t.getWaitTime()));
        }
        //update traffic light's state for the next bus
        if((int) Math.round(Math.random()) == 0) {
            t.setGreenLight(false);
            int randomNumber = (int) (1 + Math.random() * (3 - 1 + 1));
            t.setWaitTime(randomNumber);
            //next time a bus reaches this light, it'll have to wait bw 1-5 minutes
            //ideally we set this waittime based on traffic conditions based on the stime that
            //we predefine in the sim driver. for now, let's keep it simple w a rand num gen
        } else {
            t.setGreenLight(true);
            t.setWaitTime(0);
        }
    }

    public static void driveOnRoadSegmentEvent(Bus b, RoadSegment r, int stime) {
        //update bus's next pointer
        updateStop(b);
        b.setSpeed(r.getSpeedLimit() * r.getTraffic());//doesn't rly matter
        //(d in miles/v in miles/hr * 60 to convert to minutes) + add traffic delay in minutes as well
        int drivingTime = (int)(((r.getDistance() / r.getSpeedLimit()) * 60) + r.getTraffic());
        RouteNode nextStop = b.getCurrStop();
        if (nextStop instanceof BusStop) {
            schedule(arriveAtBusStop(b, (BusStop) nextStop, stime + drivingTime));
        } else {
            schedule(stopAtTrafficLightEvent(b, (TrafficLight) nextStop, stime + drivingTime));
        }

        //update conditions of the traffic on the road according to a static variable that holds
        //traffic conditions for each hour. See what "hour" range we are in the simulation, and
        //set the traffic according to that!
        
        int currentHour = (int) (stime/60) - startHour; //need to keep track of startHour
        r.setTraffic(RoadSegment.getTrafficCond().get(currentHour));

    }

    public static void arriveAtBusStop(Bus b, BusStop s, int stime) {
        //where all the passenger exchange happens!

        //held in busStop info. given a busStop's passenger list, extract the passengers boarding this bus
        Map<String, ArrayList<Passenger>> busPassengers = b.getPassengers();
        ArrayList<Passenger> departingPassengers = busPassengers.get(s.getId());
        int numOfPassDeparting = departingPassengers.size();
        
        Map<String,ArrayList<Passenger>> stopPassengers = s.getPassenger();
        ArrayList<Passenger> onboardingPassengers = stopPassengers.get(b.getBusType());
        int numOfPassBoarding = onboardingPassengers.size();

        double exchangeTime = numOfPassBoarding * 0.5 + numOfPassDeparting * 0.5;

        deletePassengers(departingPassengers, b, s, stime);
        passengerOnBoard(onboardingPassengers, b, s, stime);
        

    }

    public static void driverBreakEvent(Bus b, int stime) {

    }

    private static void updateStop(Bus b) {
        //save the next stop 
        RouteNode nextStop = null; 
        int nextIndex = -1;
        nextIndex = (b.getCurrStopIndex() + 1)%Bus.getRoutes().get(b.getBusType()).length;
        nextStop = Bus.getRoutes().get(b.getBusType())[nextIndex];
        b.setCurrStop(nextStop);
        b.setCurrStopIndex(nextIndex);
        
    }


    public static void createPassengers(BusStop b, int stime) {
        int num = Passenger.getPassengerTraffic().get((int)stime/60);
        for (int i = 0; i < num; i++) {
            //pick a starting bus stop according to bus popularity score
            BusStop chosenStart = selectBusStop(BusStop.getAllBusStops(), BusStop.getAllPopularityScores());
            ArrayList<String> allowedBuses = BusStop.getAllowedBuses().get(b);
            //implement utility method for these rand index/value generators
            Random rand = new Random(20);
            int k = rand.nextInt(allowedBuses.size());
            String chosenBus = allowedBuses.get(k);
            //pick destinaton. but wait!! dest MUST be a busStop, not just any RouteNode obj
            //assume dest is always diff from source
            //
            RouteNode[] busRoute = Bus.getRoutes().get(chosenBus);
            //given a bus color, i need to find all the stops on that bus's route when prompted
            //rn let's just put logic in this method, but need to create a utility method in Bus class
            //so we only have to do this once and not for all of the hundreds of passengers
            ArrayList<BusStop> allowedBusStops = null;
            ArrayList<Double> popularity = null;
            for (int j = 0; j < busRoute.length; j++) {
                if (busRoute[j] instanceof BusStop && busRoute[j].getId().equals(b.getId())) {
                    allowedBusStops.add((BusStop)busRoute[i]);
                    //get the Bus Stop most recently added to allowedbuses, access this bus stop's pop score
                    popularity.add(allowedBusStops.get(allowedBusStops.size() - 1).getPopularityScore());
                }
            }
            //wait but I can't just create a new Bus stop to the simulation, i need to extract find this bus stop from the created ones and then input that one
            BusStop chosenStop = selectBusStop(allowedBusStops, popularity);
            Passenger p = new Passenger(chosenStart, chosenStop, stime, chosenBus);
            //okay so a passenger has been created, not we have to add this passenger to the 
            //starting bus stop's passenger list
            b.addPassenger(p); //has to increment num passengers in bus stop as well
        }
    }

    public static void deletePassengers(ArrayList<Passenger> p, Bus b, BusStop s, int stime) {
        for (int i = 0; i < p.size(); i++) {
            p.get(i).setHasArrived(true);
            p.get(i).setTimeOfDepartureFromBus(stime); //update departure time to all passengers
        }
        Passenger.addToDeadPassengerList(p); //we've saved these passengers somewhere. 
        //now i want to empty the bus's arraylist of passengers getting off at this stop.
        //do we clear() or just null the reference? we still want those dead passengers alive in memory
        //just no longer accessible by the simulation
        Map<String, ArrayList<Passenger>> map = b.getPassengers();
        map.put(s.getId(), null);
        b.setPassengers(map); 
        //update bus's capacity right now. there are now p new empty seats!
        b.setEmptySeats(b.getEmptySeats() + p.size());

    }

    public static void passengerOnBoard(ArrayList<Passenger> onboardingPassengers, Bus b, BusStop s,    
                                        int stime) {
        ArrayList<Passenger> allowedPassengers = null;
        ArrayList<Passenger> heldBack = null;

        if (onboardingPassengers.size() > b.CAPACITY) {
            allowedPassengers = (ArrayList<Passenger>)onboardingPassengers.subList(0, b.getEmptySeats());
            heldBack = (ArrayList<Passenger>)onboardingPassengers.subList(b.getEmptySeats(), onboardingPassengers.size());
        } else {
            allowedPassengers = onboardingPassengers;
        }                              
        
        for (int i = 0; i < allowedPassengers.size(); i++) {
            allowedPassengers.get(i).setTimeOfGettingOnBus(stime); //update onboarding time on selected passengers
        }

        //add passenger onto bus
        Map<String, ArrayList<Passenger>> map = b.getPassengers();
        map.put(s.getId(), allowedPassengers);
        b.setPassengers(map); 
        //update bus's capacity right now. there are now p less seats! keep in mind this here might
        //cause some index out of bounds error if indexing in splitting the lists is off
        b.setEmptySeats(b.getEmptySeats() - allowedPassengers.size());

        //remove passengers at stop. basically just set the bus key to the held back list
        Map<String, ArrayList<Passenger>> stopMap = s.getPassenger();
        stopMap.put(s.getId(), heldBack);
        s.setPassenger(stopMap); 
        s.setNumPassengers(s.getNumPassengers() - allowedPassengers.size());

        //also update the time the bus type arrived at this stop
        Map<String, ArrayList<Integer>> arrivalMap = s.getarrivalTimeMaps();
        ArrayList<Integer> list = arrivalMap.get(b.getId());
        list.add(stime);
        s.setarrivalTimeMaps(arrivalMap);

        //create new passengers


    }

    //create num (randomly generated) passengers at a given time. randomly select bus, and end dest
    public static BusStop selectBusStop(ArrayList<BusStop> busStop, ArrayList<Double> popularityScore) { 
        double randomValue = Math.random();
        double[] cumulativeProbabilities = new double[popularityScore.size()];
        cumulativeProbabilities[0] = popularityScore.get(0);
        for (int i = 1; i < popularityScore.size(); i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + popularityScore.get(i);
        }
        BusStop selectedElement = null;
        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (randomValue <= cumulativeProbabilities[i]) {
                selectedElement = busStop.get(i);
                 break;
            }
        }
        System.out.println("Randomly selected element: " + selectedElement.getId());
        return selectedElement;
    }
}
        


        
       


        

    }
}
*/
