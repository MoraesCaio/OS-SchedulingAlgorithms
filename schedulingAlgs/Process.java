package schedulingAlgs;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by CaioMoraes on 20/03/2017.
 */
public class Process {

    /*Fields*/
    int arrivalTime;
    int executionTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;


    /*Constructors*/
    Process(int arrivalTime, int executionTime){
        this.arrivalTime = arrivalTime;
        this.executionTime = executionTime;
        this.turnaroundTime = 0;
        this.responseTime = 0;
        this.waitingTime = 0;
    }


    /*Full Constructor*/
    private Process(int arrivalTime, int executionTime, int turnaroundTime, int responseTime, int waitingTime){
        this(arrivalTime, executionTime);
        this.turnaroundTime = turnaroundTime;
        this.responseTime = responseTime;
        this.waitingTime = waitingTime;
    }


    /*Copy methods*/
    /**
     *Copies process, creating a new process
     * @param process - process to be copied
     * @return a new copy of the process
     */
    static Process copyProcess(Process process){
        return new Process(process.arrivalTime, process.executionTime);
    }


    /**
     * Copies process from a process list, creating a new process
     * @param processList - array list containing the process to be copied
     * @param idx - index of the process to be copied on the list
     * @return a new copy of the process
     */
    static Process copyProcess(ArrayList<Process> processList, int idx){
        return new Process(processList.get(idx).arrivalTime, processList.get(idx).executionTime);
    }


    /**
     * Copies process return a new process with same properties (all of them)
     * @param process - process to be copied
     * @return a new copy of the process
     */
    private static Process fullyCopyProcess(Process process){
        return new Process(process.arrivalTime,
                                    process.executionTime,
                                    process.turnaroundTime,
                                    process.responseTime,
                                    process.waitingTime
        );
    }


    /**
     * Copies process list, creating a new process list
     * @param processList - process list to be copied
     * @param fullCopy - whether, or not, it should copy turnaroundTime, responseTime and waitingTime
     * @return a new copy of the process list
     */
    static ArrayList<Process> copyProcessList(ArrayList<Process> processList, boolean fullCopy){
        ArrayList<Process> copyList = new ArrayList<>();
        if (fullCopy){
            for (Process process : processList) {
                copyList.add(fullyCopyProcess(process));
            }
        }
        else{
            for (int i = 0; i < processList.size(); i++) {
                copyList.add(copyProcess(processList, i));
            }
        }
        return copyList;
    }


    /*Auxiliary methods*/
    /**
     * Checks if every process on a list have finished its execution
     * @param processList - process list to be analyzed
     * @return true, if every process on the list is finished; false, otherwise
     */
    static boolean isFinished(ArrayList<Process> processList){
        boolean haveFinished = true;
        for (Process process : processList) {
            if (process.executionTime > 0) {
                haveFinished = false;
            }
        }
        return haveFinished;
    }


    /**
     * Checks if timer has past any process's arrival time. Used for adjusting timer in SJF's algorithm;
     *  P.S.: It also returns false when (timer == process.arrivalTime) !!!
     * @param processList - process list to be analyzed
     * @param timer - current timer's value
     * @return true, if timer's value is greater than any process's arrival time; false, otherwise.
     */
    static boolean havePastProcessesArrivalTime(ArrayList<Process> processList, int timer){
        for (Process process : processList) {
            if (timer > process.arrivalTime) {
                return true;
            }
        }
        return false;
    }


    /*Comparators*/
    static final Comparator<Process> EXECUTION_TIME_COMPARATOR = Comparator.comparingInt(p -> p.executionTime);
    static final Comparator<Process> ARRIVAL_TIME_COMPARATOR = Comparator.comparingInt(p -> p.arrivalTime);


    /*Printing methods*/
    @Override
    public String toString(){
        String fmt = "%4d";
        return "Arrival: " + String.format(fmt, arrivalTime) +
               " Execution: " + String.format(fmt, executionTime) +
               " \tTurnaround: " + String.format(fmt, turnaroundTime) +
               " Response: " + String.format(fmt, responseTime) +
               " Waiting: " + String.format(fmt, waitingTime);

    }


    /**
     * Print a process list
     * @param processList - process list to be printed
     */
    static void printProcessList(ArrayList<Process> processList){
        for (int i = 0; i < processList.size(); i++) {
            System.out.println("Process["+i+"]: " + processList.get(i));
        }
        System.out.println();
    }
}
