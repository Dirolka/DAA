package daa.util;

import java.util.Random;

public final class ArrayUtils {
    private ArrayUtils() {}

    public static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void shuffle(int[] a, long seed) {
        Random rnd = new Random(seed);
        for (int i = a.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            swap(a, i, j);
        }
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i-1] > a[i]) return false;
        }
        return true;
    }

    public static int[] copyOf(int[] a) {
        int[] b = new int[a.length];
        System.arraycopy(a, 0, b, 0, a.length);
        return b;
    }
}
