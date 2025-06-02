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
    private String targetLocation = "5번 자리";
    private TextView textMoving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_to_user);

        textMoving = findViewById(R.id.textMoving);
        robot = Robot.getInstance();

        textMoving.setText("🤖 Temi가 " + targetLocation + "으로 이동 중입니다...");
        robot.goTo(targetLocation);

        // ✅ 도착 상태 리스너 등록 (SDK 1.135.1 기준)
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
        Intent intent = new Intent(MovingToUserActivity.this, UserAuthActivity.class);
        intent.putExtra("seatNumber", targetLocation);
        startActivity(intent);
        finish();
    }
}
