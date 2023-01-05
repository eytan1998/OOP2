import java.util.Comparator;
import java.util.concurrent.*;


public class CustomExecutor {
    ThreadPoolExecutor threadPoolExecutor;

    public CustomExecutor() {
        PriorityBlockingQueue<Runnable> pq = new PriorityBlockingQueue<Runnable>(20, new ComparePriority());
        int numOfCores = Runtime.getRuntime().availableProcessors();
        int minPoolSize = numOfCores / 2;
        int maxPoolSize = numOfCores - 1;
        this.threadPoolExecutor = new ThreadPoolExecutor(minPoolSize, maxPoolSize,
                300, TimeUnit.MILLISECONDS,pq);
    }

    public <T> Future<T> submit(Task<T> task) {
        return threadPoolExecutor.submit(task);
    }

    public <T> Future<T> submit(Callable<T> task, TaskType type) {
        return submit(Task.createTask(task,type));
    }

    public <T> Future<T> submit(Callable<T> task) {
        return submit(Task.createTask(task));
    }

    public void gracefullyTerminate() {
        threadPoolExecutor.shutdown();
    }

    public int getCurrentMax() {
        return TaskType.IO.getPriorityValue();
    }
}
class ComparePriority <T> implements Comparator<Task<T>> {
    @Override
    public int compare(Task<T> o1, Task<T> o2) {
        return Integer.compare(o1.getPriority(),o2.getPriority());
    }
}
