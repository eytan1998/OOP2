import java.util.concurrent.Callable;

public class readFileThreadPool implements Callable<Integer> {
    private final String fileName;

    public readFileThreadPool(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Integer call() throws Exception {
        return Ex2.sumOfLinesInFile(fileName);

    }


}
