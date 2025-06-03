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

import com.robotemi.sdk.Robot;

public class RentalCompleteActivity extends AppCompatActivity {

    TextView textUserInfo, textStartTime, textEndTime;
    Button btnBackToMain;
    private Robot robot;
    private final String returnStation = "3번 자리";  // Temi에 저장된 위치명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_complete);

        // Temi SDK 초기화
        robot = Robot.getInstance();

        // XML 요소 연결
        textUserInfo = findViewById(R.id.textBatteryInfo);
        textStartTime = findViewById(R.id.textUsageTime);
        textEndTime = findViewById(R.id.textReturnTime);
        btnBackToMain = findViewById(R.id.btnGoHome);

        // ✅ Firebase에서 battery 값 가져오기
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");

        ref.child("battery").get().addOnSuccessListener(snapshot -> {
            String batteryNumber = snapshot.exists() ? snapshot.getValue(String.class) : "3";
            textUserInfo.setText("🔋 " + batteryNumber + "번 보조배터리를 가져가세요!");
        }).addOnFailureListener(e -> {
            textUserInfo.setText("🔋 보조배터리 정보를 불러올 수 없습니다.");
        });

        // ✅ 시간 정보는 여전히 GlobalData에서 가져옴
        String startTime = GlobalData.startTime != null ? GlobalData.startTime : "14:00";
        String endTime = GlobalData.endTime != null ? GlobalData.endTime : "15:30";
        textStartTime.setText("🕒 사용 시작 시간: " + startTime);
        textEndTime.setText("📅 반납 예정 시간: " + endTime);

        // ✅ 버튼 클릭 시: Temi 이동 + Firebase에 open=false 저장 + 메인 화면 이동
        btnBackToMain.setOnClickListener(v -> {
            if (robot != null && robot.getLocations().contains(returnStation)) {
                robot.goTo(returnStation);
                Toast.makeText(this, "Temi가 충전 스테이션으로 돌아갑니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Temi 위치 정보가 불안정하거나 '충전 스테이션' 위치가 없습니다.", Toast.LENGTH_LONG).show();
            }

            ref.child("open").setValue(false);  // ✅ open 상태 종료 처리

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
