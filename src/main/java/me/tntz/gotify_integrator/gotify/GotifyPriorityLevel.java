package me.tntz.gotify_integrator.gotify;

public enum GotifyPriorityLevel {
    INF(999),
    HIGH(8),
    NORMAL(5),
    LOW(2),
    MIN(0);


    private final int value;
    GotifyPriorityLevel(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
