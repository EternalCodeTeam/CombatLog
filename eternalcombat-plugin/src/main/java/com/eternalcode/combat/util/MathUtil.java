package com.eternalcode.combat.util;

import java.util.Collection;
import java.util.function.ToIntFunction;

public class MathUtil {

    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static double getCountFromPercentage(int percentage, int totalCount) {
        return percentage / 100D * totalCount;
    }

    public static double getCountPercentage(double value, double base) {
        return value / base * 100;
    }

    public static int getRoundedCountPercentage(double value, double base) {
        return (int) Math.round(getCountPercentage(value, base));
    }

    public static int getRoundedCountFromPercentage(int percentage, int baseNumber) {
        return (int) Math.round(getCountFromPercentage(percentage, baseNumber));
    }

    public static <T> int sum(Collection<T> collection, ToIntFunction<? super T> mapper) {
        return collection.stream().mapToInt(mapper).sum();
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
