import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JackAnalyzer {
    public static void main(String[] args) throws IOException {
        InputFile in = new InputFile();
        OutputFile out = new OutputFile();
        Tokenizer t = new Tokenizer(in, out);
        // CompilationEngine c = new CompilationEngine();

        String inFilePath = "./Main.jack";
        String outFilePath = "./outfile.xml";
        in.setInputFile(inFilePath);
        out.setOutputFile(outFilePath);

        int lineCount = 0;
        
        try {
            lineCount = in.countLines();
            System.out.println("Number of lines: " + lineCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        t.writeOpenXml();

        for (int currentLine = 1; currentLine <= lineCount; currentLine++) {
            t.processLine(currentLine);
        }

        t.writeCloseXml();

    }
}