package com.example.installguard;

public class GuardState {
    private static boolean unlocked = false;
    private static long unlockTime = 0;
    private static final long UNLOCK_DURATION = 5 * 60 * 1000;

    public static boolean isUnlocked() {
        if (unlocked && (System.currentTimeMillis() - unlockTime) < UNLOCK_DURATION) {
            return true;
        }
        unlocked = false;
        return false;
    }

    public static void unlock() {
        unlocked = true;
        unlockTime = System.currentTimeMillis();
    }

    public static void lock() {
        unlocked = false;
    }
}
