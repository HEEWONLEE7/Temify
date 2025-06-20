package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import java.util.List;

public class MovingToUserActivity extends AppCompatActivity {

    private Robot robot;
    private TextView textMoving;

    private DatabaseReference callRequestsRef;
    private final DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        reservationRef.child("move").setValue(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_to_user);

        textMoving = findViewById(R.id.textMoving);
        robot = Robot.getInstance();
        callRequestsRef = FirebaseDatabase.getInstance().getReference("callRequests");

        // ✅ callRequests/seat 값만 불러오기
        callRequestsRef.child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot numberSnapshot) {
                String targetLocation = numberSnapshot.getValue(String.class);

                if (targetLocation == null || targetLocation.isEmpty()) {
                    Toast.makeText(MovingToUserActivity.this, "❌ 좌석 정보가 없습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                startTemiMovement(targetLocation);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MovingToUserActivity.this, "❌ seat 정보 접근 실패", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void startTemiMovement(String targetLocation) {
        new Handler().postDelayed(() -> {
            List<String> locations = robot.getLocations();
            Log.d("TemiDebug", "Temi 위치 목록: " + locations);

            if (!locations.contains(targetLocation)) {
                Toast.makeText(this, "Temi에 '" + targetLocation + "' 위치가 없습니다.", Toast.LENGTH_LONG).show();
                textMoving.setText("🚫 Temi 위치 데이터 없음: " + targetLocation);
                return;
            }

            textMoving.setText("🤖 Temi가 " + targetLocation + "으로 이동 중입니다...");
            robot.goTo(targetLocation);

            // ✅ 이동 상태 리스너 등록
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

        }, 1000); // Temi SDK 초기화 대기
    }

    private void moveToNextActivity() {
        Intent intent = new Intent(this, UserAuthActivity.class);
        startActivity(intent);
        finish();
    }
}
