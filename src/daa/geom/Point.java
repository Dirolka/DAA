package daa.geom;

public record Point(double x, double y) {
    public static double dist2(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return dx * dx + dy * dy;
    }
}
