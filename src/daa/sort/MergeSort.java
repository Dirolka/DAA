package daa.sort;

import daa.metrics.Metrics;
import daa.util.InsertionSort;

public final class MergeSort {
    private MergeSort() {}

    public static final int DEFAULT_CUTOFF = 24;

    public static void sort(int[] a, Metrics m) {
        sort(a, DEFAULT_CUTOFF, m);
    }

    public static void sort(int[] a, int cutoff, Metrics m) {
        if (a == null || a.length <= 1) return;
        int[] buf = new int[a.length];
        if (m != null) m.incAllocations(a.length);
        mergesort(a, 0, a.length, buf, cutoff, m);
    }

    private static void mergesort(int[] a, int l, int r, int[] buf, int cutoff, Metrics m) {
        if (r - l <= cutoff) {
            InsertionSort.sort(a, l, r, m);
            return;
        }
        if (m != null) m.enterRecursion();
        int mid = l + ((r - l) >>> 1);
        mergesort(a, l, mid, buf, cutoff, m);
        mergesort(a, mid, r, buf, cutoff, m);
        merge(a, l, mid, r, buf, m);
        if (m != null) m.exitRecursion();
    }

    private static void merge(int[] a, int l, int m, int r, int[] buf, Metrics metrics) {
        int leftLen = m - l;
        System.arraycopy(a, l, buf, 0, leftLen);
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < leftLen && j < r) {
            if (metrics != null) metrics.incComparisons(1);
            if (buf[i] <= a[j]) {
                a[k++] = buf[i++];
            } else {
                a[k++] = a[j++];
            }
        }
        while (i < leftLen) {
            a[k++] = buf[i++];
        }
    }
}
