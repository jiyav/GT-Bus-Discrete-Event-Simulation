import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DataExport {
    public static void writeBusStopDataToCSV(ArrayList<BusStop> busStops, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write CSV header
            writer.append("BusStopId,BusName,ArrivalTime\n");

            for (BusStop busStop : busStops) {
                String busStopName = busStop.getId();
                Map<String, ArrayList<Integer>> arrivalTimeMaps = busStop.getArrivalTimeMap();

                for (Map.Entry<String, ArrayList<Integer>> entry : arrivalTimeMaps.entrySet()) {
                    String busName = entry.getKey();
                    ArrayList<Integer> arrivalTimes = entry.getValue();

                    for (Integer arrivalTime : arrivalTimes) {
                        // Write data to CSV
                        writer.append(busStopName).append(",").append(busName).append(",").append(arrivalTime.toString()).append("\n");
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writePassengerDataToCSV(ArrayList<ArrayList<Passenger>> deadPassengers, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("id,StopArrivalTime,BusArrivalTime,BusDepartureTime,StartStopId,EndStopId,BusType, Num Detours\n");
            for (ArrayList<Passenger> passengers : deadPassengers) {
                for (Passenger passenger : passengers) {
                    writer.append(String.format("%d,%d,%d,%d,%s,%s,%s,%d\n",
                        passenger.getId(),
                        passenger.getTimeOfArrivalToStop(),
                        passenger.getTimeOfGettingOnBus(),
                        passenger.getTimeOfDepartureFromBus(),
                        passenger.getStartStop().getId(),
                        passenger.getEndStop().getId(),
                        passenger.getBus(),
                        passenger.getNumDetours()
                ));
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePassengerDataToCSVAppend(ArrayList<ArrayList<Passenger>> deadPassengers, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            for (ArrayList<Passenger> passengers : deadPassengers) {
                for (Passenger passenger : passengers) {
                    writer.append(String.format("%d,%d,%d,%d,%s,%s,%s,%d\n",
                            passenger.getId(),
                            passenger.getTimeOfArrivalToStop(),
                            passenger.getTimeOfGettingOnBus(),
                            passenger.getTimeOfDepartureFromBus(),
                            passenger.getStartStop().getId(),
                            passenger.getEndStop().getId(),
                            passenger.getBus(),
                            passenger.getNumDetours()
                    ));
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}