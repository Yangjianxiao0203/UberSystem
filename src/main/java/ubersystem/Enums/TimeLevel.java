package ubersystem.Enums;

public enum TimeLevel {
    SECOND(1),
    MINUTE(60),
    HOUR(3600),
    DAY(86400),
    WEEK(604800),
    MONTH(2592000),
    YEAR(31536000);

    private long value;

    TimeLevel(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
