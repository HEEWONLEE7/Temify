package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    TextView stockText;
    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        stockText = findViewById(R.id.stockText);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        fetchBatteryStock();

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(StockActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void fetchBatteryStock() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("status");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int availableCount = 0;
                List<String> availableBatteries = new ArrayList<>();

                for (DataSnapshot battery : snapshot.getChildren()) {
                    String batteryId = battery.getKey();  // ex: "Batt2"
                    String isBattery = String.valueOf(battery.child("isBattery").getValue());
                    String levelStr = String.valueOf(battery.child("BatLevel1").getValue());

                    try {
                        double level = Double.parseDouble(levelStr);

                        if ("true".equalsIgnoreCase(isBattery) && level >= 0.0) {
                            availableCount++;
                            availableBatteries.add(batteryId);
                        }
                    } catch (Exception e) {
                        Log.e("StockActivity", "파싱 실패: " + e.getMessage());
                    }
                }

                // ✅ 최종 출력 문자열
                String result = "🔋 사용 가능한 보조배터리는 " + availableCount + "개입니다.\n\n";
                if (availableCount > 0) {
                    result += "✅ 사용 가능 목록:\n";
                    for (String id : availableBatteries) {
                        result += "• " + id + "\n";
                    }
                } else {
                    result += "❌ 사용 가능한 배터리가 없습니다.";
                }

                stockText.setText(result.trim());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(StockActivity.this, "❌ 서버 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("StockActivity", "Firebase Error: " + error.getMessage());
            }
        });
    }
}
