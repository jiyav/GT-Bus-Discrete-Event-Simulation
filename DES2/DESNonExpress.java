/*
For simplicity, traffic is left constant.
Goal is to find optimal number of buses for no traffic
Passengers are set to low
No express routes
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

//have a schedule somehwere like of how many buses in the simulation at a given time
 public class DESNonExpress {
    public static void main(String[] args) {
        boolean visualized = false;
            //Nav
        BusStop stopA = new BusStop("S1", 0.6);
        stopA.init();
        RoadSegment r1 = new RoadSegment("R1", 0.3);
        TrafficLight t1 = new TrafficLight("T1");
        RoadSegment r2 = new RoadSegment("R2", 0.9);

        //Cherry street
        BusStop stopB = new BusStop("S2", 0.3);
        stopB.init();
        RoadSegment r3 = new RoadSegment("R3", 0.3);

        //Transit terminal
        BusStop stopC = new BusStop("S3", 0.4);
        stopC.init();
        RoadSegment r4 = new RoadSegment("R4", 0.5);

        //Student Center
        BusStop stopD = new BusStop("S4", 0.2);
        stopD.init();
        RoadSegment r5 = new RoadSegment("R5", 0.8);
        TrafficLight t2 = new TrafficLight("T2");
        RoadSegment r6 = new RoadSegment("R6", 0.1);
//CRC
        BusStop stopE = new BusStop("S5", 0.6);
        stopE.init();
        RoadSegment r7 = new RoadSegment("R7", 0.6);
        TrafficLight t3 = new TrafficLight("T3");
        RoadSegment r8 = new RoadSegment("R8", 0.7);

        //Turner Place
        BusStop stopF = new BusStop("S6", 0.3);
        stopF.init();
        RoadSegment r9 = new RoadSegment("R9", 0.9);

        //Eighth Street
        BusStop stopG = new BusStop("S7", 0.2);
        stopG.init();
        RoadSegment r10 = new RoadSegment("R10", 0.2);

        //Manufacturing insitutue
        BusStop stopH = new BusStop("S8", 0.3);
        stopH.init();
        RoadSegment r11 = new RoadSegment("R11", 0.3);
        TrafficLight t4 = new TrafficLight("T4");
        RoadSegment r12 = new RoadSegment("R12", 0.1);

        //Kendeda
        BusStop stopI = new BusStop("S9", 0.6);
        stopI.init();
        RoadSegment r13 = new RoadSegment("R13", 0.3);
//cherry emerson
        BusStop stopJ = new BusStop("S10", 0.3);
        stopJ.init();
        RoadSegment r14 = new RoadSegment("R14", 0.5);

        //Klaus
        BusStop stopK = new BusStop("S11", 0.4);
        stopK.init();
        RoadSegment r15 = new RoadSegment("R15", 0.2);

        //Baseball field
        BusStop stopL = new BusStop("S12", 0.1);
        stopL.init();
        RoadSegment r16 = new RoadSegment("R16", 0.1);
        TrafficLight t5 = new TrafficLight("T5");
        RoadSegment r17 = new RoadSegment("R17", 0.3);

        //Kappa Alpha
        BusStop stopM = new BusStop("S13", 0.3);
        stopM.init();
        RoadSegment r18 = new RoadSegment("R18", 0.4);

        //Frats
        BusStop stopN = new BusStop("S14", 0.4);
        stopN.init();
        RoadSegment r19 = new RoadSegment("R19", 0.2);

        //Freshman Hill
        BusStop stopO = new BusStop("S15", 0.5);
        stopO.init();
        RoadSegment r20 = new RoadSegment("R20", 0.1);
        TrafficLight t6 = new TrafficLight("T6");
        RoadSegment r21 = new RoadSegment("R21", 0.6);

        //Intersection
        BusStop stopP = new BusStop("S16", 0.1);
        stopP.init();
        RoadSegment r22 = new RoadSegment("R22", 0.1);
        TrafficLight t7 = new TrafficLight("T7");
        RoadSegment r23 = new RoadSegment("R23", 0.3);

        ArrayList<RouteNode> redRoute = new ArrayList<>(Arrays.asList(stopA, r1, t1, r2, stopB, r3, stopC, r4, stopD, r5, t2, r6, stopE, r7,t3,r8,stopF, r9, stopG, r10, stopH, r11, t4, r12, stopI, r13, stopJ, r14, stopK, r15, stopL, r16, t5, r17, stopM, r18, stopN, r19, stopO, r20, t6, r21, stopP, r22, t7, r23));

        Map<String, ArrayList<RouteNode>> routes = new HashMap<>(); 
        routes.put("Red", redRoute);
        Bus.setRoutes(routes);
        Bus.setBusStops();
        BusStop.setAllowedBuses(routes);  


        //change bus initialization so that it begins at a bus stop, not any route node
        // BusStop bsBlue = Bus.selectRandomBusStop(randomBusStopIndexBlue);
        // System.out.println(randomBusStopIndexBlue);
        // System.out.println(bsBlue);
        

        // int randomBusStopIndexRed = Bus.selectRandomBusStop("Red");
        // BusStop bsRed = Bus.selectRandomBusStop(randomBusStopIndexRed);
        // System.out.println(randomBusStopIndexRed);
        // System.out.println(bsRed);
        // randomBusStopIndexRed = Bus.rot

        // int randomBusStopIndexRed2 = Bus.selectRandomBusStop("Red");
        // BusStop bsRed2 = Bus.selectRandomBusStop(randomBusStopIndexRed);
        ArrayList<Bus> busList = new ArrayList<>();
        for (int j = 0; j <= 3; j++) {
            busList.add(new Bus("redbus" + j, "Red", redRoute));
            busList.get(j).init("Red");
        }
        
        Map<Integer, Double> trafficIndex = new HashMap<Integer, Double>(); 
            //maybe traffic should be like extra minutes added to the base travel time
            //super busy from 10-2, and 5-8
            trafficIndex.put(7, 2.0);
            trafficIndex.put(8, 2.0);
            trafficIndex.put(9, 2.0);
            trafficIndex.put(10, 2.0);
            trafficIndex.put(11, 2.0);
            trafficIndex.put(12, 2.0);
            trafficIndex.put(13, 2.0);
            trafficIndex.put(14, 2.0);
            trafficIndex.put(15, 2.0);
            trafficIndex.put(16, 2.0);
            trafficIndex.put(17, 2.0);
            trafficIndex.put(18, 2.0);
            trafficIndex.put(19, 2.0);//7pm    

        RoadSegment.setTrafficCond(trafficIndex);

        Map<Integer, Integer> passengerTraffic = new HashMap<>(); 
            //this is all arbritary for now. we can honeslty guestimate this.
            //its just that we should be trying to inject new passengers like every few min
            passengerTraffic.put(7, 15);
            passengerTraffic.put(8, 15);
            passengerTraffic.put(9, 15);
            passengerTraffic.put(10, 15);
            passengerTraffic.put(11, 15);
            passengerTraffic.put(12, 15);
            passengerTraffic.put(13, 15);
            passengerTraffic.put(14, 15);
            passengerTraffic.put(15, 15);
            passengerTraffic.put(16, 15);
            passengerTraffic.put(17, 15);
            passengerTraffic.put(18, 15);
            passengerTraffic.put(19, 15);//7pm    

            Passenger.setPassengerTraffic(passengerTraffic);

            /*create initial passengers for all bus stops. the createPassenger() method is in
            ArriveAtBusStop event*/
            int time = 420;

            ArrayList<BusStop> allBusStops = BusStop.getAllBusStops();
            for (int j = 0; j < allBusStops.size(); j++) {
                ArriveAtBusStopEvent.createPassengers(time);
            }

            // for (int j = 0; j < allBusStops.size(); j++) {
            //     Map<String, ArrayList<Passenger>> initialPass = allBusStops.get(j).passenger;

                // for (Map.Entry<String, ArrayList<Passenger>> entry : initialPass.entrySet()) {
                //     String busType = entry.getKey();
                //     ArrayList<Passenger> value = entry.getValue();
                //     System.out.print("For this bus stop, " + allBusStops.get(j).getId() + " these are Passengers ");
                //     for (int i = 0; i < value.size(); i++) {
                //         System.out.println("Passenger with ID " + value.get(i).getId() + "riding on Bus " + value.get(i).getBus());
                //     }
                //     System.out.println();
                    
                // }

           // }

            System.out.println(Bus.getAllBuses());
            // SimulationExecutive simulation = new SimulationExecutive();
            for (int i = 0; i < Bus.getAllBuses().size(); i++) {
                System.out.println(Bus.getAllBuses().get(i).getId()+ "\'s FIRST STOP ASSIGNED: " + Bus.getAllBuses().get(i).getCurrStop());
                SimulationExecutive.schedule(new ArriveAtBusStopEvent(Bus.getAllBuses().get(i), (BusStop) Bus.getAllBuses().get(i).getCurrStop(), time));
            }
            // for (int j = 0; j < Bus.getAllBuses().size(); j++) {
            //     System.out.println(Bus.getAllBuses().get(j).getId()+ "\'s FIRST STOP ASSIGNED: " + Bus.getAllBuses().get(i).getCurrStop());
            //     SimulationExecutive.schedule(new ArriveAtBusStopEvent(Bus.getAllBuses().get(j), (BusStop) Bus.getAllBuses().get(j).getCurrStop(), time));
            // }



            SimulationExecutive.simulation();
            DataExport.writeBusStopDataToCSV(allBusStops, "BusStopsExpress.csv");
            ArrayList<ArrayList<Passenger>> deadPassengers = Passenger.getDeadPassengers();
            DataExport.writePassengerDataToCSV(deadPassengers, "PassengersExpress.csv");
            if (!visualized)
                RouteVisualizer.visualizeRoute(redRoute, "Red Route", Bus.getAllBuses().size());
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