package schedulingAlgs;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CaioMoraes on 20/03/2017.
 */
public class RR extends SchedulingAlgorithm{

    /*Fields*/
    private int quantum;


    /*Constructor*/
    public RR(ArrayList<Process> processList, int quantum){
        super(processList);
        name = "RR";
        this.quantum = quantum;
    }

    /**
     * Running method
     */
    public void run(){
        //Empty input
        if(processList.isEmpty()){
            System.out.println("RR: Empty input.");
            return;
        }

        //Avoiding infinite loop
        if(quantum <= 0){
            System.out.println("Quantum can't be " + quantum + ".");
            return;
        }

        //Lists:
        // waitingList => safe copy from input
        // runningProcesses => list of processes whose timer has past its arrival time
        ArrayList<Process> waitingList = Process.copyProcessList(processList, false);
        ArrayList<Process> runningProcesses = new ArrayList<>();

        //Sorting
        waitingList.sort(Process.ARRIVAL_TIME_COMPARATOR);

        //Each cycle contains 1 quantum of each process in the queue of ready processes
        for (int cycle = 0; waitingList.size() > 0 || !Process.isFinished(runningProcesses); cycle++) {

            System.out.println("\n" + "----CYCLE " + cycle + "\t(Timer: " + timer+")----\n");

            //Fixing timer
            if (!waitingList.isEmpty() && Process.isFinished(runningProcesses)){
                System.out.println("Adjusting timer");
                timer = waitingList.get(0).arrivalTime;
                System.out.println("New timer value: " + timer + "\n");
            }

            //Fetching new processes from waitingList to runningProcesses
            for (Iterator<Process> i = waitingList.iterator(); i.hasNext();)
            {
                Process currentProcess = i.next();
                System.out.println("Checking process's arrival time...");

                //Checking if process should enter in runningProcesses
                if (timer >= currentProcess.arrivalTime)
                {
                    System.out.println("Moving process to running processes list.");
                    System.out.println("\t(Arrival: "+currentProcess.arrivalTime+" \tExecution: "+currentProcess.executionTime+")");
                    //Moves process from the copy list to running processes list
                    Process copyProcess = Process.copyProcess(currentProcess);

                    //Taking into account the time past during last cycle
                    copyProcess.waitingTime = timer - copyProcess.arrivalTime;
                    copyProcess.turnaroundTime = copyProcess.waitingTime;

                    runningProcesses.add(copyProcess);
                    i.remove(); //preventing duplicates
                }
            }

            //Running processes
            System.out.println();
            System.out.println("Iterating queue:");
            for (int i = 0; i < runningProcesses.size(); i++)
            {
                //ExecutionTime property here works as a reserve of time
                if(runningProcesses.get(i).executionTime > 0)
                {
                    System.out.println("\tRunning process:\n\t\t" + runningProcesses.get(i));
                    /*updateValues:
                    * timer +
                    * others waiting and turnaround times +
                    * own execution time -
                    * sets response time (if not set)
                    * */
                    if(runningProcesses.get(i).executionTime >= quantum){
                        updateValues(runningProcesses, i, quantum);
                    }
                    //Prevents miscalculations in case quantum is bigger than execution time left.
                    else {
                        updateValues(runningProcesses, i, runningProcesses.get(i).executionTime);
                    }
                    System.out.println("\t\tTime to finish: " + runningProcesses.get(i).executionTime+"\n");
                }
            }
        }

        //Calculating average time
        for (int i = 0; i < runningProcesses.size(); i++) {
            avgWaitingTime += runningProcesses.get(i).waitingTime;
            avgResponseTime += runningProcesses.get(i).responseTime;
            avgTurnaroundTime += runningProcesses.get(i).turnaroundTime;
            runningProcesses.get(i).executionTime = processList.get(i).executionTime;
        }
        avgWaitingTime /= runningProcesses.size();
        avgResponseTime /= runningProcesses.size();
        avgTurnaroundTime /= runningProcesses.size();

        //Printing
        System.out.println(this.toString());
        Process.printProcessList(runningProcesses);

        //Storing results
        processList = Process.copyProcessList(runningProcesses, true);
    }


    /**
     * Updates (adds or subtracts) every value of every process that's still running
     * @param processList - process list to work on
     * @param idx - index of current process
     * @param value - time value to be increased or decreased
     */
    private void updateValues(ArrayList<Process> processList, int idx, int value){
        timer += value;
        setResponseTime(processList.get(idx), value);
        increaseOthersWaitingTime(processList, idx, value);
        updateTurnaroundTime(processList, idx, value);
    }


    /**
     * Increase waiting time value of every process that's still running
     * @param processList - process list to work on
     * @param idx - index of current process
     * @param value - time value to be increased
     */
    private static void increaseOthersWaitingTime(ArrayList<Process> processList, int idx, int value){
        for (int i = 0; i < processList.size(); i++) {
            if(i != idx && processList.get(i).executionTime > 0){
                processList.get(i).waitingTime += value;
            }
        }
    }


    /**
     *Updates (adds or subtracts) every turn around time value of every process that's still running
     * (including the current process's)
     * @param processList - process list to work on
     * @param idx - index of current process
     * @param value - time value to be increased or decreased
     */
    private static void updateTurnaroundTime(ArrayList<Process> processList, int idx, int value){
        for (Process process : processList) {
            if (process.executionTime > 0) {
                process.turnaroundTime += value;
            }
        }
        processList.get(idx).executionTime -= value;
    }


    /**
     *Sets response of a process only for the first time
     * @param process - process to set response time
     * @param value - time value to be set
     */
    private static void setResponseTime(Process process, int value){
        if(process.responseTime == 0 && value > 0){
            process.responseTime = value;
        }
    }


    @Override
    public String toString() {
        return super.toString() +
                "\nQuantum: " + this.quantum;
    }
}
