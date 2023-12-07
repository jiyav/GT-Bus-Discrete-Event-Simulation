/*
 * Utility class for helper methods
 * Currently provides functionality to allow for express routes
 */
import java.util.ArrayList;
import java.util.Arrays;
public class Utility {
    /*
     * Converts an arraylist of RouteNodes and filters out the stops with less than popularityThreshold.
     */
    public static ArrayList<RouteNode> toExpress(ArrayList<RouteNode> route, double popularityThreshold) {
        ArrayList<RouteNode> expressRoute = new ArrayList<RouteNode>();
        for (int i = 0; i < route.size(); i++) {
            if (!(route.get(i) instanceof BusStop) || ((BusStop)(route.get(i))).getPopularityScore() >= popularityThreshold) {
                expressRoute.add(route.get(i));
            }
        } 
        for (int i = 0; i < expressRoute.size(); i++) {
            if (expressRoute.get(i) instanceof RoadSegment && expressRoute.get((i + 1) % expressRoute.size()) instanceof RoadSegment) {
                RoadSegment currentSegment = (RoadSegment) expressRoute.get(i);
                RoadSegment nextSegment = (RoadSegment) expressRoute.get((i + 1) % expressRoute.size());

                // Merge adjacent RoadSegments
                RoadSegment mergedSegment = mergeSegments(currentSegment, nextSegment);

                // Replace the two adjacent segments with the merged segment
                expressRoute.set(i, mergedSegment);
                expressRoute.remove((i + 1) % expressRoute.size());
                if (i == expressRoute.size() - 1) {
                    i --;
                }
                i--; // Decrement i to recheck the new current index
                }
            }
        return expressRoute;
        }
    /*
    Makes sure that the route doesn't have two adjacent segments by merging current and next segments
    The length of the combination of road segments a and b is a.length + b.length. 
    The road traffic constant is the weighted average of traffic constants by road segments (a.length * a.traffic) + (b.length * b.traffic) / (a.length + b.length).
    */
    public static RoadSegment mergeSegments(RoadSegment current, RoadSegment next) {
        String combinedId = current.getId() + "&" + next.getId();
        double combinedDistance = current.getDistance() + next.getDistance();
        //double combinedTraffic = current.getDistance() * current.getTraffic();
        //combinedTraffic += next.getDistance() * next.getTraffic();
        //combinedTraffic /= combinedDistance;
        return new RoadSegment(combinedId, combinedDistance/*combinedTraffic*/);
    }
}
    
