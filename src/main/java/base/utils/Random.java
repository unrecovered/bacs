package base.utils;

/**
 * Created by valera on 10.03.17.
 */
public class Random {

    static String[] colors = {
//            "FF0000", "00FF00", "0000FF",
            "440000", "004400", "000044", "444400", "004444", "444444",
            "880000", "008800", "000088", "888800", "008888", "888888",
            "CC0000", "00CC00", "0000CC", "CCCC00", "00CCCC", "CCCCCC",
            "FE0000", "00FE00", "0000FE", "FEFE00", "00FEFE", "FEFEFE",
    };

    public static int getRandom(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }

    public static String getRandomColorCode() {
        return Integer.toHexString(getRandom(1, 16777214));
//        return colors[getRandom(0, colors.length -1 )];
    }

    public static int getRandomPercent() {
        return getRandom(0, 100);
    }
}
