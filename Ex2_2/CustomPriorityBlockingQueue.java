package Ex2_2;

import java.util.Collection;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *  custom priority queue to update the max priority in the queue, every queue changing
 *  event this class override and add notify
 */
class CustomPriorityBlockingQueue<T> extends PriorityBlockingQueue<T> {
    /**
     * the Executor that old this priority queue and need n be notify about changes
     */
    private final CustomExecutor observer;

    CustomPriorityBlockingQueue(CustomExecutor observer) {
        super();
        this.observer = observer;
    }

    /**
     * use to notify the observer about changes in queue
     */
    private void notifyObserver() {
        observer.updateMax(this.peek());
    }
    // all the override methods that can change the queue
    @Override
    public boolean offer(T t) {
        boolean ans = super.offer(t);
        if (ans)
            notifyObserver();
        return ans;
    }

    @Override
    public boolean remove(Object o) {
        boolean ans = super.remove(o);
        if (ans)
            notifyObserver();
        return ans;
    }

    @Override
    public T poll() {
        notifyObserver();
        return super.poll();
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        notifyObserver();
        return super.poll(timeout, unit);
    }

    @Override
    public T take() throws InterruptedException {
        notifyObserver();
        return super.take();
    }

    @Override
    public boolean isEmpty() {
        notifyObserver();
        return super.isEmpty();
    }

    @Override
    public int drainTo(Collection<? super T> c) {
        notifyObserver();
        return super.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super T> c, int maxElements) {
        notifyObserver();
        return super.drainTo(c, maxElements);
    }
}