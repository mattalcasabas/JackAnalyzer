import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {
    public String outputFile;
    
    public void setOutputFile(String output) {
        this.outputFile = output;
    }

    public String getOutputFile() {
        return this.outputFile;
    }

    public void writeOutputLine(String line) {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(this.outputFile, true));
            if (line != null) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
