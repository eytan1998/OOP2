package Ex2_1;

/**
 * Custom Thead that given file name, store the number of rows
 * after run the Thread.
 */
class readFileThread extends Thread {
    private final String fileName;
    private int lineNumber= -1;

    /**
     *
     * @param fileName name of file include path
     */
    public readFileThread(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return the answer of number of lines, if Thread didn't start\finish page number will be -1
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * override run of Thread, and sum the lines in the file
     */
    @Override
    public void run() {
        this.lineNumber = Ex2.sumOfLinesInFile(this.fileName);
    }

}
