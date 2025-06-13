package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import java.util.List;

public class MovingToUserActivity extends AppCompatActivity {

    private Robot robot;
    private String targetLocation = "5번 자리";  // Temi에 저장된 위치 이름
    private TextView textMoving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_to_user);

        textMoving = findViewById(R.id.textMoving);
        robot = Robot.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");

        GlobalData.seatNumber = targetLocation;

        // ✅ 화면이 뜨는 순간 → Temi가 이동 시작으로 간주 → 충전 아님
        ref.child("charging").setValue(0);

        // ✅ Temi 이동 상태 리스너 등록
        robot.addOnGoToLocationStatusChangedListener(new OnGoToLocationStatusChangedListener() {
            @Override
            public void onGoToLocationStatusChanged(String location, String status, int id, String description) {
                if (!location.equals(targetLocation)) return;

                runOnUiThread(() -> {
                    switch (status.toLowerCase()) {
                        case "complete":
                            textMoving.setText("✅ Temi가 " + location + "에 도착했습니다!");
                            moveToNextActivity();
                            break;
                        case "abort":
                            Toast.makeText(MovingToUserActivity.this, "❌ Temi가 이동을 중단했습니다.", Toast.LENGTH_LONG).show();
                            break;
                        case "error":
                            Toast.makeText(MovingToUserActivity.this, "⚠️ Temi 이동 중 오류 발생", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(MovingToUserActivity.this, "ℹ️ 현재 상태: " + status, Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ✅ 화면이 뜨는 순간 → Temi가 이동 시작으로 간주 → 충전 아님


        new Handler().postDelayed(() -> {
            List<String> locations = robot.getLocations();
            Log.d("TemiDebug", "지연 후 Temi 위치 목록: " + locations);

            if (!locations.contains(targetLocation)) {
                Toast.makeText(this, "Temi에 '" + targetLocation + "' 위치가 없습니다.", Toast.LENGTH_LONG).show();
                textMoving.setText("🚫 Temi 위치 데이터 없음: " + targetLocation);
                return;
            }

            textMoving.setText("🤖 Temi가 " + targetLocation + "으로 이동 중입니다...");
            robot.goTo(targetLocation);

        }, 1000); // Temi SDK 초기화 시간 확보
    }

    private void moveToNextActivity() {
        Intent intent = new Intent(MovingToUserActivity.this, UserAuthActivity.class);
        startActivity(intent);
        finish();
    }
}
