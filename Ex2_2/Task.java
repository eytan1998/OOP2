package Ex2_2;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * custom callable that as priority and can submitted to custom executor
 */
public class Task<T>  implements  Callable<T> {
    private Callable<T> task;
    private TaskType type;


    private Task(Callable<T> task, TaskType type) {
        this.type = type;
        this.task = task;
    }

    //factory methods
    public static <T> Task<T> createTask(Callable<T> callable, TaskType type) {
        return new Task<T>(callable, type);
    }

    public static <T> Task<T> createTask(Callable<T> callable) {
        return new Task<T>(callable, TaskType.OTHER);
    }


    //Getters
    public int getPriority() {
        return type.getPriorityValue();
    }

    public Callable<T> getTask() {
        return task;
    }

    public TaskType getType() {
        return type;
    }

    /**
     *
      * @return result
     * @throws Exception if unable to compute a result
     */
    @Override
    public T call() throws Exception {
        try {
            return task.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task<?> task1 = (Task<?>) o;
        return Objects.equals(task, task1.task) && type == task1.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, type);
    }

    @Override
    public String toString() {
        return "Task{" +
                "task=" + task +
                ", type=" + type +
                '}';
    }
}
