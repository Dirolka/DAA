package daa.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class Metrics {
    private final AtomicLong comparisons = new AtomicLong();
    private final AtomicLong swaps = new AtomicLong();
    private final AtomicLong allocations = new AtomicLong();
    private final AtomicLong recursionDepth = new AtomicLong();
    private final AtomicLong maxRecursionDepth = new AtomicLong();

    private long startTimeNs;
    private long elapsedNs;

    public void startTimer() {
        startTimeNs = System.nanoTime();
    }

    public void stopTimer() {
        elapsedNs = System.nanoTime() - startTimeNs;
    }

    public long getElapsedNs() { return elapsedNs; }

    public void incComparisons(long c) { comparisons.addAndGet(c); }
    public void incSwap() { swaps.incrementAndGet(); }
    public void incAllocations(long a) { allocations.addAndGet(a); }

    public long getComparisons() { return comparisons.get(); }
    public long getSwaps() { return swaps.get(); }
    public long getAllocations() { return allocations.get(); }
    public long getMaxRecursionDepth() { return maxRecursionDepth.get(); }

    public void enterRecursion() {
        long d = recursionDepth.incrementAndGet();
        // update max depth
        maxRecursionDepth.accumulateAndGet(d, Math::max);
    }

    public void exitRecursion() {
        recursionDepth.decrementAndGet();
    }
}
