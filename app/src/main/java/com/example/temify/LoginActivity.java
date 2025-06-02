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
            String id = editId.getText().toString().trim();
            String pw = editPw.getText().toString().trim();

            // 예시 계정: admin / 1234
            if (id.equals("admin") && pw.equals("1234")) {
                Toast.makeText(this, "✅ 로그인 성공!", Toast.LENGTH_SHORT).show();

                // ✅ GlobalData에 사용자 ID 저장
                GlobalData.userId = id;

                // 로그인 후 메인 화면 또는 관리자 화면 등으로 이동 가능
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

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
