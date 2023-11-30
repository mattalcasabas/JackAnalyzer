public class LineNumber {
    public Integer lineNum;

    public Integer getLineNumber() {
        return lineNum;
    }
    
    public void setLineNumber(int n) {
        lineNum = n;
    }
    
    public void nextLine() {
        this.lineNum++;
    }
}