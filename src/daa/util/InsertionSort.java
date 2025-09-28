package daa.util;

import daa.metrics.Metrics;

public final class InsertionSort {
    private InsertionSort() {}

    public static void sort(int[] a, int left, int right, Metrics m) {
        // sort range [left, right) using insertion sort
        for (int i = left + 1; i < right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left) {
                m.incComparisons(1);
                if (a[j] <= key) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
}
