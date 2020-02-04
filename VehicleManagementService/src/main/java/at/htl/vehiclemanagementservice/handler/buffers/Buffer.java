package at.htl.vehiclemanagementservice.handler.buffers;

import at.htl.vehiclemanagementservice.util.CircularFifoQueue;
import org.apache.ignite.Ignite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public abstract class Buffer<T> implements Iterable<T> {

    private CircularFifoQueue<T> queue;

    protected Buffer(int size, String name, Ignite ignite) {
        this.queue = CircularFifoQueue.create(size, name, ignite);
    }

    public void buffer(T t) {
        queue.add(t);
    }

    @Nullable
    public T observe() {
        return queue.peek();
    }

    @Nullable
    public T take() {
        return queue.poll();
    }

    @NotNull
    public Iterator<T> iterator() {
        return queue.iterator();
    }
}
