package Ex2_2;

import java.util.concurrent.FutureTask;

/**
 * custom Future task for the compare function so the priority queue can prioritize
 */
class CustomFutureTask<T> extends FutureTask<T> implements Comparable<CustomFutureTask<T>> {
    //for the methods that use the task
    private final Task<T> task;

    public CustomFutureTask(Task<T> task) {
        super(task);
        this.task = task;
    }

    public int getPriority() {
        return task.getPriority();
    }

    @Override
    public int compareTo(CustomFutureTask o) {
        return Integer.compare(this.task.getPriority(), o.task.getPriority());
    }
}