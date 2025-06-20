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

import java.util.List;

public class ExtendSuccessActivity extends AppCompatActivity {

    TextView textSuccess, textUsageInfo;
    Button btnBackToMain;

    private Robot robot;
    private final String returnStation = "홈베이스";  // Temi에 저장된 위치 이름

    private final DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
    private final DatabaseReference flagsRef = FirebaseDatabase.getInstance().getReference("Flags");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_success);

        textSuccess = findViewById(R.id.textSuccess);
        textUsageInfo = findViewById(R.id.textUsageInfo);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        robot = Robot.getInstance();

        // ✅ Intent로부터 전달받은 연장 정보 사용
        Intent intent = getIntent();
        String extendedTime = intent.getStringExtra("extendedTime");
        String originalStart = intent.getStringExtra("startTime");
        String newEnd = intent.getStringExtra("endTime");

        if (extendedTime == null) extendedTime = "알 수 없음";
        if (originalStart == null) originalStart = "??:??";
        if (newEnd == null) newEnd = "??:??";

        // ✅ UI 출력
        textSuccess.setText("✅ 대여가 " + extendedTime + " 연장되었습니다!");
        textUsageInfo.setText("🕒 사용 시간: " + originalStart + " ~ " + newEnd +
                "\n📈 최종 반납 시간: " + newEnd);

        // ✅ 버튼 클릭 시: open = false 설정만 (화면 전환 없음)
        btnBackToMain.setOnClickListener(v -> {
            reservationRef.child("open").setValue(false);
            Toast.makeText(this, "🛑 Temi 사용 종료 처리 완료. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
        });

        // ✅ Flags/Flag == "end" 감지 시 → Temi 이동 + MainActivity 전환
        flagsRef.child("Flag").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String flag = snapshot.getValue(String.class);
                if ("end".equals(flag)) {
                    List<String> locations = robot.getLocations();
                    if (locations.contains(returnStation)) {
                        robot.goTo(returnStation);
                        Toast.makeText(ExtendSuccessActivity.this, "✅ Temi가 '" + returnStation + "'으로 이동합니다!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExtendSuccessActivity.this, "⚠️ Temi에 '" + returnStation + "' 위치가 저장되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                    }

                    // 화면 전환
                    Intent intent = new Intent(ExtendSuccessActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExtendSuccessActivity.this, "⚠️ Flag 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
