package base.utils;

/**
 * Created by valera on 10.03.17.
 */
public class Random {

    public static int getRandom(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }
}
