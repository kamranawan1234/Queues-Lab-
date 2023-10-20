import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * @author Kamran Awan
 * Simulate the check-in process of an airline.
 * */
public class AirlineCheckinSim extends JOptionPane
{
// Data Fields
    /** Queue of frequent flyers. */
    private PassengerQueue frequentFlyerQueue = new PassengerQueue("Frequent Flyer"); /** Queue of regular passengers. */
    private PassengerQueue regularPassengerQueue = new PassengerQueue("Regular Passenger"); /** Maximum number of frequent flyers to be served
     before a regular passenger gets served. */
    private int frequentFlyerMax;
    /** Maximum time to service a passenger. */
    private int maxProcessingTime;
    /** Total simulated time. */
    private int totalTime;
    /** If set true, print additional output. */
    private boolean showAll;
    /** Simulated clock. */
    private int clock = 0;
    /** Time that the agent will be done with the current passenger.*/
    private int timeDone;
    /** Number of frequent flyers served since the
     last regular passenger was served. */
    private int frequentFlyersSinceRP;

    /** Main method.
     @param args Not used
     */
    public static void main(String args[])
    {
        AirlineCheckinSim sim = new AirlineCheckinSim();
        sim.enterData();
        sim.runSimulation();
        sim.showStats();
        System.exit(0);
    }

    /**
     * Lets user enter the data for the variables.
     */
    private void enterData()
    {
        frequentFlyerQueue.setArrivalRate(Double.parseDouble(JOptionPane.showInputDialog("Expected number of frequent flyer arrivals per hour: "))/60);
        regularPassengerQueue.setArrivalRate(Double.parseDouble(JOptionPane.showInputDialog("Expected number of regular passenger arrivals per hour: "))/60);
        frequentFlyerMax = Integer.parseInt(JOptionPane.showInputDialog("The maximum number of frequent flyers served between regular passengers: "));
        maxProcessingTime = Integer.parseInt(JOptionPane.showInputDialog("Maximum service time in minutes: "));
        Passenger.setMaxProcessingTime(maxProcessingTime);
        totalTime = Integer.parseInt(JOptionPane.showInputDialog("Total simulation time in minutes: "));
        showAll = JOptionPane.showInputDialog("Flag. If true, display minute-by-minute trace of simulation: ").equalsIgnoreCase("y");
    }

    /**
     * Runs the simulation for the passengers.
     */
    private void runSimulation()
    {
        for (clock = 0; clock < totalTime; clock++)
        {
            frequentFlyerQueue.checkNewArrival(clock, showAll);
            regularPassengerQueue.checkNewArrival(clock, showAll);
            if (clock >= timeDone)
            {
                startServe();
            }
        }
    }

    /**
     * Serves the passengers, frequent passengers are served before regular ones.
     */
    private void startServe()
    {
        if (!frequentFlyerQueue.isEmpty() && ((frequentFlyersSinceRP <= frequentFlyerMax) || regularPassengerQueue.isEmpty()))
        {
            // Serve the next frequent flyer.
            frequentFlyersSinceRP++;
            timeDone = frequentFlyerQueue.update(clock, showAll);
        }
        else if (!regularPassengerQueue.isEmpty())
        {
            // Serve the next regular passenger.
            frequentFlyersSinceRP = 0;
            timeDone = regularPassengerQueue.update(clock, showAll);
        }
        else if (showAll)
        {
            System.out.println("Time is " + clock + " server is idle");
        }
    }

    /**
     * Method to show the statistics.
     */
    private void showStats()
    {
        System.out.println("\nThe number of regular passengers served was " + regularPassengerQueue.getNumServed());
        double averageWaitingTime = (double) regularPassengerQueue.getTotalWait() / (double) regularPassengerQueue.getNumServed();

        System.out.println(" with an average waiting time of " + averageWaitingTime);
        System.out.println("The number of frequent flyers served was " + frequentFlyerQueue.getNumServed());
        double averageWaitingTime2 = (double) frequentFlyerQueue.getTotalWait() / (double) frequentFlyerQueue.getNumServed();
        System.out.println(" with an average waiting time of " + averageWaitingTime2);
        System.out.println("Passengers in frequent flyer queue: " + frequentFlyerQueue.size());
        System.out.println("Passengers in regular passenger queue: " + regularPassengerQueue.size());
    }

}

