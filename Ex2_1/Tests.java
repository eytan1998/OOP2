package Ex2_1;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Tests {
    @Test
    public void partialTest() {

        String[] asd = new String[]{"alsjdlkasd", "ajsdhsl"};
        int n=100;
        int seed=10;
        int bound=100;
        String [] files=Ex2.createTextFiles(n,seed,bound);
        System.out.println("number of files-"+n+"\nseed-"+seed+"\nbound-"+bound+"\n");
        long startTime = System.currentTimeMillis();
        int num=Ex2.getNumOfLines(files);
        long stopTime = System.currentTimeMillis();
        System.out.println("###########getNumOfLines###########");
        printResults(num,(stopTime - startTime));

        long startTimeT = System.currentTimeMillis();
        int numT=Ex2.getNumOfLinesThreads(files);
        long stopTimeT = System.currentTimeMillis();
        System.out.println("###########getNumOfLinesThreads###########");
        printResults(numT,(stopTimeT - startTimeT));

        long startTimeTP = System.currentTimeMillis();
        int numTP=Ex2.getNumOfLinesThreadsPool(files);
        long stopTimeTP = System.currentTimeMillis();
        System.out.println("###########getNumOfLinesThreadsPool###########");
        printResults(numTP,(stopTimeTP - startTimeTP));

        Assertions.assertThrows(RuntimeException.class,()->Ex2.getNumOfLinesThreadsPool(asd));
        Assertions.assertThrows(RuntimeException.class,()->Ex2.getNumOfLinesThreads(asd));
        Assertions.assertThrows(RuntimeException.class,()->Ex2.getNumOfLines(asd));


    }
    public static void printResults(int linesNum, long time) {
        System.out.println("Number of lines :" + linesNum +
                "\nTime in Millis:" + time +
                "\nTime in Seconds:" + time / 1000.0 +"\n");
    }
}
