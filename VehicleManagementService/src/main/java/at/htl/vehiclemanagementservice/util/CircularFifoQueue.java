package at.htl.vehiclemanagementservice.util;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.internal.processors.datastructures.GridAtomicCacheQueueImpl;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class CircularFifoQueue<T>  {

    private int size;
    private IgniteQueue<T> queue;

    private CircularFifoQueue(int size, String name, Ignite ignite) {
        this.size = size;
        this.queue = ignite.queue(name, size, new CollectionConfiguration());
    }

    public static <V> CircularFifoQueue<V> create(int size, String name, Ignite ignite) {
        return new CircularFifoQueue<>(size, name, ignite);
    }

    public void add(T entity) {
        if (queue.size() > size) {
            queue.poll();
        }

        queue.add(entity);
    }

    public T peek() {
        return queue.peek();
    }

    public T poll() {
        return queue.poll();
    }

    public Iterator<T> iterator() {
        return queue.iterator();
    }
}
