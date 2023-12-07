public class DriveOnRoadSegmentEvent extends Event {
    
    private RoadSegment r;
    //private int time;

    public DriveOnRoadSegmentEvent(Bus b, RoadSegment r, int time) {
        super(time, b);
        this.r = r;
    }

    @Override
    public void completeEvent() {
        //calculator the current hour. 
        int currentHour = (time/60);
        b.setSpeed(RoadSegment.getSpeedLimit() * RoadSegment.getTrafficCond().get(currentHour));//doesn't rly matter
        //((d in miles/v divided by speed limit in miles/hr) * 60 to convert to minutes) * traffic delay index
        int drivingTime = (int)((Math.ceil((r.getDistance() / RoadSegment.getSpeedLimit()) * 60) * RoadSegment.getTrafficCond().get(currentHour)));
        System.out.println("This is the amount of time we must drive for " + drivingTime);

        //update the current stop pointer
        updateStop(b);
        RouteNode nextStop = b.getCurrStop();
        System.out.println("What's the route node after road segment?");

        //schedule ArriveAtBusStopEvent if next stop is a BusStop
        if (nextStop instanceof BusStop) {
            System.out.println("Next node is a bus stop! " + nextStop.getId());
            Event e = new ArriveAtBusStopEvent(b, (BusStop) nextStop, time + drivingTime);
            SimulationExecutive.schedule(e);
        //schedule StopAtTrafficLightEvent if next stop is a TrafficLight
        } else {
            System.out.println("Next node is a traffic light! " + nextStop.getId());
            Event e = new StopAtTrafficLightEvent(b, (TrafficLight) nextStop, time + drivingTime);
            SimulationExecutive.schedule(e);
        }

    }

    /*------------------------------------getters and setters--------------------------------------- */



    public Bus getB() {
        return this.b;
    }

    public void setB(Bus b) {
        this.b = b;
    }

    public RoadSegment getR() {
        return this.r;
    }

    public void setR(RoadSegment r) {
        this.r = r;
    }

    public int getTime() {
        return this.time;
    }

    public int setTime(int time) {
        this.time = time;
        return this.time;
    }

    @Override
    public String toString() {
        return "{Drive on Road Event" +
            " bus='" + getB().getId() + "'" +
            ", road='" + getR().getId() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }



}