package com.xiaohei.java.lib.util;

import java.math.BigDecimal;

public class Arith {
    private static final int DEF_SALE = 10;

    public static double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2));
    }

    public static double add(String v1, String v2) {
        return new BigDecimal(v1).add(new BigDecimal(v2)).doubleValue();
    }

    public static double subtract(double v1, double v2) {
        return subtract(Double.toString(v1), Double.toString(v2));
    }

    public static double subtract(String v1, String v2) {
        return new BigDecimal(v1).subtract(new BigDecimal(v2)).doubleValue();
    }

    public static double multiply(double v1, double v2) {
        return multiply(Double.toString(v1), Double.toString(v2));
    }

    public static double multiply(String v1, String v2) {
        return new BigDecimal(v1).multiply(new BigDecimal(v2)).doubleValue();
    }

    public static double divide(double v1, double v2) {
        return divide(Double.toString(v1), Double.toString(v2));
    }

    public static double divide(double v1, double v2, int scale) {
        return divide(Double.toString(v1), Double.toString(v2), scale);
    }

    public static double divide(String v1, String v2) {
        return divide(v1, v2, DEF_SALE);
    }

    public static double divide(String v1, String v2, int scale) {
        if (scale < 0)
            scale = DEF_SALE;
        return new BigDecimal(v1).divide(new BigDecimal(v2), scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double v) {
        return divide(v, 1);
    }

    public static double round(double v, int scale) {
        return divide(v, 1, scale);
    }

    public static double round(String v) {
        return divide(v, "1");
    }

    public static double round(String v, int scale) {
        return divide(v, "1", scale);
    }
}
