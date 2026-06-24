package com.example.installguard;

import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.provider.Settings;

public class GuardUtils {
    public static boolean isAccessibilityEnabled(Context context) {
        ComponentName expected = new ComponentName(context, InstallGuardService.class.getName());
        String enabledServices = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (TextUtils.isEmpty(enabledServices)) return false;

        String expectedFlat = expected.flattenToString();
        for (String svc : enabledServices.split(":")) {
            if (svc.equalsIgnoreCase(expectedFlat)) return true;
        }
        return false;
    }
}
