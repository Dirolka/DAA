package daa.sort;

import daa.metrics.Metrics;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    @Test
    void sortsSmallFixedArray() {
        int[] a = {3, 1, 2, 5, 4, 1, -1, 0};
        int[] expected = Arrays.copyOf(a, a.length);
        Arrays.sort(expected);
        Metrics m = new Metrics();
        QuickSort.sort(a, m);
        assertArrayEquals(expected, a);
        assertTrue(m.getComparisons() > 0);
    }

    @RepeatedTest(3)
    void sortsRandomArraysAndDepthIsLogLike() {
        for (int n : new int[]{0, 1, 2, 5, 50, 5_000}) {
            int[] a = randomArray(n, 98765L + n);
            int[] expected = Arrays.copyOf(a, a.length);
            Arrays.sort(expected);
            Metrics m = new Metrics();
            QuickSort.sort(a, m);
            assertArrayEquals(expected, a, "array not sorted for n=" + n);
            // Typical depth O(log n). Allow a loose upper bound for randomness.
            if (n > 1) {
                long maxDepth = m.getMaxRecursionDepth();
                double log2n = Math.log(Math.max(2, n)) / Math.log(2);
                assertTrue(maxDepth <= 4 * log2n + 10, "depth too large: " + maxDepth + ", n=" + n);
            }
        }
    }

    private static int[] randomArray(int n, long seed) {
        int[] a = new int[n];
        Random rnd = new Random(seed);
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        return a;
    }
}
