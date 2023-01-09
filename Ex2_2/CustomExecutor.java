package Ex2_2;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Custom executor that can run and prioritize task in kind of {@link Task}.
 * also keep track of max priority task that currently in queue
 */
public class CustomExecutor {
    ThreadPoolExecutor mThreadPoolExecutor;
    Integer maxTaskType;

    public CustomExecutor() {
        this.maxTaskType = -1;
        int coreNum = Runtime.getRuntime().availableProcessors();
        mThreadPoolExecutor = new ThreadPoolExecutor(coreNum / 2,
                coreNum - 1,
                300, TimeUnit.MILLISECONDS,
                new CustomPriorityBlockingQueue<>(this));
    }

    /**
     * instade of using the simple submit, change it, so it get CustomFutureTask that comparable
     * @param task to submit to thead poll
     */
    public <T> Future<T> submit(Task<T> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = new CustomFutureTask<>(task);
        mThreadPoolExecutor.execute(ftask);
        return ftask;
    }

    /**
     * instade of using the simple submit, change it, so it get CustomFutureTask that comparable
     * @param task to submit to thead poll
     */
    public <T> Future<T> submit(Callable<T> task) {
        return submit(Task.createTask(task));
    }
    /**
     * instade of using the simple submit, change it, so it get CustomFutureTask that comparable
     * @param task to submit to thead poll
     */
    public <T> Future<T> submit(Callable<T> task, TaskType type) {
        return submit(Task.createTask(task, type));
    }

    /**
     * use thread poll shutdown method {@link ThreadPoolExecutor#shutdown()}
     */
    public void gracefullyTerminate() {
        mThreadPoolExecutor.shutdown();
    }

    /**
     *
     * @return the current max priority in queue, if get -1 the queue is empty
     */
    public int getCurrentMax() {
        return maxTaskType;
    }

    // get the queue update if null return -1
    protected void updateMax(Object peek) {
        if ((peek instanceof CustomFutureTask<?>))
            this.maxTaskType = ((CustomFutureTask<?>) peek).getPriority();
        else
            this.maxTaskType = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomExecutor that = (CustomExecutor) o;
        return Objects.equals(mThreadPoolExecutor, that.mThreadPoolExecutor) && Objects.equals(maxTaskType, that.maxTaskType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mThreadPoolExecutor, maxTaskType);
    }

    @Override
    public String toString() {

        return "CustomExecutor{" +
                mThreadPoolExecutor +
                ", Max type in queue= " + ((getCurrentMax() == -1) ? "Empty queue" : maxTaskType) +
                '}';
    }
}

