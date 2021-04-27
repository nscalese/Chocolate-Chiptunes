package chocolate_chiptunes;

import java.util.HashMap;

import static java.lang.Math.*;

public class Utils {

    public static final HashMap<Character, Double> KEY_FREQUENCIES = new HashMap<Character, Double>();

    static {
        final char[] KEYS = "q2w3er5t6y7uzsxdcvgbhnjm".toCharArray();
        final int STARTING_KEY = 40;
        for(int i = STARTING_KEY, key = 0; key < KEYS.length; i++, key++) {
            KEY_FREQUENCIES.put(KEYS[key], Utils.Math.getKeyFrequency(i));
            System.out.println(KEY_FREQUENCIES.get(KEYS[key]));
        }

    }

    public static class Math
    {
        public static double getKeyFrequency(int keyNum)
        {
            return pow(root(2, 12), keyNum - 49) * 440;
        }

        public static int getKey(double frequency){
            return (int) round(log(frequency/440) / log (root(2, 12))) + 49;
        }

        public static double root(double num, double root) { return pow(E, log(num) / root); }
    }
}
