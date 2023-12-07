public abstract class Event {
    /*
     * Since this simulation is modeled after a discrete event simulation, the events in 
     * this bus simulation are defined as behaviors that a Bus can exhibit at a given time 
     * that alter certain state variables, or more so that states held by certain objects/entities
     * involved in that event. The advancement of time through the simulation is driven by the 
     * times that events are scheduled.
     * 
     * The possible events are as follows:
     * - ArriveAtBusStopEvent: 
     *      This bus been scheduled to arrive at a bus stop via this event. When the event is processed,
     *      it'll first check whether the driver is due to take a break. If so, a DriverBreakEvent will 
     *      be scheduled accordingly. If not, it'll check if the stop is occupied by another bus. If so,
     *      the same event will be scheduled again at a later time since the bus will have to wait
     *      until the stop is free. If the stop is free, then the bus can arrive and carry out its 
     *      processes. First, it'll offload the passengers who need to get off this stop and delete them
     *      from the simulation. Then, it'll extract the passengers waiting at the bus stop and onboard 
     *      to the bus. However, it can only onboard a threshold of passengers before exceeding 
     *      capacity. If capacity is reached before onboarding all passengers, then those passengers
     *      must wait for the next bus. Once onboarding is complete, new passengers are spawned 
     *      and added to the simulation. This removal, exchange, and creation of passengers in
     *      the simulation results in changes to the states held by the Bus, Bus Stop, and Passenger
     *      instances involved, as well as the class-level/static data-holding variables in these .java
     *      files. After this event is processed, a DepartFromBusStopEvent is scheduled to occur 
     *      sometime in the future. This is determined by the amount of time it takes to complete 
     *      passenger onboarding and offboarding. 
     * 
     * - DepartFromBusStopEvent:
     *      Once an ArrivalAtBusStopEvent is completed, the next step for this bus is to depart
     *      from the bus stop. The DepartFromBusStopEvent will update the current stop/index of the bus
     *      to its next node in the route. This is by default a Road Segment because all routes are 
     *      defined to have a Road Segment in between every Bus Stop and or Traffic Light because a bus
     *      must travel some distance on the road before reaching them. Thus, a DriveOnRoadSegment will 
     *      be scheduled to occur immedietly after this one. 
     * 
     * - DriveOnRoadSegmentEvent:
     *      The bus will drive on the road for a certain amount of time. This is calculated based on
     *      the distance (in miles) of the road, the speed limit (in miles/hours), and the road traffic
     *      at that hour (a # that is multiplied to the base time calculation with the latter two
     *      paramters, time = (d/v) * traffic. The current stop of the bus will be updated to the next  
     *      stop in the route, and if it is a StopAtTrafficLightEvent, this event will be scheduled
     *      at the calculated time. If it is a Bus Stop, an ArriveAtBusStopEvent will be scheduled 
     *      accordingly. It is assumed that the routes do not contain adjacent road segments, as this
     *      could just be reduced to a single, longer road segment.
     * 
     * - DriverBreakEvent: 
     *      This event will be scheduled if when a bus arrives at a stop and it's reached its
     *      numLoops threshold after which it is allowed to take a break. For the purpose of our 
     *      simulation model, a driver's health comes first and so we disregard if another bus is 
     *      occupying the stop. In the case that it is occupied, we assume this bus just parks behind
     *      the current bus present. During this event, passengers who need to get off this stop will
     *      get off/be deleted like normal. However, the bus must be empty for a driver to take a       
     *      break, get food, use the restroom, etc. Thus, ALL other passengers in the bus must offboard 
     *      at this stop as well. This will be considered a "detour" stop for these passengers, and they
     *      must wait until either the next bus of this type arrives, or until the driver of the 
     *      current bus has finished his/her break. A DriverFinishedBreakEvent will be automatically 
     *      scheduled for this bus after the allocated break time is over. 
     * 
     * - DriverFinishedBreakEvent:
     *      Thus bus is back in service so it can properly "arrive" again at the stop and begin picking
     *      up passengers again. Thus, an ArriveAtBusStop will be scheduled to occur immedietly after. 
     *    
     */
    protected int time;
    protected Bus b;


    /*Event constructor. Since a bus is central to all event times, this is a state variable
     * common to ALL events. Time is also involved/changed to drive the simulation forward in time.
     */
    public Event(int time, Bus b) {
        this.time = time;
        this.b = b;
    }


    /*
     * Each event child inherits and overrides this method. This method is invoked in the simulation
     * executive when the event to selected to processed in the future event list is chosen
     */
    public void completeEvent() {
        System.out.println("complete event");
    }

    /*
     * Updates the stop of the bus. Used by events as needed in order to traverse through the route
     * Since the route is held in an array list, the index wraps around to 0 when we reach the end
     * of the list to avoid indexOutOfBounds error and maintain a cycle. It is assumed that the
     * route will be traversed as a circle.
     */
    public void updateStop(Bus b) {
        int currStopIndex = b.getCurrStopIndex();
        currStopIndex += 1; 
        if (currStopIndex >= b.getRoute().size()) {
            currStopIndex = 0;
            b.setNumLoops(b.getNumLoops() + 1); 
        }

        b.setCurrStopIndex(currStopIndex);
        b.setCurrStop(b.getRoute().get(b.getCurrStopIndex()));
    }

    /*----------------------------getters, setters, toString---------------------------- */
    public int getTime() {
        return this.time;
    }

    public int setTime(int time) {
        this.time = time;
        return this.time;
    }

    public Bus getB() {
        return this.b;
    }

    public void setB(Bus b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "{" +
            " time='" + getTime() + "'" +
            ", bus='" + getB() + "'" +
            "}";
    }
    
}
