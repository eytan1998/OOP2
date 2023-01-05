public class readFileThread extends Thread {
    private final String fileName;
    private int lineNumber= -1;

    public readFileThread(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public void run() {
        this.lineNumber = Ex2.sumOfLinesInFile(this.fileName);
    }

}
