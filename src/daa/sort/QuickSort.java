package daa.sort;

import daa.metrics.Metrics;

import java.util.concurrent.ThreadLocalRandom;

public final class QuickSort {
    private QuickSort() {}

    public static void sort(int[] a, Metrics m) {
        if (a == null || a.length <= 1) return;
        quicksort(a, 0, a.length - 1, m);
    }

    private static void quicksort(int[] a, int lo, int hi, Metrics m) {
        // Iterative tail-call elimination on larger side; recurse on smaller side only
        while (lo < hi) {
            if (m != null) m.enterRecursion();
            int p = randomizedPartition(a, lo, hi, m);
            int leftSize = p - lo;
            int rightSize = hi - p;
            // Recurse into smaller partition first to bound stack depth
            if (leftSize < rightSize) {
                if (lo < p - 1) quicksort(a, lo, p - 1, m);
                lo = p + 1; // tail on larger side
            } else {
                if (p + 1 < hi) quicksort(a, p + 1, hi, m);
                hi = p - 1; // tail on larger side
            }
            if (m != null) m.exitRecursion();
        }
    }

    private static int randomizedPartition(int[] a, int lo, int hi, Metrics m) {
        int pivotIndex = ThreadLocalRandom.current().nextInt(lo, hi + 1);
        swap(a, pivotIndex, hi, m); // move pivot to end
        int pivot = a[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (m != null) m.incComparisons(1);
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j, m);
            }
        }
        swap(a, i + 1, hi, m);
        return i + 1;
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        if (m != null) m.incSwap();
    }
}
