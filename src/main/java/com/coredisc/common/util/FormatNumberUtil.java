package com.coredisc.common.util;

public class FormatNumberUtil {

    private FormatNumberUtil() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static String formatNumberUnit(long number) {
        double value = number;
        String suffix = "";

        if(number >= 1_000_000_000) {
            value = value / 1_000_000_000;
            suffix = "b";
        } else if (number >= 1_000_000) {
            value = value / 1_000_000;
            suffix = "m";
        } else if(number >= 1_000) {
            value = value / 1_000;
            suffix = "k";
        } else {
            return String.valueOf(number); // 1000미만은 그대로 반환
        }

        return String.format("%.1f", value) + suffix;
    }
}
