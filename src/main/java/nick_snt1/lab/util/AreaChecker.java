package nick_snt1.lab.util;

public class AreaChecker {

    private static boolean hitTriangle(Double x, Double y, Double r) {
        return x <= 0 && y <= 0 && Math.abs(x) + Math.abs(y) <= r / 2;
    }

    private static boolean hitSquare(Double x, Double y, Double r) {
        return x >= 0 && y <= 0 && x <= r/2 && Math.abs(y) <= r;
    }

    private static boolean hitCircle(Double x, Double y, Double r) {
        return x >= 0 && y >= 0 && Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= r;
    }

    public static boolean isHit(Double x, Double y, Double r) {
       return hitCircle(x, y, r) || hitSquare(x, y, r) || hitTriangle(x, y, r);
    }
}
