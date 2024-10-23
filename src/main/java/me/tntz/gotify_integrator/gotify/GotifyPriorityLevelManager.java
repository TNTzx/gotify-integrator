package me.tntz.gotify_integrator.gotify;

public class GotifyPriorityLevelManager {
    private static GotifyPriorityLevel minPriorityLevel = GotifyPriorityLevel.MIN;

    public static void setMinPriorityLevel(GotifyPriorityLevel minPriorityLevel) {
        GotifyPriorityLevelManager.minPriorityLevel = minPriorityLevel;
    }

    public static boolean isQualified(GotifyPriorityLevel priorityLevel) {
        return priorityLevel.getValue() >= minPriorityLevel.getValue();
    }
}
