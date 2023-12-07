public class StopAtTrafficLightEvent extends Event {
    private TrafficLight t;
    
    public StopAtTrafficLightEvent(Bus b, TrafficLight t, int time) {
        super(time, b);
        this.t = t;     
    }
    
    @Override
    public void completeEvent() {
        updateStop(b);//update bus's next stop pointer
        //if light is green, wait time should automatically be 0
        SimulationExecutive.schedule(new DriveOnRoadSegmentEvent(b, (RoadSegment)b.getCurrStop(), time + t.getWaitTime()));
        if((int) Math.round(Math.random()) == 0) {
            t.setGreenLight(false);
            int randomNumber = (int) (1 + Math.random() * (2 - 1 + 1));
            t.setWaitTime(randomNumber);     
        } else {
            t.setGreenLight(true);
            t.setWaitTime(0);
        }
    }

    public Bus getB() {
        return this.b;
    }

    public void setB(Bus b) {
        this.b = b;
    }

    public TrafficLight getT() {
        return this.t;
    }

    public void setT(TrafficLight t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "{Stop at traffic light event " +
            " time='" + getTime() + "'" +
            ", bus='" + getB().getId() + "'" +
            ", traffic light='" + getT().getId() + "'" +
            "}";
    }

    
}
