package com.example.installguard;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class InstallGuardService extends AccessibilityService {

    private static final String[] INSTALL_PACKAGES = {
        "com.android.packageinstaller",
        "com.miui.packageinstaller",
        "com.android.permissioncontroller",
        "com.google.android.packageinstaller"
    };

    private static final String[] APP_STORE_PACKAGES = {
        "com.miui.player",
        "com.xiaomi.market",
        "com.android.vending",
        "com.tencent.android.appstore",
        "com.wandoujia",
        "com.baidu.appsearch",
        "com.qihoo.appstore"
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return;

        String packageName = String.valueOf(event.getPackageName());
        if (packageName == null) return;

        for (String pkg : APP_STORE_PACKAGES) {
            if (packageName.equals(pkg)) {
                if (GuardState.isUnlocked()) return;
                performGlobalAction(GLOBAL_ACTION_BACK);
                GuardState.setCooldown();
                return;
            }
        }

        boolean isInstaller = false;
        for (String pkg : INSTALL_PACKAGES) {
            if (packageName.equals(pkg)) { isInstaller = true; break; }
        }
        if (!isInstaller) return;
        if (GuardState.isUnlocked()) return;
        if (GuardState.isInCooldown()) return;

        Intent intent = new Intent(this, PasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        findAndClickCancel(getRootInActiveWindow());
    }

    private void findAndClickCancel(AccessibilityNodeInfo node) {
        if (node == null) return;
        CharSequence text = node.getText();
        if (text != null) {
            String s = text.toString().toLowerCase();
            if (s.equals("取消") || s.equals("cancel") || s.equals("拒绝") || s.equals("denied")) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            findAndClickCancel(node.getChild(i));
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        info.notificationTimeout = 500;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {}
}
