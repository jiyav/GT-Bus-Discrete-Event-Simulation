public class DriverFinishedBreakEvent extends Event {
    private BusStop s; //break will be taken after reaching this stop
    //remember to step isFree to true bc break will be taken away from the unloading site of the stop
    //meaning that other buses can still unload while this bus is on break
    //Driver is done from break and can pick up passengers again. this could be newly spawned passengers
    //that happened in the meantime or the passengers it previously kicked off lol
    //have to reset break 
    public DriverFinishedBreakEvent(Bus b, BusStop s, int time) {
        super(time, b);
        this.s = s;
    }

    public void completeEvent() {
        //bus will drive over to bus stop unloading point to pickup passengers again
        System.out.println("Bus " + b.getId() + " has finished its break at time " + time +  "and will pick up passengers ");
        SimulationExecutive.schedule(new ArriveAtBusStopEvent(b, s, time + 1));
    }

    @Override
    public String toString() {
        return "{Driver finished break event " +
            " s='" + s + "'" +
            "}";
    }
    

}