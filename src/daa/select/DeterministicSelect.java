package daa.select;

import daa.metrics.Metrics;

public final class DeterministicSelect {
    private DeterministicSelect() {}

    public static int select(int[] a, int k, Metrics m) {
        if (a == null) throw new IllegalArgumentException("array is null");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return select(a, 0, a.length - 1, k, m);
    }

    private static int select(int[] a, int lo, int hi, int k, Metrics m) {
        while (true) {
            if (lo == hi) return a[lo];
            int pivotIndex = medianOfMedians(a, lo, hi, m);
            int p = partition(a, lo, hi, pivotIndex, m);
            if (k == p) {
                return a[p];
            } else if (k < p) {
                // Prefer recursing into the smaller side
                if (m != null) m.enterRecursion();
                hi = p - 1;
                if (m != null) m.exitRecursion();
            } else {
                if (m != null) m.enterRecursion();
                lo = p + 1;
                if (m != null) m.exitRecursion();
            }
        }
    }

    // Partition around a[pivotIndex] using Lomuto partition scheme
    private static int partition(int[] a, int lo, int hi, int pivotIndex, Metrics m) {
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, hi, m);
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (m != null) m.incComparisons(1);
            if (a[j] < pivot) {
                swap(a, i, j, m);
                i++;
            }
        }
        swap(a, i, hi, m);
        return i;
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        if (m != null) m.incSwap();
    }

    // Median of medians of groups of 5, returns index of pivot in a
    private static int medianOfMedians(int[] a, int lo, int hi, Metrics m) {
        int n = hi - lo + 1;
        if (n <= 5) {
            insertionSortRange(a, lo, hi, m);
            return lo + n / 2;
        }
        // Move medians of each 5-group to the front of array segment
        int numMedians = 0;
        for (int i = lo; i <= hi; i += 5) {
            int subHi = Math.min(i + 4, hi);
            insertionSortRange(a, i, subHi, m);
            int median = i + (subHi - i) / 2;
            swap(a, lo + numMedians, median, m);
            numMedians++;
        }
        // Recursively compute median of the medians
        int medianOfMedsIndex = selectIndex(a, lo, lo + numMedians - 1, lo + numMedians / 2, m);
        return medianOfMedsIndex;
    }

    // Helper that returns index of k-th element in [lo..hi] using same algorithm but returns index
    private static int selectIndex(int[] a, int lo, int hi, int kIndex, Metrics m) {
        while (true) {
            if (lo == hi) return lo;
            int pivotIndex = medianOfMedians(a, lo, hi, m);
            int p = partition(a, lo, hi, pivotIndex, m);
            if (kIndex == p) return p;
            else if (kIndex < p) hi = p - 1;
            else lo = p + 1;
        }
    }

    private static void insertionSortRange(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= lo) {
                if (m != null) m.incComparisons(1);
                if (a[j] <= key) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
}
