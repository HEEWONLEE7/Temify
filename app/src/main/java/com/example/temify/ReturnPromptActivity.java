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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_prompt);

        btnReturn = findViewById(R.id.btnReturn);
        btnExtend = findViewById(R.id.btnExtend);
        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);

        // ✅ Firebase에서 예약 정보 불러오기
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String seat = snapshot.child("seat").getValue(String.class);
                    String battery = snapshot.child("battery").getValue(String.class);
                    String start = snapshot.child("start_time").getValue(String.class);
                    String end = snapshot.child("end_time").getValue(String.class);

                    // GlobalData에도 저장 (선택)
                    GlobalData.seatNumber = seat;
                    GlobalData.batteryNumber = battery;
                    GlobalData.startTime = start;
                    GlobalData.endTime = end;

                    // ✅ UI 표시
                    textUserInfo.setText("📌 " + seat + "번 자리 - " + battery + "번 보조배터리");
                    textUsageTime.setText("🕒 사용 시간: " + start + " ~ " + end);
                } else {
                    Toast.makeText(ReturnPromptActivity.this, "예약 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ReturnPromptActivity.this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
