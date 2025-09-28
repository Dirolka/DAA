package daa.closest;

import daa.geom.Point;
import daa.metrics.Metrics;

import java.util.Arrays;
import java.util.Comparator;

public final class ClosestPair {
    private ClosestPair() {}

    public static double closestDistance2(Point[] points, Metrics m) {
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;
        Point[] px = Arrays.copyOf(points, points.length);
        Arrays.sort(px, Comparator.comparingDouble(Point::x));
        Point[] py = Arrays.copyOf(px, px.length);
        Arrays.sort(py, Comparator.comparingDouble(Point::y));
        Point[] aux = new Point[points.length];
        if (m != null) m.incAllocations(points.length * 3L);
        return divide(px, py, 0, px.length, aux, m);
    }

    private static double divide(Point[] px, Point[] py, int l, int r, Point[] aux, Metrics m) {
        int n = r - l;
        if (n <= 3) {
            // brute force
            double best = Double.POSITIVE_INFINITY;
            for (int i = l; i < r; i++) {
                for (int j = i + 1; j < r; j++) {
                    double d = Point.dist2(px[i], px[j]);
                    if (m != null) m.incComparisons(1);
                    if (d < best) best = d;
                }
            }
            // py for this segment must be sorted by y already by caller
            return best;
        }
        if (m != null) m.enterRecursion();
        int mid = l + (n >>> 1);
        double midX = px[mid].x();

        // Split py into pyl, pyr according to x <= midX
        int leftN = mid - l;
        int rightN = r - mid;
        Point[] pyl = new Point[leftN];
        Point[] pyr = new Point[rightN];
        if (m != null) m.incAllocations(leftN + rightN);
        int il = 0, ir = 0;
        for (int i = 0; i < py.length; i++) {
            Point p = py[i];
            if (p.x() < midX || (p.x() == midX && indexOf(px, l, mid, p) >= 0)) {
                // go left if strictly less x, or if equal and point belongs to left by index
                if (il < leftN) pyl[il++] = p;
            } else {
                if (ir < rightN) pyr[ir++] = p;
            }
        }
        // Recursively compute left and right
        double dl = divide(px, pyl, l, mid, aux, m);
        double dr = divide(px, pyr, mid, r, aux, m);
        double d = Math.min(dl, dr);

        // Build strip: points within sqrt(d) from midX, already sorted by y because pyl/pyr were
        int s = 0;
        for (Point p : py) {
            double dx = p.x() - midX;
            if (dx * dx < d) aux[s++] = p; // reuse aux as strip container
        }
        // Check each point with next up to 7 points in y-order
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s && (aux[j].y() - aux[i].y()) * (aux[j].y() - aux[i].y()) < d && j <= i + 8; j++) {
                double dist2 = Point.dist2(aux[i], aux[j]);
                if (m != null) m.incComparisons(1);
                if (dist2 < d) d = dist2;
            }
        }
        if (m != null) m.exitRecursion();
        return d;
    }

    // Helper to decide side when x equals midX; linear scan on tiny halves
    private static int indexOf(Point[] px, int l, int r, Point p) {
        for (int i = l; i < r; i++) if (px[i] == p) return i; // identity check since we reuse references
        return -1;
    }
}
