package com.example.installguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView tvStatus;
    private EditText etSetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        etSetPassword = findViewById(R.id.etSetPassword);
        Button btnEnableService = findViewById(R.id.btnEnableService);
        Button btnSavePassword = findViewById(R.id.btnSavePassword);
        Button btnTestLock = findViewById(R.id.btnTestLock);

        etSetPassword.setText(PasswordManager.getPassword(this));

        btnEnableService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GuardUtils.isAccessibilityEnabled(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "守护服务已开启", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
            }
        });

        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = etSetPassword.getText().toString();
                if (pwd.length() < 4) {
                    Toast.makeText(MainActivity.this, "密码至少4位", Toast.LENGTH_SHORT).show();
                    return;
                }
                PasswordManager.setPassword(MainActivity.this, pwd);
                Toast.makeText(MainActivity.this, "密码已保存", Toast.LENGTH_SHORT).show();
            }
        });

        btnTestLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardState.lock();
                Toast.makeText(MainActivity.this, "已锁定，下次安装需输入密码", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        boolean enabled = GuardUtils.isAccessibilityEnabled(this);
        if (enabled) {
            tvStatus.setText("● 守护状态：已开启（安装受保护）");
            tvStatus.setTextColor(0xFF4CAF50);
        } else {
            tvStatus.setText("● 守护状态：未开启（点击下方按钮开启）");
            tvStatus.setTextColor(0xFFF44336);
        }
    }
}
