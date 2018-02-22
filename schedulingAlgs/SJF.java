package schedulingAlgs;

import java.util.ArrayList;

/**
 * Created by CaioMoraes on 20/03/2017.
 */
public class SJF extends SchedulingAlgorithm{

    /*Constructor*/
    public SJF(ArrayList<Process> processList){
        super(processList);
        name = "SJF";
    }


    /**
     * Running method
     */
    public void run(){

        //Empty input
        if(processList.isEmpty()){
            System.out.println("SJF: Empty input");
            return;
        }

        //Lists
        ArrayList<Process> waitingList = Process.copyProcessList(processList, false);
        ArrayList<Process> runningProcesses = new ArrayList<>();
        ArrayList<Process> toBeSorted = new ArrayList<>(); //Used for sorting before moving processes from waitingList to runningProcesses

        //Sorting
        waitingList.sort(Process.ARRIVAL_TIME_COMPARATOR);

        //CREATING QUEUE
        //Each cycle contains 1 quantum of each process in the queue of ready processes
        System.out.println("SJF fetching n' sorting:");
        for (int cycle = 0; waitingList.size() > 0 || toBeSorted.size() > 0; cycle++) {
            System.out.println("\n" + "----CYCLE " + cycle + "\t(Timer: " + timer+")----\n");

            //Fixing timer
            if (!waitingList.isEmpty() && toBeSorted.isEmpty() && !Process.havePastProcessesArrivalTime(waitingList, timer)){
                timer = waitingList.get(0).arrivalTime;
                System.out.println("Adjusting timer");
                System.out.println("New timer value: " + timer + "\n");
            }

            //FETCHING NEW PROCESSES FROM WAITING LIST
            System.out.println("Timer: "+timer);
            System.out.println("Fetching new processes");

            //Fetching
            for (Process process : waitingList) {
                System.out.println("Checking process arrival time...");
                if (timer >= process.arrivalTime) {
                    System.out.println("\tTimer is equal/greater: adding process process:");
                    System.out.println("\t\tArrival: " + process.arrivalTime + "\tExecution: " + process.executionTime);
                    toBeSorted.add(Process.copyProcess(process));
                }
            }

            System.out.println("\n");

            //Removing added processes from temporary list
            waitingList.removeIf(p -> timer >= p.arrivalTime);

            //Sorting by execution time
            toBeSorted.sort(Process.EXECUTION_TIME_COMPARATOR);

            //Moving the process with minimum execution time
            runningProcesses.add(Process.copyProcess(toBeSorted, 0));
            toBeSorted.remove(0);

            //Running the process with minimum execution time
            Process currentProcess = runningProcesses.get(runningProcesses.size()-1);
            System.out.println("Running process:");
            System.out.println("\tArrival: "+currentProcess.arrivalTime+"\tExecution: "+currentProcess.executionTime);

            //Updating its values
            currentProcess.waitingTime = timer - currentProcess.arrivalTime;
            currentProcess.turnaroundTime = timer + currentProcess.executionTime;
            currentProcess.responseTime = currentProcess.turnaroundTime;

            //Updating timer by last process's execution time on runningProcesses
            timer += currentProcess.executionTime;
        }

        System.out.println();

        //Calculating average times
        for (Process runningProcess : runningProcesses) {
            avgWaitingTime += runningProcess.waitingTime;
            avgResponseTime += runningProcess.responseTime;
            avgTurnaroundTime += runningProcess.turnaroundTime;
        }
        avgWaitingTime /= runningProcesses.size();
        avgResponseTime /= runningProcesses.size();
        avgTurnaroundTime /= runningProcesses.size();

        //Printing
        System.out.println(this.toString());
        System.out.println("Process list (in order of execution):");
        Process.printProcessList(runningProcesses);

        //Storing results
        processList = Process.copyProcessList(runningProcesses, true);
    }
}
