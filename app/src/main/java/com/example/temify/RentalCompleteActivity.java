package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;

public class RentalCompleteActivity extends AppCompatActivity {

    TextView textUserInfo, textStartTime, textEndTime;
    Button btnBackToMain;
    private Robot robot;
    private String returnStation = "홈베이스";

    private final DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
    private final DatabaseReference callRequestsRef = FirebaseDatabase.getInstance().getReference("callRequests");
    private final DatabaseReference flagsRef = FirebaseDatabase.getInstance().getReference("Flags");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_complete);

        robot = Robot.getInstance();

        textUserInfo = findViewById(R.id.textBatteryInfo);
        textStartTime = findViewById(R.id.textUsageTime);
        textEndTime = findViewById(R.id.textReturnTime);
        btnBackToMain = findViewById(R.id.btnGoHome);

        reservationRef.child("open").setValue(true);
        reservationRef.child("rentalStatus").setValue(1);

        callRequestsRef.child("seat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String seat = snapshot.getValue(String.class);
                if (seat != null && !seat.isEmpty()) {
                    returnStation = seat;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RentalCompleteActivity.this, "Temi 반환 위치 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        reservationRef.child("battery").get().addOnSuccessListener(snapshot -> {
            String batteryNumber = snapshot.exists() ? snapshot.getValue(String.class) : "3";
            textUserInfo.setText("🔋 " + batteryNumber + "번 보조배터리를 가져가세요!");
        }).addOnFailureListener(e -> {
            textUserInfo.setText("🔋 보조배터리 정보를 불러올 수 없습니다.");
        });

        callRequestsRef.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String start = snapshot.getValue(String.class);
                if (start == null) start = "??:??";
                textStartTime.setText("🕒 사용 시작 시간: " + start);
                reservationRef.child("start_time").setValue(start);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RentalCompleteActivity.this, "시작 시간을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        reservationRef.child("end_time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String end = snapshot.getValue(String.class);
                if (end == null) end = "??:??";
                textEndTime.setText("📅 반납 예정 시간: " + end);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RentalCompleteActivity.this, "종료 시간을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Flags/Flag = "end" 감지되면 → 자동 이동 및 화면 전환
        flagsRef.child("Flag").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String flagValue = snapshot.getValue(String.class);
                if ("end".equals(flagValue)) {
                    reservationRef.child("open").setValue(false);
                    if (robot != null && returnStation != null && robot.getLocations().contains(returnStation)) {
                        robot.goTo(returnStation);
                        Toast.makeText(RentalCompleteActivity.this, "✅ Temi가 " + returnStation + "으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RentalCompleteActivity.this, "⚠️ Temi 반환 위치가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                    // ✅ 메인으로 전환
                    Intent intent = new Intent(RentalCompleteActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RentalCompleteActivity.this, "⚠️ Flag 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ 버튼 클릭 시
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(RentalCompleteActivity.this, MainActivity.class);
        });
    }
}
