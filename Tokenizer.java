import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private InputFile input;
    private OutputFile output;
    private String line = "";
    private LineNumber ln = new LineNumber();
    // private String tokenType = "";
    // private String token = "";
    private int tokenIndex = 0;
    private int lineNumber = 0;
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

    // public boolean hasMoreTokens() {
    //     try {
    //         return (line = input.readLine()) != null;
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return false;
    // }

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
        tokens = tokenizeLine();
    
        // Process the tokens as needed
        for (Token token : tokens) {
            System.out.println("Type: " + token.getTokenType() + ", Token: " + token.getToken());
            String tokenType = token.getTokenType();
            String tokenValue = token.getToken();
            output.writeOutputLine("<" + tokenType + "> " + escapeXml(tokenValue) + " </" + tokenType + ">");
        }
    }

    public List<Token> tokenizeLine() {
        String tokenType = "";
        String token = "";
        List<Token> tokens = new ArrayList<>();
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
        return tokens;
    }
    
    public void writeOpenXml() {
        output.writeOutputLine("<tokens>");
    }

    public void writeCloseXml() {
        output.writeOutputLine("</tokens>");
    }

    // public void writeTokensToFile(List<Token> tokens, String outputFile) {
    //     try (FileWriter writer = new FileWriter(outputFile)) {
    //         writer.write("<tokens>\n");

    //         for (Token token : tokens) {
    //             String tokenType = token.getTokenType();
    //             String tokenValue = token.getToken();

    //             writer.write("<" + tokenType + "> " + escapeXml(tokenValue) + " </" + tokenType.toLowerCase() + ">\n");
    //         }

    //         writer.write("</tokens>\n");
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    private String escapeXml(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "")
                .replace("'", "");
    }

    // public String tokenType(String token) {
    //     // test if the token is a keyword or symbol
    //     switch(token) {
    //         case "class":
    //         case "constructor":
    //         case "function":
    //         case "method":
    //         case "field":
    //         case "static":
    //         case "var":
    //         case "int":
    //         case "char":
    //         case "boolean":
    //         case "void":
    //         case "true":
    //         case "false":
    //         case "null":
    //         case "this":
    //         case "let":
    //         case "do":
    //         case "if":
    //         case "else":
    //         case "while":
    //         case "return":
    //             return "KEYWORD";
    //         case "{":
    //         case "}":
    //         case "(":
    //         case ")":
    //         case "[":
    //         case "]":
    //         case ".":
    //         case ",":
    //         case ";":
    //         case "+":
    //         case "-":
    //         case "*":
    //         case "/":
    //         case "&":
    //         case "|":
    //         case "<":
    //         case ">":
    //         case "=":
    //         case "~":
    //             return "SYMBOL";
    //         default:
    //             break;
    //     }

    //     // test if the token is an integer constant
    //     try {
    //         int number = Integer.parseInt(token);
    //         if (number >= 0 && number <= 32767) {
    //             return "INT_CONST";
    //         }
    //     } catch (NumberFormatException e) {
    //         // String not a valid integer
    //     }

    //     // test if the token is a string constant
    //     if(token != null && token.length() >= 2) {
    //         char firstChar = token.charAt(0);
    //         char lastChar = token.charAt(token.length() - 1);
    //         if(firstChar == '"' && lastChar == '"') {
    //             return "STRING_CONST";
    //         }
    //     }

    //     // test if the token is an identifier:
    //     // regex will match any string that contains letters, numbers,
    //     // or underscores, provided it does not start with a digit
    //     if(token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
    //         return "IDENTIFIER";
    //     }

    //     return "INVALID";
    // }

    // public String keyWord() {
    //     // only if tokenType = "KEYWORD"
    //     return this.token;
    // }

    // public char symbol() {
    //     // only if tokenType = "SYMBOL"
    //     return this.token.charAt(0);
    // }

    // public String identifier() {
    //     // only if tokenType = "IDENTIFIER"
    //     return this.token;
    // }

    // public int intVal() {
    //     // only if tokenType = "INT_CONST"
    //     return Integer.parseInt(token);
    // }

    // public String stringVal() {
    //     // only if tokenType = "STRING_CONST"
    //     return this.token;
    // }
}