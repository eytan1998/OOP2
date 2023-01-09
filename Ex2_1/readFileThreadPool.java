package Ex2_1;

import java.util.concurrent.Callable;

/**
 * Custom Callable that given file name, return number of rows after called
 */
class readFileThreadPool implements Callable<Integer> {
    private final String fileName;

    /**
     *
     * @param fileName  name of file include path
     */
    public readFileThreadPool(String fileName) {
        this.fileName = fileName;
    }


    /**
     * override call of Callable, and sum the lines in the file and return the answer.
     */
    @Override
    public Integer call() throws Exception {
        return Ex2.sumOfLinesInFile(fileName);

    }


}
