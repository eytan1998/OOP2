package Ex2_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.*;

/**
 * Class that hold Three way to sum lines in files.
 * <br>
 * 1- {@link #getNumOfLines(String[])} The simple way with loop and main Tread
 * <br>
 * 2-  {@link #getNumOfLinesThreads(String[])} Every file get Thread using {@link readFileThread} class
 * <br>
 * 3- {@link #getNumOfLinesThreads(String[])} Using ThreadPoll and submitting the callable
 * using {@link readFileThreadPool} class.
 * <br>
 * try to see the difference between the methods.
 */
class Ex2 {

    /**
     * crate n txt files, in folder "flies" - if doesn't exist create directory, make sure to delete the files after use.
     *
     * @param n     number of text files you want to make
     * @param seed  parameters for creating random numbers of rows
     * @param bound the bound of lines
     * @return array of the files names
     */
    public static String[] createTextFiles(int n, int seed, int bound)  {
        //check if directory is exists, if not create
        File directory = new File("files");
        if (!directory.exists()) {
            directory.mkdir();
        }
        //initialize the random
        Random rnd = new Random(seed);
        String[] names = new String[n];
        //creating all files and store them in name
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
                throw new RuntimeException(e);
            }

        }
        return names;

    }

    /**
     * Simple way with loop and main Tread
     *
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
     * Every file get Thread using {@link readFileThread} class
     *
     * @param fileNames array with names of files
     * @return sum of files rows
     * @throws InterruptedException if any thread has interrupted the current thread. The
     *                              <i>interrupted status</i> of the current thread is
     *                              cleared when this exception is thrown.
     */
    public static int getNumOfLinesThreads(String[] fileNames) {
        int sum = 0;
        Stack<readFileThread> mThreads = new Stack<>();

        //add all custom Theads to stack and start them
        for (String fileName : fileNames) {
            mThreads.push(new readFileThread(fileName));
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
     * Using ThreadPoll and submitting the callable
     * using {@link readFileThreadPool} class.
     *
     * @param fileNames array with names of files
     * @return sum of files rows
     * @throws CancellationException if the computation was cancelled
     * @throws  RuntimeException    if the computation threw an
     * exception and if the current thread was interrupted
     * while waiting
     */
    public static int getNumOfLinesThreadsPool(String[] fileNames) {
        int sum = 0;
        //make dynamic thread-poll
        int numOfCores = Runtime.getRuntime().availableProcessors();
        int corePoolSize = numOfCores / 2;
        int maxPoolSize = numOfCores - 1;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                500, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(fileNames.length));

        //get all futer to array
        ArrayList<Future<Integer>> futures = new ArrayList<>();
        for (String fileName : fileNames) {
            futures.add(threadPool.submit(new readFileThreadPool(fileName)));
        }
        Integer answer = null;
        //sums the results, and throw exception
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


    /**
     * sum lines of giver file path and name
     * @param fileName file name include path
     * @return the number of lines in this file
     * @throws RuntimeException FileNotFoundException if path not good
     */
    protected static int sumOfLinesInFile(String fileName) throws RuntimeException {
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
            throw new RuntimeException(e);
        }
        return sum;
    }
}
