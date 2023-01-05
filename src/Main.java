import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CustomExecutor customExecutor = new CustomExecutor();
        for (int j = 0; j < 10; j++) {
            var task = Task.createTask(() -> {
                int sum = 0;
                for (int i = 1; i <= 10; i++) {
                    sum += i;
                }
                return sum;
            }, TaskType.COMPUTATIONAL);
            var sumTask = customExecutor.submit(task);
            customExecutor.submit(task);
            final int sum;
            try {
                sum = sumTask.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            logger.info(() -> "Sum of 1 through 10 = " + sum);
        }

        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
// var is used to infer the declared type automatically
        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            reversed = reverseTask.get();
            totalPrice = priceTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
      /*  String[] asd = Ex2.createTextFiles(19900,2123123,1000);
        long startTime = System.currentTimeMillis();
        int x= Ex2.getNumOfLines(asd);
        long endTime   = System.currentTimeMillis();
        long totalTime =  endTime - startTime;
        pint(x,totalTime);

        startTime = System.currentTimeMillis();
        int y= Ex2.getNumOfLinesThreads(asd);
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        pint(y,totalTime);

        startTime = System.currentTimeMillis();
        int z= Ex2.getNumOfLinesThreadsPool(asd);
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        pint(z,totalTime);*/
    }

    public static void pint(int linesNum, long time) {
        System.out.println("----------------------------");
        System.out.println("Number of lines :" + linesNum +
                "\nTime in Millis:" + time +
                "\nTime in Seconds:" + time / 1000.0);
        System.out.println("----------------------------\n");
    }
}