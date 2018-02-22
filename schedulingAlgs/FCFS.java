package schedulingAlgs;

import java.util.ArrayList;

/**
 * Created by CaioMoraes on 20/03/2017.
 */
public class FCFS extends SchedulingAlgorithm{

    /*Constructor*/
    public FCFS(ArrayList<Process> processList){
        super(processList);
        name = "FCFS";
    }


    /**
     * Running method
     */
    public void run(){

        //Empty input
        if(processList.isEmpty()){
            System.out.println("FCFS: Empty input");
            return;
        }

        //Safe copy list
        ArrayList<Process> copyList = Process.copyProcessList(processList, false);
        copyList.sort(Process.ARRIVAL_TIME_COMPARATOR);

        //Starts off at first process's arrival time
        timer = copyList.get(0).arrivalTime;

        for (Process process : copyList) {
            //Fixing Timer
            if (timer < process.arrivalTime) {
                timer = process.arrivalTime;
            }

            //Running current process
            process.waitingTime = timer - process.arrivalTime;
            process.turnaroundTime = process.waitingTime + process.executionTime;
            //in FCFS: response == turn around
            process.responseTime = process.turnaroundTime;

            //Part of average times calculations
            avgWaitingTime += process.waitingTime;
            avgResponseTime += process.responseTime;
            avgTurnaroundTime += process.turnaroundTime;

            //Increasing timer
            timer += process.executionTime;
        }

        //Finishing average times calculations
        avgWaitingTime /= copyList.size();
        avgResponseTime /= copyList.size();
        avgTurnaroundTime /= copyList.size();

        //Printing
        System.out.println(this.toString());
        Process.printProcessList(copyList);

        //Storing results
        processList = Process.copyProcessList(copyList, true);
    }
}
