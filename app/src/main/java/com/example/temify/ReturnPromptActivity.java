package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReturnPromptActivity extends AppCompatActivity {

    Button btnReturn, btnExtend;
    TextView textUserInfo, textUsageTime;

    private final DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
    private final DatabaseReference callRequestsRef = FirebaseDatabase.getInstance().getReference("callRequests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        reservationRef.child("open").setValue(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_prompt);

        btnReturn = findViewById(R.id.btnReturn);
        btnExtend = findViewById(R.id.btnExtend);
        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);

        fetchReservationInfo();

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReturnCompleteActivity.class);
            startActivity(intent);
        });

        btnExtend.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExtendRentalActivity.class);
            startActivity(intent);
        });
    }

    private void fetchReservationInfo() {
        // 🔄 두 경로를 병렬로 불러옴
        callRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot callSnapshot) {
                String seat = callSnapshot.child("number").getValue(String.class);
                String start = callSnapshot.child("time").getValue(String.class);

                reservationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot resSnapshot) {
                        String battery = resSnapshot.child("battery").getValue(String.class);
                        String end = resSnapshot.child("end_time").getValue(String.class);

                        // ✅ UI 업데이트
                        textUserInfo.setText("📌 " + seat + "번 자리 - " + battery + "번 보조배터리");
                        textUsageTime.setText("🕒 사용 시간: " + start + " ~ " + end);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ReturnPromptActivity.this, "예약 정보 로딩 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ReturnPromptActivity.this, "좌석 정보 로딩 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
