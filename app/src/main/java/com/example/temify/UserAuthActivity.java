package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserAuthActivity extends AppCompatActivity {

    EditText editPin;
    Button btnAuth;
    TextView textSeatInfo;

    private String correctPassword = "";
    private String seatNumber = "";

    private DatabaseReference reservationRef;
    private DatabaseReference callRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);

        editPin = findViewById(R.id.editPin);
        btnAuth = findViewById(R.id.btnAuth);
        textSeatInfo = findViewById(R.id.textSeatInfo);

        reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
        callRequestsRef = FirebaseDatabase.getInstance().getReference("callRequests");

        // ✅ callRequests/number → 자리 정보 불러오기
        callRequestsRef.child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String seat = snapshot.getValue(String.class);
                if (seat != null && !seat.trim().isEmpty()) {
                    seatNumber = seat.trim();
                    textSeatInfo.setText("🔔 " + seatNumber + "번 자리 대여 인증");
                } else {
                    textSeatInfo.setText("🔔 대여 좌석 정보를 불러올 수 없습니다.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textSeatInfo.setText("🔔 좌석 정보 로드 실패");
                Toast.makeText(UserAuthActivity.this, "좌석 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ reservation/password → 인증용 비밀번호 불러오기
        reservationRef.child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                correctPassword = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserAuthActivity.this, "비밀번호를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ 인증 버튼 클릭
        btnAuth.setOnClickListener(v -> {
            String input = editPin.getText().toString().trim();

            if (input.length() != 4) {
                Toast.makeText(this, "4자리 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (input.equals(correctPassword)) {
                Toast.makeText(this, "인증 성공!", Toast.LENGTH_SHORT).show();

                // ✅ 현재 시간 기준 시작 및 종료 시간 계산
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String now = sdf.format(cal.getTime());
                cal.add(Calendar.HOUR_OF_DAY, 2);
                String end = sdf.format(cal.getTime());

                // ✅ 서버에 반영
                reservationRef.child("open").setValue(true);
                callRequestsRef.child("time").setValue(now);
                reservationRef.child("end_time").setValue(end);

                // ✅ rentalStatus 값 확인하여 분기 처리
                reservationRef.child("rentalStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer status = snapshot.getValue(Integer.class);
                        if (status != null) {
                            moveToNextActivity(status);
                        } else {
                            Toast.makeText(UserAuthActivity.this, "rentalStatus 값을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserAuthActivity.this, "rentalStatus 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ rentalStatus에 따라 다음 액티비티로 이동
    private void moveToNextActivity(int rentalStatus) {
        Intent intent = null;

        if (rentalStatus == 0) {
            intent = new Intent(UserAuthActivity.this, RentalCompleteActivity.class);
        } else if (rentalStatus == 1) {
            intent = new Intent(UserAuthActivity.this, ReturnPromptActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "❌ 알 수 없는 rentalStatus 값: " + rentalStatus, Toast.LENGTH_LONG).show();
        }
    }
}
