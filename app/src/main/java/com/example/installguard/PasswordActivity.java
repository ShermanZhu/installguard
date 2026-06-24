package com.example.installguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends Activity {

    private EditText etPassword;
    private TextView tvHint;
    private int errorCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        etPassword = findViewById(R.id.etPassword);
        tvHint = findViewById(R.id.tvHint);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnCancel = findViewById(R.id.btnCancel);

        final String savedPwd = PasswordManager.getPassword(this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etPassword.getText().toString();
                if (input.equals(savedPwd)) {
                    GuardState.unlock();
                    Toast.makeText(PasswordActivity.this, "验证通过，可继续安装", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    errorCount++;
                    tvHint.setText("密码错误，请重试（已错 " + errorCount + " 次）");
                    etPassword.setText("");
                    if (errorCount >= 3) {
                        Toast.makeText(PasswordActivity.this, "错误次数过多，已取消安装", Toast.LENGTH_LONG).show();
                        GuardState.lock();
                        finish();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardState.lock();
                finish();
            }
        });
    }
}
