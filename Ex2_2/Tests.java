package Ex2_2;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;


public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);
    //TODO javadoc ex2_1 2
    //TODO javadoc ex2_2 2
    //TODO README.md ex2_1 2
    //TODO organize classes 2
    //TODO test ex2_1 2
    //TODO test ex2_2 1
    //TODO uml ex2_2 2
    //TODO uml ex2_2 0


    //ways to submit task
    @Test
    public void testSubmit() {
        CustomExecutor customExecutor = new CustomExecutor();
        //can create task with or with out priority
        Task<Integer> task1 = Task.createTask((() -> {
            return 1;
        }), TaskType.OTHER);

        Task<Integer> task2 = Task.createTask((() -> {
            return 1;
        }));
        //and default is 3
        Assert.assertEquals(3, task2.getPriority());

        //can submit both to thread-poll
        customExecutor.submit(task1);
        customExecutor.submit(task2);

        //another way to submit
        Callable<Integer> callable1 = () -> {
            return 1;
        };
        customExecutor.submit(callable1, TaskType.OTHER);
        //and default is 3
        customExecutor.submit(callable1);

    }

    //test the queue and thread-poll
    @Test
    public void threadPoll() throws InterruptedException {
        CustomExecutor customExecutor = new CustomExecutor();
        //after initiation every thing is zero and queue is empty
        logger.info(customExecutor::toString);
        for (int i = 0; i < 4; i++) {
            customExecutor.submit(Task.createTask((() -> {
                sleep(10);
                return 1;
            }), TaskType.COMPUTATIONAL));
        }
        //after submitting 4 task he preferred add to core Threads (=4) then queueing
        logger.info(customExecutor::toString);


        for (int i = 0; i < 100; i++) {
            customExecutor.submit(Task.createTask((() -> {
                sleep(10);
                return 1;
            }), TaskType.OTHER));
        }
        //even after submit a lot task still the core is four because only if can't add to queue he will add thread
        logger.info(customExecutor::toString);

        for (int i = 0; i < 100; i++) {
            customExecutor.submit(Task.createTask((() -> {
                sleep(10);
                return 1;
            }), TaskType.COMPUTATIONAL));
        }
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //although the COMPUTATIONAL tasks come after they will go to head of queue
        logger.info(customExecutor::toString);

        sleep(3000);
        //we can see after we let all task to complete the queue is empty again
        logger.info(customExecutor::toString);
        sleep(3000);


    }

    //test shutdown properties
    @Test
    public void shutdownn() throws InterruptedException {
        //execute all task in queue
        //finish all task in threads
        CustomExecutor customExecutor = new CustomExecutor();
        Task<Integer> task1 = Task.createTask((() -> {
            sleep(1000);
            return 1;
        }), TaskType.OTHER);
        customExecutor.submit(task1);
        customExecutor.gracefullyTerminate();
        //cant submit after terminated
        Assertions.assertThrows(RuntimeException.class, () -> {
            customExecutor.submit(task1);
        });
        logger.info(customExecutor::toString);
        sleep(2000);
        logger.info(customExecutor::toString);

        CustomExecutor customExecutor1 = new CustomExecutor();
        Task<Integer> task2 = Task.createTask((() -> {
            sleep(1000);
            return 1;
        }), TaskType.OTHER);
        for (int i = 0; i < 10; i++) {
            customExecutor1.submit(task2);
        }
        customExecutor1.gracefullyTerminate();
        logger.info(customExecutor1::toString);
        sleep(3000);
        logger.info(customExecutor1::toString);


    }

    @Test
    public void exceptionTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        Task<Integer> task = Task.createTask(() -> {
            int sum = 0;
            sleep(1000);
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);

        Future<Integer> sumTask = customExecutor.submit(task);

        //THROW timeout because didn't finish in given time 1 mils
        Assert.assertThrows(TimeoutException.class, () -> {
            sumTask.get(1, TimeUnit.MILLISECONDS);
        });

        Task<Integer> task2 = Task.createTask(() -> {
            return 1/0;
        }, TaskType.COMPUTATIONAL);

        Future<Integer> divzero = customExecutor.submit(task2);
        //THROW ExecutionException because can't to this task
        Assert.assertThrows(ExecutionException.class, () -> {
            divzero.get(1000, TimeUnit.MILLISECONDS);
        });



    }

    @Test
    public void partialTest() {
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
            final int sum;
            try {
                sum = sumTask.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            logger.info(() -> "Sum of 1 through 10 = " + sum);
        }

        System.out.println(customExecutor);
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }
}