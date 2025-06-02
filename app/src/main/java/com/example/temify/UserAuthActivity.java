package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserAuthActivity extends AppCompatActivity {

    EditText editPin;
    Button btnAuth;
    TextView textSeatInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);

        editPin = findViewById(R.id.editPin);
        btnAuth = findViewById(R.id.btnAuth);
        textSeatInfo = findViewById(R.id.textSeatInfo);

        // ✅ GlobalData에서 동적 정보 사용
        textSeatInfo.setText("🔔 " + GlobalData.seatNumber + " 대여 인증");

        btnAuth.setOnClickListener(v -> {
            String input = editPin.getText().toString().trim();

            if (input.length() != 4) {
                Toast.makeText(this, "4자리 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Firebase에서 받아온 동적 비밀번호와 비교
            if (input.equals(GlobalData.password)) {
                Toast.makeText(this, "인증 성공!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, RentalCompleteActivity.class));
            } else {
                Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
