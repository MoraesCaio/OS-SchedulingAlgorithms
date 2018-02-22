package schedulingAlgs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Created by CaioMoraes on 20/04/2017.
 */
public class ProcessListEntry {
    /*Files*/
    private String entryFile;
    private String outputFile;


    /*Constructor*/
    public ProcessListEntry(String entryFile, String outputFile) throws IOException {
        //Checking if input file exists
        if (Files.notExists(Paths.get(entryFile))){
            throw new IOException("File doesn't exist.");
        }
        this.entryFile = entryFile;
        this.outputFile = outputFile;
    }


    /**
     *Running method
     * @param runFCFS - whether, or not, FCFS should be run
     * @param runSJF - whether, or not, SJF should be run
     * @param runRR - whether, or not, RR should be run
     */
    public void runAlgorithms(boolean runFCFS, boolean runSJF, boolean runRR){
        //Reading input file
        ArrayList<String> lines;
        try {
            lines = (ArrayList<String>) Files.readAllLines(Paths.get(entryFile));
        }
        catch (IOException ioEx){
            ioEx.printStackTrace();
            return;
        }

        //Parsing
        ArrayList<Process> inputProcessList = new ArrayList<>();
        String[] aux;
        for (String line : lines) {
            aux = line.split(" ");
            if (aux.length >= 2) {
                //arrivalTime and executionTime
                inputProcessList.add(new Process(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
            } else {
                System.out.println("Invalid input on line: " + line);
                System.out.println("Please write using the following format:\n(int arrivalTime) (int executionTime)");
                return;
            }
        }

        //Printing initial process list
        System.out.println("Process list from input:");
        Process.printProcessList(inputProcessList);

        //Setting algorithms
        ArrayList<SchedulingAlgorithm> schAlgs = new ArrayList<>();
        if (runFCFS) schAlgs.add(new FCFS(inputProcessList));
        if (runSJF) schAlgs.add(new SJF(inputProcessList));
        if (runRR) schAlgs.add(new RR(inputProcessList, 2));

        //Running algorithms
        for (SchedulingAlgorithm schAlg : schAlgs) {
            schAlg.run();
        }

        //Writing output file
        try{
            //Checking if file doesn't exist
            if (Files.notExists(Paths.get(outputFile))) {
                Files.createFile(Paths.get(outputFile));
            }
            //Printing results
            Files.write(Paths.get(outputFile), "".getBytes()); //Clearing file
            for (SchedulingAlgorithm schAlg : schAlgs) {
                System.out.print(schAlg.getResultString());
                Files.write(Paths.get(outputFile), schAlg.getResultString().getBytes(), StandardOpenOption.APPEND);
            }
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

}
