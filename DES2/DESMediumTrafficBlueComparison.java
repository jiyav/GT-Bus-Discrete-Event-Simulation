/*
Driver for DES Medium Traffic for the blue route
Runs the simulation for num buses = 1-10 on blue line
Passenger and road traffic set to medium
Outputs written to csvs
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.Comparator;

public class DESMediumTrafficBlueComparison {


    public static void main(String[] args) {
        boolean visualized = false;
            // Blue Route
            //Nav
            BusStop stopA = new BusStop("S1", 0.6);
            stopA.init();
            RoadSegment r1 = new RoadSegment("R1", 0.2);
            TrafficLight t1 = new TrafficLight("T1");
            RoadSegment r2 = new RoadSegment("R2", 0.1);

            //Freshman dorms
            BusStop stopB = new BusStop("S2", 0.4);
            stopB.init();
            RoadSegment r3 = new RoadSegment("R3", 0.4);
            TrafficLight t2 = new TrafficLight("T2");
            RoadSegment r4 = new RoadSegment("R4", 0.1);

            //Frats 1
            BusStop stopC = new BusStop("S3", 0.25);
            stopC.init();
            RoadSegment r5 = new RoadSegment("R5", 0.2);

            //Frats 2
            BusStop stopD = new BusStop("S4", 0.25);
            stopD.init();
            RoadSegment r6 = new RoadSegment("R6", 0.2);

            //Kappa Alpha
            BusStop stopE = new BusStop("S5", 0.4);
            stopE.init();
            RoadSegment r7 = new RoadSegment("R7", 0.1);
            TrafficLight t3 = new TrafficLight("T3");
            RoadSegment r8 = new RoadSegment("R8", 0.2);
            TrafficLight t4 = new TrafficLight("T4");
            RoadSegment r9 = new RoadSegment("R9", 0.1);

            //Baseball field
            BusStop stopF = new BusStop("S6", 0.2);
            stopF.init();
            RoadSegment r10 = new RoadSegment("R10", 0.3);

            //Klaus
            BusStop stopG = new BusStop("S7", 0.4);
            stopG.init();
            RoadSegment r11 = new RoadSegment("R11", 0.3);


            //Cherry Emerson
            BusStop stopH = new BusStop("S8", 0.5);
            stopH.init();
            RoadSegment r12 = new RoadSegment("R12", 0.1);
            TrafficLight t5 = new TrafficLight("T5");
            RoadSegment r13 = new RoadSegment("R13", 0.2);

            //Kendeda
            BusStop stopI = new BusStop("S9", 0.5);
            stopI.init();
            RoadSegment r14 = new RoadSegment("R14", 0.7);

            //8th street intersection
            BusStop stopJ = new BusStop("S10", 0.3);
            stopJ.init();
            RoadSegment r15 = new RoadSegment("R15", 0.5);

            //Ninth Street
            BusStop stopK = new BusStop("S11", 0.2);
            stopK.init();
            RoadSegment r16 = new RoadSegment("R16", 0.6);

            //Eighth street again
            BusStop stopL = new BusStop("S12", 0.3);
            stopL.init();
            RoadSegment r17 = new RoadSegment("R17", 0.7);

            //Nanotech institue
            BusStop stopM = new BusStop("S13", 0.2);
            stopM.init();
            RoadSegment r18 = new RoadSegment("R18", 0.5);

            //CRC
            BusStop stopN = new BusStop("S14", 0.7);
            stopN.init();
            RoadSegment r19 = new RoadSegment("R19", 0.1);
            TrafficLight t6 = new TrafficLight("T6");
            RoadSegment r20 = new RoadSegment("R20", 0.5);

            //Student Center
            BusStop stopO = new BusStop("S15", 0.1);
            stopO.init();
            RoadSegment r21 = new RoadSegment("R21", 0.5);

            //Transit Terminal
            BusStop stopP = new BusStop("S16", 0.4);
            stopP.init();
            RoadSegment r22 = new RoadSegment("R22", 0.3);

            //Cherry Street
            BusStop stopQ = new BusStop("S17", 0.2);
            stopQ.init();
            RoadSegment r23 = new RoadSegment("R23", 0.3);
            TrafficLight t7 = new TrafficLight("T7");
            RoadSegment r24 = new RoadSegment("R24", 0.5);
            TrafficLight t8 = new TrafficLight("T8");
            RoadSegment r25 = new RoadSegment("R25", 0.8);
            TrafficLight t9 = new TrafficLight("T9");
            RoadSegment r26 = new RoadSegment("R26", 0.3);


            ArrayList<RouteNode> blueRoute = new ArrayList<>(Arrays.asList(stopA, r1, t1, r2, stopB, r3, t2, r4, stopC, r5, stopD, r6, stopE, r7, t3, r8, t4, r9, stopF, r10, stopG, r11, stopH, r12, t5, r13, stopI, r14, stopJ, r15, stopK, r16, stopL, r17, stopM, r18, stopN, r19, t6, r20, stopO, r21, stopP, r22, stopQ, r23, t7, r24, t8, r25, t9, r26));

            Map<String, ArrayList<RouteNode>> routes = new HashMap<>();
            routes.put("Blue", blueRoute);
            Bus.setRoutes(routes);
            Bus.setBusStops();
            BusStop.setAllowedBuses(routes);

            ArrayList<Bus> busList = new ArrayList<>();
            for (int j = 0; j <= 3; j++) {
                busList.add(new Bus("blueBus" + j, "Blue", blueRoute));
                busList.get(j).init("Blue");
            }

            Map<Integer, Double> trafficIndex = new HashMap<Integer, Double>();
            trafficIndex.put(7, 1.5);
            trafficIndex.put(8, 1.5);
            trafficIndex.put(9, 1.5);
            trafficIndex.put(10, 1.5);
            trafficIndex.put(11, 1.5);
            trafficIndex.put(12, 1.5);
            trafficIndex.put(13, 1.5);
            trafficIndex.put(14, 1.5);
            trafficIndex.put(15, 1.5);
            trafficIndex.put(16, 1.5);
            trafficIndex.put(17, 1.5);
            trafficIndex.put(18, 1.5);
            trafficIndex.put(19, 1.5);//7pm

            RoadSegment.setTrafficCond(trafficIndex);

            Map<Integer, Integer> passengerTraffic = new HashMap<>();
            passengerTraffic.put(7, 10);
            passengerTraffic.put(8, 10);
            passengerTraffic.put(9, 10);
            passengerTraffic.put(10, 10);
            passengerTraffic.put(11, 10);
            passengerTraffic.put(12, 10);
            passengerTraffic.put(13, 10);
            passengerTraffic.put(14, 10);
            passengerTraffic.put(15, 10);
            passengerTraffic.put(16, 10);
            passengerTraffic.put(17, 10);
            passengerTraffic.put(18, 10);
            passengerTraffic.put(19, 10);//7pm

            Passenger.setPassengerTraffic(passengerTraffic);

            /*create initial passengers for all bus stops. the createPassenger() method is in
            ArriveAtBusStop event*/
            int time = 420;

            ArrayList<BusStop> allBusStops = BusStop.getAllBusStops();
            for (int j = 0; j < allBusStops.size(); j++) {
                ArriveAtBusStopEvent.createPassengers(time);
            }

            for (int j = 0; j < Bus.getAllBuses().size(); j++) {
                SimulationExecutive.schedule(new ArriveAtBusStopEvent(Bus.getAllBuses().get(j), (BusStop) Bus.getAllBuses().get(j).getCurrStop(), time));
            }



            SimulationExecutive.simulation();
            DataExport.writeBusStopDataToCSV(allBusStops, "BusStopsMediumTrafficBlueComparison" + ".csv");
            ArrayList<ArrayList<Passenger>> deadPassengers = Passenger.getDeadPassengers();
            DataExport.writePassengerDataToCSVAppend(deadPassengers, "PassengersMediumTrafficBlueComparison" + ".csv");

            if (!visualized) {
                RouteVisualizer.visualizeRoute(blueRoute, "Blue Route", Bus.getAllBuses().size());
            }
            visualized = true;
            Bus.allBuses = new ArrayList<>();
            Bus.allBusesMap = new HashMap<>();
            Passenger.deadPassengers = new ArrayList<>();
            Passenger.numPassengers = 0;
            BusStop.allBusStops = new ArrayList<>();
            BusStop.allBusStopsMap = new HashMap<>();
            BusStop.allowedBuses = new HashMap<>();
            BusStop.allPopularityScores = new ArrayList<>();


    }
}