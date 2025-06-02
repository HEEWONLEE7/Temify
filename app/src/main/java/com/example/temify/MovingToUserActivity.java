package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

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

        // ✅ GlobalData에 좌석 정보 저장
        GlobalData.seatNumber = targetLocation;

        // 이동 안내 텍스트 설정
        textMoving.setText("🤖 Temi가 " + targetLocation + "으로 이동 중입니다...");
        robot.goTo(targetLocation);

        // ✅ Temi 이동 상태 리스너
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

    private void moveToNextActivity() {
        // 다음 인증 액티비티로 이동하며 GlobalData 사용
        Intent intent = new Intent(MovingToUserActivity.this, UserAuthActivity.class);
        startActivity(intent);
        finish();
    }
}
