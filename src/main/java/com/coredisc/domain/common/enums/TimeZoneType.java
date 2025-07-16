package com.coredisc.domain.common.enums;

public enum TimeZoneType {
    DAWN,       // 0~5시
    MORNING,    // 6~10시
    DAY,        // 11~13시
    AFTERNOON,  // 14~17시
    EVENING;    // 18~23시

    public boolean containsHour(int hour) {
        return switch (this) {
            case DAWN -> hour >= 0 && hour <= 5;
            case MORNING -> hour >= 6 && hour <= 10;
            case DAY -> hour >= 11 && hour <= 13;
            case AFTERNOON -> hour >= 14 && hour <= 17;
            case EVENING -> hour >= 18 && hour <= 23;
        };
    }

    public static TimeZoneType fromHour(int hour) {
        for (TimeZoneType type : values()) {
            if (type.containsHour(hour)) return type;
        }
        throw new IllegalArgumentException("Invalid hour: " + hour);
    }
}
