package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editId, editPw;
    Button btnLogin, btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editId = findViewById(R.id.editId);
        editPw = findViewById(R.id.editPw);
        btnLogin = findViewById(R.id.btnLogin);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        btnLogin.setOnClickListener(v -> {
            String id = editId.getText().toString();
            String pw = editPw.getText().toString();

            if (id.equals("admin") && pw.equals("1234")) {
                Toast.makeText(this, "✅ 로그인 성공!", Toast.LENGTH_SHORT).show();
                // 추가 동작 가능
            } else {
                Toast.makeText(this, "❌ 로그인 실패!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
