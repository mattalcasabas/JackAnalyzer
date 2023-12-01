import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private InputFile input;
    private OutputFile output;
    private String line = "";
    private LineNumber ln = new LineNumber();
    private Pattern keywordPattern = Pattern.compile("\\b(?:class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)\\b");
    private Pattern symbolPattern = Pattern.compile("[{}\\[\\]().,;\\+\\-*/&|<>=~]");
    private Pattern integerPattern = Pattern.compile("\\b\\d+\\b");
    private Pattern stringPattern = Pattern.compile("\"[^\"]*\"");
    private Pattern identifierPattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private Matcher matcher;
    private List<Token> tokens = new ArrayList<>();



    public Tokenizer(InputFile inFile, OutputFile outFile) {
        this.input = inFile;
        this.output = outFile;
        ln.setLineNumber(1);
    }

    public List<Token> getTokens() {
        return this.tokens;
    }
    
    public String trimComment(String input) {
        // Remove comments starting with //
        input = input.replaceAll("//.*", "");
    
        // Remove comments starting with /* and ending with */
        input = input.replaceAll("/\\*.*?\\*/", "");
    
        return input.trim();
    }

    public String trimWhitespace(String input) {
        return input.trim();
    }
    
    public void processLine(int lineNum) throws IOException {
        line = input.readLineFromFile(lineNum);
        System.out.println("Line: " + line);

    
        // Skip empty lines or lines that are just comments
        if (line == null || line.trim().isEmpty() || line.trim().startsWith("//") || line.trim().startsWith("/*")) {
            return;
        }

        // create matcher for the current line
        matcher = Pattern.compile(keywordPattern.pattern() + "|" +
                                         identifierPattern.pattern() + "|" +
                                         integerPattern.pattern() + "|" +
                                         stringPattern.pattern() + "|" +
                                         symbolPattern.pattern())
                                        .matcher(line);
    
        // Trim comments and whitespace
        line = trimComment(line);
        line = trimWhitespace(line);
    
        // Tokenize the line and store the tokens
        tokenizeLine();
    }

    public void printTokensToConsole() {
        for (Token token : tokens) {
            System.out.println("Type: " + token.getTokenType() + ", Token: " + token.getToken());
        }
    }

    public void printTokensToFile() {
        writeOpenXml();
        for (Token token : tokens) {
            String tokenType = token.getTokenType();
            String tokenValue = token.getToken();
            output.writeOutputLine("<" + tokenType + "> " + escapeXml(tokenValue) + " </" + tokenType + ">");
        }
        writeCloseXml();
    }

    public void tokenizeLine() {
        String tokenType = "";
        String token = "";
        while (matcher.find()) {
            token = matcher.group();
            if (keywordPattern.matcher(token).matches()) {
                tokenType = "keyword";
            } else if (identifierPattern.matcher(token).matches()) {
                tokenType = "identifier";
            } else if (integerPattern.matcher(token).matches()) {
                tokenType = "integerConstant";
            } else if (stringPattern.matcher(token).matches()) {
                tokenType = "stringConstant";
            } else if (symbolPattern.matcher(token).matches()) {
                tokenType = "symbol";
            } else {
                tokenType = "UNKNOWN";
            }
            Token tokenObject = new Token(token, tokenType);
            tokens.add(tokenObject);
        }
    }
    
    public void writeOpenXml() {
        output.writeOutputLine("<tokens>");
    }

    public void writeCloseXml() {
        output.writeOutputLine("</tokens>");
    }

    private String escapeXml(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "")
                .replace("'", "");
    }
}