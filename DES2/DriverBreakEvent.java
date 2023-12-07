public class DriverBreakEvent extends Event {
    private BusStop s; //break will be taken after reaching this stop
    //remember to step isFree to true bc break will be taken away from the unloading site of the stop
    //meaning that other buses can still unload while this bus is on break
    //updateStop() does NOT occur in ArriveAtBusStop btw

    public DriverBreakEvent(Bus b, BusStop s, int time) {
        super(time, b);
        this.s = s;
    }

    public void completeEvent() {
        s.setIsOccupied(false);
        b.setNumLoops(0); //reset this for the next break
        //set finished break after necesaary time
        System.out.println("Bus " + b.getId() + " will take a break at " + time +  " and start back up at " + (time + b.getBreakTime()));
        SimulationExecutive.schedule(new DriverFinishedBreakEvent(b, s, time + b.getBreakTime()));
    }


    @Override
    public String toString() {
        return "{Driver Break Event" +
            " s='" + s + "'" +
            "}";
    }


    

    

}