package net.yukulab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static double round(int scale, double value) {
        return processDecimalByMode(scale, RoundingMode.HALF_UP, value);
    }
    public static double floor(int scale, double value) {
        return processDecimalByMode(scale, RoundingMode.FLOOR, value);
    }
    public static double ceil(int scale, double value) {
        return processDecimalByMode(scale, RoundingMode.CEILING, value);
    }
    public static double processDecimalByMode(int scale, RoundingMode roundMode, double value) {
        return BigDecimal.valueOf(value).setScale(scale, roundMode).doubleValue();
    }
}
