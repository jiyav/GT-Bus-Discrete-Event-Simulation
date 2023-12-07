import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

//Note: see Event.java for more info on event handling
//ArriveAtBusStopEvent objects inherit methods and instance variables from Event
public class ArriveAtBusStopEvent extends Event {
    private BusStop s;
    /*
     * Constructor is passed in the Bus instance that needs to arrive, the Bus Stop
     * instance it needs to arrive at, as well as the time it'll arrive. 
     */
    public ArriveAtBusStopEvent(Bus b, BusStop s, int time) {
        super(time, b);
        this.s = s;
        this.time = time;
    }

    /* Overriden method
     * Called when an event is selected from future event list and processed by the simulation loop
     * First, it'll extract the the passengers aboard the bus that need to get off at this stop
     * as well as the passengers waiting at the bus stop that need to get on the stop
     * We assume that is takes 20 seconds for each person to onboard or offboard. Thus, the total time
     * a bus is required to occupy a stop is 20 seconds * (number of offboarding passengers + number
     * of onboarding passengers). We'll calculate these values and set them aside for later
     * 
     * Now we must check is the bus has reached its break time depending if it has made its 
     * set number of rounds based on a Bus's numLoops field. If so, delete passengers who need
     * to get off at this stop, and force remaining bus passengers to take a detour, and
     * schedule a DriverBreakEvent @ 'timeToDepart' minutes in the future. We disregard if the stop
     * is occupied or not, because this is a special scenario where a bus does not have to 
     * offboard directly in front of the bus stop bay, it can pull up behind the currently offboarding
     * bus and offboard there instead. 
     * 
     * Otherwise, if the stop is occupied by another bus and there is NO driver break to be had right
     * now, reschedule this event to occur later at some estimated time in the future
     * 
     * If the stop is free, then we can safely arrive. Once we arrive, we'll delete the offboarding passengers via deletePassenger() and make
     * sure to save their states. Then, we'll onboard waiting passengers via passengerOnBoard(). 
     * This method will also return the a smaller subset of onboarding passengers in case bus 
     * capacity is reached. We'll recalculate exchangeTime just in case we reached capacity.
     * Since we presumeably removed some passengers from the simulation (assuming there were passengers
     * in the bus who needed to get off at this stop), now it a good time to inject some more 
     * into the simulation using the createPassengers() method. Lastly, we will schedule a 
     * DriveOnSegmentEvent some 'exchangeTime' minutes in the future.
     * 
     */
    @Override
    public void completeEvent() {
        System.out.println("The current stop " + b.getCurrStop().getId());

        double timeToBoard = 0;
        double timeToDepart = 0;
        
        Map<String, ArrayList<Passenger>> busPassengers = b.getPassengers();
        System.out.println("These are the passengers on this Bus " + b.getPassengers());
        ArrayList<Passenger> departingPassengers = new ArrayList<>();
        if (busPassengers.size() > 0) {
            if (busPassengers.get(s.getId()) == null) {
                System.out.println("There are no passengers for this stop so lets initialize an empty arraylist for them");
                busPassengers.put(s.getId(), new ArrayList<Passenger>());
            }
            System.out.println("These are the passengers getting off at this stop " + s.getId()       
                                + busPassengers.get(s.getId()));
            departingPassengers = busPassengers.get(s.getId());
            int numOfPassDeparting = departingPassengers.size();
            timeToDepart = numOfPassDeparting * 0.3;

        }
        
        Map<String,ArrayList<Passenger>> stopPassengers = s.getPassenger();
        System.out.println("These are the passengers on this STOP " + s.getPassenger());
        if (stopPassengers.get(b.getBusType()) == null) {
                System.out.println("There are no passengers for this stop right now so we must initialize an empty ArrayList for them");
                stopPassengers.put(b.getBusType(), new ArrayList<Passenger>());
        }
        ArrayList<Passenger> onboardingPassengers = stopPassengers.get(b.getBusType());
        timeToBoard = onboardingPassengers.size() * 0.3; // Time to board
        int exchangeTime = (int)(timeToBoard + timeToDepart);

        if (b.getNumLoops() >= b.getBreakAfterNumLoops()) {
            deletePassengers(departingPassengers, b, s, time);
            detourPassenger(b, s, time);
            Event e = new DriverBreakEvent(b, s, time + (int)timeToDepart);
            SimulationExecutive.schedule(e);

        } else if (s.getIsOccupied()) {
            System.out.println("THe stop is currently occupied by another bus, I'm going to schedule this event again ");
            Event e = new ArriveAtBusStopEvent(b, s, time + exchangeTime + 2);
            SimulationExecutive.schedule(e);
        } else { 
            System.out.println("The stop is free, I'm going to then arrive and then depart from this stop");
            s.setIsOccupied(true);
            deletePassengers(departingPassengers, b, s, time);
            //in case we reach capacity, exchange time will reflect the # of limited passengers that can 
            //onboard
            onboardingPassengers = passengerOnBoard(onboardingPassengers, b, s, time);
            timeToBoard = onboardingPassengers.size() * 0.3; 
            exchangeTime = (int)(timeToBoard + timeToDepart);
            createPassengers(time);
            Event e = new DepartFromBusStopEvent(b, s, time + exchangeTime + 1);
            SimulationExecutive.schedule(e);

        }
        

    }

    /*
     * deleletePassenger() is called prior to detourPassenger() and so it will take care
     * of altering the # of leftover passengers in the bus. We invoke getPassenger()
     * methods of the Bus and Bus Stop instance and move passengers from the bus's passenger list
     * over to the bus stop's passenger list and change the bus capacity and bus stop passenger number
     * respectively.
     *     
     */
    public static void detourPassenger(Bus b, BusStop s, int stime) {
            Map<String, ArrayList<Passenger>> busMap = b.getPassengers();
            Map<String, ArrayList<Passenger>> stopMap = s.getPassenger();
            for (Map.Entry<String, ArrayList<Passenger>> entry : busMap.entrySet()) {
                    ArrayList<Passenger> value = entry.getValue();
                    for (int i = 0; i < value.size(); i++) {
                        Passenger pass = value.get(i);
                        pass.setNumDetours(pass.getNumDetours() + 1);
                        value.set(i, pass);
                    }

                    String busStops = entry.getKey();

                    //add passengers to stop
                    stopMap.put(b.getBusType(), value);
                    s.setPassenger(stopMap);

                    //remove this passengers from bus
                    busMap.put(busStops, new ArrayList<Passenger>());
                    b.setPassengers(busMap);
            
            //b.num pass not important
            s.setNumPassengers(s.getNumPassengers() + (b.CAPACITY - b.getEmptySeats()));
            //bus is empty  so at full capacity
            b.setEmptySeats(b.CAPACITY);
            
        }

    }

    /*
     * Arraylist of offboarding passengers is passed in, along with the current bus and bus stop involved
     * in the exchange. We add these passengers to the deadPassengers class-level arraylist stored
     * in Passenger.java after setting their time of departure. The bus has emptied out the arraylist
     * of passengers getting off at the stop with the specified id, so we need to add a new empty arraylist
     * for this bus stop id key in the bus's passengers map. Adjust the bus's number of empty seats as well
     */
    public static void deletePassengers(ArrayList<Passenger> p, Bus b, BusStop s, int stime) {
        if (p.size() > 0) {
            for (int i = 0; i < p.size(); i++) {
                p.get(i).setHasArrived(true);
                p.get(i).setTimeOfDepartureFromBus(stime); //update departure time to all passengers
            }
            Passenger.addToDeadPassengerList(p); //we've saved these passengers somewhere. 
            //now i want to empty the bus's arraylist of passengers getting off at this stop.
            //do we clear() or just null the reference?
            System.out.println("Byebye passengers, thanks for riding! ");
            Map<String, ArrayList<Passenger>> map = b.getPassengers();
            map.put(s.getId(), new ArrayList<>());
            System.out.println("The Bus's map after passengers from this stop is now " + map.get(s.getId()) + " for the bus stop, " + s.getId());
            b.setPassengers(map); 
            //update bus's capacity right now. there are now p new empty seats!
            b.setEmptySeats(b.getEmptySeats() + p.size());
            
        }
        

    }


    /*
     * Takes an the arraylist of onboarding passengers, as well as the current bus and bus stop
     * We will create two new arraylists, allowedPassengers and heldBack to allocate the passengers
     * who can get on without exceeding bus capacity, and those that must be held back and remain
     * at the stop. We populate these arraylists accordingly and add 'allowedPassengers' as a value to
     * the bus's passenger map, and add 'heldBack' as a value to the bus stop's map
     */
    public static ArrayList<Passenger> passengerOnBoard(ArrayList<Passenger> onboardingPassengers, Bus b, BusStop s,    
                                        int stime) {

        System.out.println("Passengers will onboard now!");
        System.out.println(onboardingPassengers + "will get on bus " + b.getId() + "from bus stop" + s.getId());

        ArrayList<Passenger> allowedPassengers = new ArrayList<>();
        ArrayList<Passenger> heldBack = new ArrayList<>();

        //reached capacity. all waiting passengers are heldback
        if (b.getEmptySeats() == 0) {
            System.out.println("sorry, we've reached capacity!");
            heldBack = onboardingPassengers; //all of them heldback
        //there are empty seats, but not enough for all. must partition the input passenger arraylist
        } else if (onboardingPassengers.size() > b.getEmptySeats() && b.getEmptySeats() != 0) {
            System.out.println("There's too many of you, only the first " + b.getEmptySeats() + "can get on");
            for (int i = 0; i < onboardingPassengers.size(); i++) {
                if (i < b.getEmptySeats()) {
                    allowedPassengers.add(onboardingPassengers.get(i));
                } else {
                    heldBack.add(onboardingPassengers.get(i));
                }
            }

            System.out.println("Allowed passengers " + allowedPassengers + " and" + allowedPassengers.size() + "of u");
            System.out.println("Passengers Helds back " + heldBack + " and" + heldBack.size() + "of u");

        //no capacity issues! we can add all waiting passengers
        } else {
            System.out.println("Sweet! We have plently of seats, All of you can get on");
            allowedPassengers = onboardingPassengers;
        }                              
        
        //add the selected waiting passengers to the bus's passenger list according to their
        //destination bus stop
        Map<String, ArrayList<Passenger>> map1 = b.getPassengers();
        for (int i = 0; i < allowedPassengers.size(); i++) {
            //null check
            if (map1.get(allowedPassengers.get(i).getEndStop().getId()) == null) {
                map1.put(allowedPassengers.get(i).getEndStop().getId(), new ArrayList<Passenger>());
            }
            ArrayList<Passenger> p = map1.get(allowedPassengers.get(i).getEndStop().getId());
            p.add(allowedPassengers.get(i));
            map1.put(allowedPassengers.get(i).getEndStop().getId(), p);
            allowedPassengers.get(i).setTimeOfGettingOnBus(stime); //update onboarding time of selected passengers
        }
         
        Map<String, ArrayList<Passenger>> map = b.getPassengers();
        map.put(s.getId(), allowedPassengers);
        b.setPassengers(map); 
        System.out.println("These passengers are on the bus now! " + b.getPassengers());
        //update bus's capacity right now. there are now p less seats! 
        b.setEmptySeats(b.getEmptySeats() - allowedPassengers.size());

        //remove passengers at stop. basically just set the bus key to the held back list
        Map<String, ArrayList<Passenger>> stopMap = s.getPassenger();
        stopMap.put(b.getBusType(), heldBack);
        s.setPassenger(stopMap); 
        System.out.println("These passengers removed from the stop" + s.getId()+ "now " + s.getPassenger());
        s.setNumPassengers(s.getNumPassengers() - allowedPassengers.size());

        //also update the time the bus type arrived at this stop in the BusStop class's arrivalTimeMap
        Map<String, ArrayList<Integer>> arrivalMap = s.getarrivalTimeMaps();
        if (arrivalMap.get(b.getId()) == null) {
            arrivalMap.put(b.getId(), new ArrayList<>());
        } else {
            ArrayList<Integer> list = arrivalMap.get(b.getId());
            list.add(stime);
            s.setarrivalTimeMaps(arrivalMap);

        }

        //return the allowedPassengers list so the complete() can calculate bus departure time correctly
        return allowedPassengers;
        
    }

    /*
     * A Passenger object needs its chosen start stop, end stop, and bus type
     * createPassengers() takes care of this by first randomly selecting a start stop according to 
     * popularity probability distribution, then assigns a bus type that goes to this stop, then creates
     * a subset of bus stops that this bus visits. All of these attributes are fed into the Passenger
     * constructor to generate a new passenger instance.
     */
    public static void createPassengers(int stime) {
        // System.out.println(Passenger.getPassengerTraffic()); // check if null
        // System.out.println(stime%60);
        int num = Passenger.getPassengerTraffic().get((int)(stime/60));
        System.out.println("I'm creating " + num + " this many new passengers in the simulation");
        
        for (int i = 0; i < num; i++) {
            //pick a starting bus stop according to bus popularity score
            BusStop chosenStartStop = selectBusStop(BusStop.getAllBusStops(), BusStop.getAllPopularityScores());
            ArrayList<String> allowedBuses = BusStop.getAllowedBuses().get(chosenStartStop);
            Random rand = new Random();
            
            //pick a bus type that the passenger can get on from this starting stop
            int k = rand.nextInt(allowedBuses.size());
            String chosenBus = allowedBuses.get(k);
            if (chosenStartStop.passenger.get(chosenBus) == null) {
               chosenStartStop.passenger.put(chosenBus, new ArrayList<>());
            }
            
            //create a subset of allowed bus stops and their corresponding popularity scores the selected
            //bus type can visit
            ArrayList<RouteNode> busRoute = Bus.getRoutes().get(chosenBus);
            ArrayList<BusStop> allowedBusStops = new ArrayList<>();
            ArrayList<Double> popularity = new ArrayList<>();
            for (int j = 0; j < busRoute.size(); j++) {
                if (busRoute.get(j) instanceof BusStop && !busRoute.get(j).getId().equals(chosenStartStop.getId())) {
                    allowedBusStops.add((BusStop)busRoute.get(j));
                    popularity.add(allowedBusStops.get(allowedBusStops.size() - 1).getPopularityScore());
                }
            }

            //select end stop
            BusStop chosenEndStop = selectBusStop(allowedBusStops, popularity); 

            //provide the above attributes as input to create the passenger instance
            Passenger p = new Passenger(chosenStartStop, chosenEndStop, stime, chosenBus);
            System.out.println("Passenger at this start stop " + p.getStartStop() + " end stop :" + p.getEndStop() + " and this bus " + p.getBus());
            
            //add passenger to start bus stop
            chosenStartStop.addPassenger(p); //has to increment num passengers in bus stop as well
        }
    }

    /*
     * Randomly selects a Bus Stop from the buses in the 'busStop' arraylist passed in according to 
     * their corresponding popularity probability distribution. 
     * We apply normalization here so that the sum of popularity scores need not equal to 1.
     * 
     */
    public static BusStop selectBusStop(ArrayList<BusStop> busStop, ArrayList<Double> popularityScore) { 
        double randomValue = new Random().nextDouble();
        double[] cumulativeProbabilities = new double[popularityScore.size()];
        ArrayList<Double> normalized = new ArrayList<>();       
        // Calculate the sum of all elements in the ArrayList
        double sum = 0.0;
        for (double value : popularityScore) {
            sum += value;
        }
        // Normalize each element by dividing by the sum
        for (double value : popularityScore) {
            double normalizedValue = value / sum;
            normalized.add(normalizedValue);
        }
        popularityScore = normalized;
        cumulativeProbabilities[0] = popularityScore.get(0);
        BusStop selectedElement = null;
        boolean pickedElement = false;
        for (int i = 1; i < popularityScore.size(); i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + popularityScore.get(i);
            if (!pickedElement && randomValue < cumulativeProbabilities[i]) {
                selectedElement = busStop.get(i);
                pickedElement = true;
            }
        }

        //System.out.println("Randomly selected element: " + selectedElement.getId());
        return selectedElement;
    }

    /*----------------------------------getters, setters, toString--------------------------------- */

    public Bus getB() {
        return this.b;
    }

    public void setB(Bus b) {
        this.b = b;
    }

    public BusStop getS() {
        return this.s;
    }

    public void setS(BusStop s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "{ This is an Arrive At Bus Stop Event at time" +
            " time='" + getTime() + "'" +
            " for , bus='" + getB().getId() + "'" +
            "and at stop, stop='" + getS().getId() + "'" +
            "We are at index " + b.getCurrStopIndex() + " right now, which should be a " + b.getCurrStop();
    }

}