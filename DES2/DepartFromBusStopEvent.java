public class DepartFromBusStopEvent extends Event {
    //private int time;
    //private Bus b;
    private BusStop s;

    public DepartFromBusStopEvent(Bus b, BusStop s, int time) {
        super(time, b);
        this.s = s;
    }

    public void completeEvent() {
        //print statements to view the current stop
        System.out.println(b.getCurrStop());
        System.out.println(b.getCurrStopIndex());
        System.out.println(b.getRoute());

        //update the stop and set isOccupied to false
        s.setIsOccupied(false);
        updateStop(b);

        //print statements to view next stop after updating
        System.out.println(b.getCurrStop());
        System.out.println(b.getCurrStopIndex());

        //next stop is guarenteed to be a road segment
        RoadSegment r = (RoadSegment) b.getCurrStop();
        //System.out.println("This is the bus" + b);

        //schedule DriveOnRoadSegmentEvent
        Event e = new DriveOnRoadSegmentEvent(b, r, time + 1);
        SimulationExecutive.schedule(e);

    }


    @Override
    public String toString() {
        return "{This is a DepartFromBusStop Event " + getB().getId() +
            " moves from stop s=" + getS().getId() + "'" +
            "at Time " + getTime() + ". so from index " + b.getCurrStopIndex() + " to " + (b.getCurrStopIndex()+1);
    }


    public BusStop getS() {
        return this.s;
    }

    public void setS(BusStop s) {
        this.s = s;
    }



}