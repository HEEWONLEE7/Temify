package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        // ✅ GlobalData에서 자리 정보 표시
        textSeatInfo.setText("🔔 " + GlobalData.seatNumber + " 대여 인증");

        btnAuth.setOnClickListener(v -> {
            String input = editPin.getText().toString().trim();

            if (input.length() != 4) {
                Toast.makeText(this, "4자리 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (input.equals(GlobalData.password)) {
                Toast.makeText(this, "인증 성공!", Toast.LENGTH_SHORT).show();

                // ✅ 현재 시간 기준으로 start/end 계산
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String now = sdf.format(cal.getTime()); // 시작 시간
                cal.add(Calendar.HOUR_OF_DAY, 2);
                String end = sdf.format(cal.getTime()); // 종료 시간

                // ✅ Firebase에 현재 시간 업로드
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");
                ref.child("open").setValue(true);
                ref.child("start_time").setValue(now);
                ref.child("end_time").setValue(end);

                // ✅ 다음 화면으로 이동
                startActivity(new Intent(this, RentalCompleteActivity.class));
            } else {
                Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
