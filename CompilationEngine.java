import java.util.ArrayList;
import java.util.List;

public class CompilationEngine {
    private InputFile input;
    private OutputFile output;
    private List<Token> tokens = new ArrayList<>();
    private int tokenIndex = 0;
    private int nextTokenIndex = tokenIndex + 1;
    private Token currentToken = null;
    private Token nextToken = null;

    public CompilationEngine(InputFile input, OutputFile output, List<Token> tokens) {
        this.input = input;
        this.output = output;
        this.tokens = tokens;
        this.currentToken = tokens.get(this.tokenIndex);
        this.nextToken = tokens.get(this.nextTokenIndex);
    }

    public void advanceToken() {
        tokenIndex++;
    }

    public void compileClass() {
        output.writeOutputLine("<class>");
        if ("keyword".equals(nextToken.getTokenType())) {
            switch(nextToken.getTokenType()) {
                case "static":
                case "field":
                    compileClassVarDec();
                    break;
                case "constructor":
                case "function":
                case "method":
                    compileSubroutine();
                    break;
            }
        } else {
            System.out.println("Invalid class declaration at token \"" + nextToken.getToken() + "\", index " + nextTokenIndex);
            return;
        }
        advanceToken();
        output.writeOutputLine("</class>");
    }

    public void compileClassVarDec() {
        output.writeOutputLine("<classVarDec>");

    }

    public void compileSubroutine() {

    }

    public void compileParameterList() {

    }

    public void compileVarDec() {

    }

    public void compileStatements() {

    }

    public void compileDo() {

    }

    public void compileLet() {
        output.writeOutputLine("<letStatement>");
        while ()
    }

    public void compileWhile() {

    }

    public void compileReturn() {

    }

    public void compileIf() {

    }

    public void compileExpression() {

    }

    public void compileTerm() {

    }

    public void compileExpressionList() {

    }
}
