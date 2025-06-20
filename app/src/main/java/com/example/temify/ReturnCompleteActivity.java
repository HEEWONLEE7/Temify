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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReturnCompleteActivity extends AppCompatActivity {

    TextView textComplete, textUserInfo, textUsageTime, textReturnTime;
    Button btnBackToMain;
    private Robot robot;
    private final String fallbackStation = "홈베이스";
    private String returnStation = fallbackStation;

    private final DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
    private final DatabaseReference callRequestsRef = FirebaseDatabase.getInstance().getReference("callRequests");
    private final DatabaseReference flagsRef = FirebaseDatabase.getInstance().getReference("Flags");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_complete);

        // UI 요소 초기화
        textComplete = findViewById(R.id.textComplete);
        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);
        textReturnTime = findViewById(R.id.textReturnTime);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // Temi 초기화
        robot = Robot.getInstance();

        // ✅ 상태 초기화
        reservationRef.child("open").setValue(true);
        flagsRef.child("Flag").setValue("start");
        reservationRef.child("rentalStatus").setValue(0);

        // ✅ 현재 시간 → 반납 시간
        String returnTime = new SimpleDateFormat("HH:mm", Locale.KOREA).format(new Date());
        textReturnTime.setText("📅 반납 시간: " + returnTime);

        // ✅ start 값을 저장하기 위한 배열
        final String[] startHolder = new String[1];

        // ✅ 좌석 및 시작 시간 정보 받아오기
        callRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String seat = snapshot.child("seat").getValue(String.class);
                String start = snapshot.child("time").getValue(String.class);

                if (seat != null && !seat.isEmpty()) returnStation = seat;
                startHolder[0] = (start != null) ? start : "??:??";

                // ✅ 배터리 및 종료 시간 정보 받아오기
                reservationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot resSnapshot) {
                        String battery = resSnapshot.child("battery").getValue(String.class);
                        String end = resSnapshot.child("end_time").getValue(String.class);

                        if (battery == null) battery = "3";
                        if (end == null) end = "??:??";

                        // ✅ UI 업데이트
                        textComplete.setText("🎉 보조배터리가 반납되었습니다!");
                        textUserInfo.setText("🔋 " + battery + "번 보조배터리 반납 완료");
                        textUsageTime.setText("🕒 사용 시간: " + startHolder[0] + " ~ " + end);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReturnCompleteActivity.this, "예약 정보 로딩 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReturnCompleteActivity.this, "좌석 정보 로딩 실패", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Flag == "end" 감지 시 Temi 이동 및 화면 전환
        flagsRef.child("Flag").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String flag = snapshot.getValue(String.class);
                if ("end".equals(flag)) {
                    if (robot != null && robot.getLocations().contains(returnStation)) {
                        robot.goTo(returnStation);
                        Toast.makeText(ReturnCompleteActivity.this, "✅ Temi가 '" + returnStation + "'으로 이동합니다!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReturnCompleteActivity.this, "⚠️ Temi 위치가 유효하지 않습니다: " + returnStation, Toast.LENGTH_LONG).show();
                    }

                    // ✅ MainActivity 전환
                    Intent intent = new Intent(ReturnCompleteActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReturnCompleteActivity.this, "⚠️ Flag 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ 버튼 클릭 시 open = false 설정만 (화면 전환 없음)
        btnBackToMain.setOnClickListener(v -> {
            reservationRef.child("open").setValue(false);
            Toast.makeText(ReturnCompleteActivity.this, "🔒 Temi 사용 종료 처리 완료. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
        });
    }
}
