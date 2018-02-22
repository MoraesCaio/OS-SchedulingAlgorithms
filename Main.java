import schedulingAlgs.ProcessListEntry;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<ProcessListEntry> fileList = new ArrayList<>();

        try {
            fileList.add(new ProcessListEntry("entry.csv", "output.txt"));
            fileList.add(new ProcessListEntry("entry1.csv", "output1.txt"));
            fileList.add(new ProcessListEntry("entry2.csv", "output2.txt"));
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        for (ProcessListEntry file : fileList) {
            file.runAlgorithms(true, true, true);
        }
    }
}
