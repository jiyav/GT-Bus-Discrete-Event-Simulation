import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RouteVisualizer extends JPanel {


    /*
    public static void main(String[] args) {
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


        //set up routes such that there is a road segment alternating between other nodes
        //ArrayList<RouteNode> blueRoute = new ArrayList<>(Arrays.asList(stopA, r1, t1, r2, stopB, r3, t2, r4, stopC, r5, t3, r6));
        visualizeRoute(redRoute, "Test Route", 1);
    }

     */





    //blue circle for the route
    //title of the screen is the route
    //

    public static void visualizeRoute(ArrayList<RouteNode> route, String routeName, int input) {


        double sum = 0;
        for (RouteNode node: route) {
            if (node instanceof RoadSegment) {
                sum += ((RoadSegment) node).getDistance()*50;
            } else if (node instanceof TrafficLight) {
                sum+=15;
            } else {
                sum+=20;
            }
        }

        //System.out.println(sum);
        double ratio = 600/sum;

        //System.out.println(ratio);

        int runningXValue = 50;

        List<Rectangle> tempList = new ArrayList<Rectangle>();

        for (RouteNode node: route) {
            if (node instanceof RoadSegment) {
                int width = (int) (((RoadSegment) node).getDistance()*50*ratio);

                tempList.add(new Rectangle(runningXValue, 50, width, 100, "red"));
                runningXValue += width;
            } else if (node instanceof TrafficLight) {
                int width = (int) (15*ratio);

                tempList.add(new Rectangle(runningXValue, 50, width, 100, "green"));
                runningXValue += width;
            } else {
                int width = (int) (20*ratio);

                tempList.add(new Rectangle(runningXValue, 50, width, 100, "blue"));
                runningXValue += width;
            }
        }

        JFrame frame = new JFrame(routeName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.getContentPane().setBackground( Color.lightGray );
        frame.getContentPane().add(new ShapeDrawing (tempList, input));
        frame.setVisible(true);

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
        }

        frame.dispose();

    }

}

class Rectangle {

    public int a;
    public int b;
    public int c;
    public int d;
    public String color;


    public Rectangle(int a, int b, int c, int d, String color) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.color=color;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", color='" + color + '\'' +
                '}';
    }
}


class ShapeDrawing extends JComponent {

    private List<Rectangle> temp;
    private int input;

    public ShapeDrawing(List<Rectangle> temp, int input) {
        this.temp = temp;
        this.input = input;
    }

    public void paint(Graphics g)
    {
        //System.out.println(temp.get(0).toString());
        Graphics2D g2 = (Graphics2D) g;

        for (Rectangle rect: temp) {
            if (rect.color.equals("blue")) {
                g2.setColor(Color.BLUE);
            } else if (rect.color.equals("green")) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.RED);
            }

            g2.drawRect(rect.a, rect.b, rect.c, rect.d);
            g2.fillRect(rect.a, rect.b, rect.c, rect.d);

            //System.out.println(rect.a);
            //System.out.println(rect.c);
        }

        g2.setColor(Color.BLUE);
        g2.drawOval(500,220,30,30);
        g2.fillOval(500,220,30,30);
        g2.setColor(Color.RED);
        g2.drawOval(500,270,30,30);
        g2.fillOval(500,270,30,30);
        g2.setColor(Color.GREEN);
        g2.drawOval(500,320,30,30);
        g2.fillOval(500,320,30,30);

        g2.drawChars("Traffic Light".toCharArray(),0,13,550,330);



        g2.setColor(Color.BLUE);
        g2.drawChars("Bus Stop".toCharArray(),0,8,550,230);

        g2.setColor(Color.RED);
        g2.drawChars("Road".toCharArray(),0,4,550,280);

        String busCount;
        if (input == 1) {
            busCount = "Count:" + input + " bus";
        } else if (input == 0) {
            busCount = "";
        } else {
            busCount = "Count:" + input + " buses";
        }
        g2.setColor(Color.BLACK);
        g2.drawChars(busCount.toCharArray(),0,busCount.length(),250,280);
    }
}
