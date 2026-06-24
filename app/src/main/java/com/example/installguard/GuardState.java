package com.example.installguard;

public class GuardState {
    private static boolean unlocked = false;
    private static long unlockTime = 0;
    private static final long UNLOCK_DURATION = 5 * 60 * 1000;

    private static long cooldownTime = 0;
    private static final long COOLDOWN_DURATION = 3000;

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
        cooldownTime = 0;
    }

    public static void lock() {
        unlocked = false;
    }

    public static void setCooldown() {
        cooldownTime = System.currentTimeMillis();
    }

    public static boolean isInCooldown() {
        return (System.currentTimeMillis() - cooldownTime) < COOLDOWN_DURATION;
    }
}
