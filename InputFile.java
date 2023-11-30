import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class InputFile {
    public String inputFile;

    public void setInputFile(String input) {
        this.inputFile = input;
    }

    public String getInputFile() {
        return this.inputFile;
    }

    public String readLineFromFile(int lineNumber) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.getInputFile()));
        String line = null;
        int currentLine = 0;

        while ((line = reader.readLine()) != null) {
            currentLine++;

            if (currentLine == lineNumber) {
                reader.close();
                return line;
            }
        }

        reader.close();
        return null;
    }

    public boolean hasMoreCommands(LineNumber ln) {
        try {
            if (readLineFromFile(ln.getLineNumber()) == null) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    
    public int countLines() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.getInputFile()))) {
            int lineCount = 0;

            while (reader.readLine() != null) {
                lineCount++;
            }

            return lineCount;
        }
    }
}
