package base.utils;

/**
 * Created by valera on 10.03.17.
 */
public class Random {

    public static int getRandom(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }

    public static String getRandomColorCode() {
        return Integer.toHexString(getRandom(1, 16777214));
    }

    public static int getRandomPercent() {
        return getRandom(0, 100);
    }

    public static int getRandomDirection() {
        return getRandom(0, 7);
    }
}
