import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private BufferedReader input;
    private BufferedWriter output;
    private String line = "";
    private String tokenType = "";
    private String token = "";
    private int tokenIndex = 0;
    private int lineNumber = 0;
    private Pattern keywordPattern = Pattern.compile("\\b(?:class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)\\b");
    private Pattern symbolPattern = Pattern.compile("[{}\\[\\].,;\\+\\-*/&|<>=~]");
    private Pattern integerPattern = Pattern.compile("\\b\\d+\\b");
    private Pattern stringPattern = Pattern.compile("\"[^\"]*\"");
    private Pattern identifierPattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private Matcher matcher = Pattern.compile(keywordPattern.pattern() + "|" +
                                         symbolPattern.pattern() + "|" +
                                         integerPattern.pattern() + "|" +
                                         stringPattern.pattern() + "|" +
                                         identifierPattern.pattern())
                                        .matcher(line);



    public Tokenizer(String inFile, String outFile) {
        try {
            FileReader fr = new FileReader(inFile);
            input = new BufferedReader(fr);
            FileWriter fw = new FileWriter(outFile);
            output = new BufferedWriter(fw);
        } catch(IOException e) {
            System.out.println("Could not open " + inFile);
            e.printStackTrace();
        }
    }

    public boolean hasMoreTokens() {
        try {
            return (line = input.readLine()) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getLine() {
        try {
            while((line = input.readLine()) != null) {
                lineNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Token> tokenizeLine() {
        List<Token> tokens = new ArrayList<>();
        while (matcher.find()) {
            token = matcher.group();
            if (keywordPattern.matcher(token).matches()) {
                tokenType = "KEYWORD";
            } else if (symbolPattern.matcher(token).matches()) {
                tokenType = "SYMBOL";
            } else if (integerPattern.matcher(token).matches()) {
                tokenType = "INT_CONST";
            } else if (stringPattern.matcher(token).matches()) {
                tokenType = "STRING_CONST";
            } else if (identifierPattern.matcher(token).matches()) {
                tokenType = "IDENTIFIER";
            } else {
                tokenType = "UNKNOWN";
            }
            Token tokenObject = new Token(token, tokenType);
            tokens.add(tokenObject);
        }
        return tokens;
    }

    public void advance() {
        
    }

    public boolean isSkippableLine() {
        if (line.length() == 0 || (line.charAt(0) == '/' && line.charAt(1) == '/')) {
            return true;
        }
        else {
            return false;
        }
        
    }

    public void writeToken() {

    }

    public String tokenType(String token) {
        // test if the token is a keyword or symbol
        switch(token) {
            case "class":
            case "constructor":
            case "function":
            case "method":
            case "field":
            case "static":
            case "var":
            case "int":
            case "char":
            case "boolean":
            case "void":
            case "true":
            case "false":
            case "null":
            case "this":
            case "let":
            case "do":
            case "if":
            case "else":
            case "while":
            case "return":
                return "KEYWORD";
            case "{":
            case "}":
            case "(":
            case ")":
            case "[":
            case "]":
            case ".":
            case ",":
            case ";":
            case "+":
            case "-":
            case "*":
            case "/":
            case "&":
            case "|":
            case "<":
            case ">":
            case "=":
            case "~":
                return "SYMBOL";
            default:
                break;
        }

        // test if the token is an integer constant
        try {
            int number = Integer.parseInt(token);
            if (number >= 0 && number <= 32767) {
                return "INT_CONST";
            }
        } catch (NumberFormatException e) {
            // String not a valid integer
        }

        // test if the token is a string constant
        if(token != null && token.length() >= 2) {
            char firstChar = token.charAt(0);
            char lastChar = token.charAt(token.length() - 1);
            if(firstChar == '"' && lastChar == '"') {
                return "STRING_CONST";
            }
        }

        // test if the token is an identifier:
        // regex will match any string that contains letters, numbers,
        // or underscores, provided it does not start with a digit
        if(token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            return "IDENTIFIER";
        }

        return "INVALID";
    }

    public String keyWord() {
        // only if tokenType = "KEYWORD"
        return this.token;
    }

    public char symbol() {
        // only if tokenType = "SYMBOL"
        return this.token.charAt(0);
    }

    public String identifier() {
        // only if tokenType = "IDENTIFIER"
        return this.token;
    }

    public int intVal() {
        // only if tokenType = "INT_CONST"
        return Integer.parseInt(token);
    }

    public String stringVal() {
        // only if tokenType = "STRING_CONST"
        return this.token;
    }
}