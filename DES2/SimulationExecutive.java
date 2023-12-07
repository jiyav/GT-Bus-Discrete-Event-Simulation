import java.util.Comparator;
import java.util.PriorityQueue;

public class SimulationExecutive {
    public static PriorityQueue<Event> futureEventList = new PriorityQueue<>(Comparator.comparingInt(Event::getTime));
    public static void schedule(Event e) {
        System.out.println("Im adding this event to the FEL via schedule() " + e + "\n");
        futureEventList.add(e);
    }

    public static void simulation() {
        //time is in minutes
        int time = futureEventList.peek().getTime();
        while(time >= 420 && time <= 1140) {
            System.out.println("THis is the future event list right now" + futureEventList);
            System.out.println("It has " + futureEventList.size() + " events in it" + "\n");
            Event e = futureEventList.peek();
            System.out.println("THis is the event about to be processed from the FEL" + e.toString() + "\n");
            e.completeEvent();
            System.out.println("THis is the event has been completed " + e.toString() + "\n");
            futureEventList.remove();
            System.out.println("THis is the event has been removed from list " + e.toString() + "\n");
            System.out.println("this is waht FEL should be now: " + futureEventList);
            time = futureEventList.peek().getTime();
            
        }
    }
}
