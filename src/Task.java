import java.util.Objects;
import java.util.concurrent.Callable;

public class Task<T> implements Comparable<Task<T>>, Callable<T> {
    Callable<T> task;
    TaskType type;


    public Task(Callable<T> task, TaskType type) {
        this.type = type;
        this.task = task;
    }

    public static <T> Task<T> createTask(Callable<T> callable, TaskType type) {
        return new Task<T>(callable, type);
    }

    public static <T> Task<T> createTask(Callable<T> callable) {
        return new Task<T>(callable, TaskType.OTHER);
    }

    public int getPriority() {
        return type.getPriorityValue();
    }
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
    public int compareTo(Task<T> o) {
        return Integer.compare(this.type.getPriorityValue(), o.type.getPriorityValue());
    }


}
