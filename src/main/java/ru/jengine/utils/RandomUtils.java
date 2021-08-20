package ru.jengine.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static <E> E chooseInCollection(Collection<E> collection) {
        if (collection.isEmpty()) {
            return null;
        }

        int chosenIndex = random.nextInt(collection.size());
        return new ArrayList<>(collection).get(chosenIndex);
    }

    public static boolean testInPercent(int trashHold) {
        return test(100, trashHold);
    }

    public static boolean test(int maxValue, int trashHold) {
        return random.nextInt(maxValue) < trashHold;
    }

    public static int tests(int maxValue, int... trashHolds) {
        if (trashHolds.length == 0) {
            return -1;
        }

        if (sum(trashHolds) > maxValue) {
            throw new RandomUtilsException("Trash holds (" + sum(trashHolds) + ") greater then max value (" + maxValue + ")");
        }

        for (int i = 0; i < trashHolds.length; i++) {
            if (test(maxValue, trashHolds[i])) {
                return i;
            }
        }

        return -1;
    }

    private static int sum(int... elements) {
        return Arrays.stream(elements).sum();
    }

    public static class RandomUtilsException extends RuntimeException {
        public RandomUtilsException(String message) {
            super(message);
        }
    }
}
