package test.daa.select;

import daa.metrics.Metrics;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeterministicSelectTest {

    @Test
    void selectsOnSmallFixed() {
        int[] a = {7, 2, 9, 4, 1, 5};
        for (int k = 0; k < a.length; k++) {
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            int v = DeterministicSelect.select(Arrays.copyOf(a, a.length), k, m);
            assertEquals(b[k], v, "k=" + k);
        }
    }

    @RepeatedTest(3)
    void comparesWithSortAcrossRandomTrials() {
        Random rnd = new Random(42);
        for (int trial = 0; trial < 100; trial++) {
            int n = rnd.nextInt(200) + 1;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
            int k = rnd.nextInt(n);
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            int v = DeterministicSelect.select(a, k, m);
            assertEquals(b[k], v, "Mismatch at trial=" + trial + ", n=" + n + ", k=" + k);
        }
    }
}
