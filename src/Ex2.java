import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.*;

public class Ex2 {

    public static int sumOfLinesInFile(String fileName) {
        int sum = 0;
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                myReader.nextLine();
                sum++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * crate n txt files
     *
     * @param n     number of text files
     * @param seed  parameters for creating random numbers of rows
     * @param bound
     * @return array of the files names
     */
    public static String[] createTextFiles(int n, int seed, int bound) {
        Random rnd = new Random(seed);
        String[] names = new String[n];
        for (int i = 1; i <= n; i++) {
            int x = rnd.nextInt(bound);
            names[i - 1] = "files/file_" + i + ".txt";
            try {
                FileWriter myWriter = new FileWriter(names[i - 1]);
                for (int j = 0; j < x; j++) {
                    myWriter.write("Hello!@#$\n");
                }
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
        return names;

    }

    /**
     * @param fileNames array with names of files
     * @return sum of files rows
     */
    public static int getNumOfLines(String[] fileNames) {
        int sum = 0;
        for (String fileName : fileNames) {
            sum += sumOfLinesInFile(fileName);
        }
        return sum;
    }

    /**
     * @param fileNames array with names of files
     * @return sum of files rows
     */
    public static int getNumOfLinesThreads(String[] fileNames) {
        int sum = 0;
        Stack<readFileThread> mThreads = new Stack<>();

        for (int i = 0; i < fileNames.length; i++) {
            mThreads.push(new readFileThread(fileNames[i]));
            mThreads.peek().start();
        }
        for (readFileThread thread : mThreads) {
            try {
                thread.join();
                sum += thread.getLineNumber();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return sum;
    }

    /**
     * @param fileNames array with names of files
     * @return sum of files rows
     */
    public static int getNumOfLinesThreadsPool(String[] fileNames) {
        int sum = 0;
        int numOfCores = Runtime.getRuntime().availableProcessors();
        int corePoolSize = numOfCores / 2;
        int maxPoolSize = numOfCores - 1;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                500, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(fileNames.length));

        ArrayList<Future<Integer>> futures = new ArrayList<>();
        for (String fileName : fileNames) {
            futures.add(threadPool.submit(new readFileThreadPool(fileName)));
        }
        Integer answer = null;
        for (Future<Integer> future : futures) {
            try {
                answer = future.get();
                sum += answer;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
        return sum;
    }

}
