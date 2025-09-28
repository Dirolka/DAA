package daa.sort;

import daa.metrics.Metrics;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    @Test
    void sortsSmallFixedArray() {
        int[] a = {5, 1, 4, 2, 8, 0, -3, 7};
        int[] expected = Arrays.copyOf(a, a.length);
        Arrays.sort(expected);
        Metrics m = new Metrics();
        MergeSort.sort(a, m);
        assertArrayEquals(expected, a);
        assertTrue(m.getComparisons() > 0, "comparisons should be counted");
    }

    @RepeatedTest(3)
    void sortsRandomArrays() {
        for (int n : new int[]{0, 1, 2, 5, 24, 25, 100, 10_000}) {
            int[] a = randomArray(n, 12345L + n);
            int[] expected = Arrays.copyOf(a, a.length);
            Arrays.sort(expected);
            Metrics m = new Metrics();
            MergeSort.sort(a, m);
            assertArrayEquals(expected, a, "array not sorted correctly for n=" + n);
            assertTrue(m.getMaxRecursionDepth() >= 0);
        }
    }

    private static int[] randomArray(int n, long seed) {
        int[] a = new int[n];
        Random rnd = new Random(seed);
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        return a;
    }
}
