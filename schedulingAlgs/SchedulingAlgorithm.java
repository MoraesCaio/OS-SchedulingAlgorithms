package schedulingAlgs;

import java.util.ArrayList;

/**
 * Created by CaioMoraes on 23/03/2017.
 */
public abstract class SchedulingAlgorithm {

    /*Variables*/
    ArrayList<Process> processList;
    float avgTurnaroundTime;
    float avgResponseTime;
    float avgWaitingTime;
    String name;
    int timer;


    /*Constructor*/
    SchedulingAlgorithm(ArrayList<Process> processList){
        this.processList = processList;
        avgTurnaroundTime = 0;
        avgResponseTime = 0;
        avgWaitingTime = 0;
        timer = 0;
    }


    /**
     * Running method
     */
    public abstract void run();


    /**
     *Get method of the string for quick analysis.
     * @return short string containing only the information on the results.
     */
    String getResultString(){
        String fmt = "%.1f";
        return name + " " + String.format(fmt, avgTurnaroundTime) + " " + String.format(fmt, avgResponseTime) + " " + String.format(fmt, avgWaitingTime) + "\n";
    }


    public String toString() {
        String fmt = "%.1f";
        return  name +
                "\nAverage turnaround time: " + String.format(fmt, avgTurnaroundTime) +
                "\nAverage response time: " + String.format(fmt, avgResponseTime) +
                "\nAverage waiting time: " + String.format(fmt, avgWaitingTime);
    }
}
