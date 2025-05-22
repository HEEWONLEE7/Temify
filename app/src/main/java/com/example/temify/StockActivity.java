package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StockActivity extends AppCompatActivity {

    TextView stockText;
    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        stockText = findViewById(R.id.stockText);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // 예시: 하드코딩된 보조배터리 수량
        int availableCount = 5;
        stockText.setText("🔋 사용 가능한 보조배터리는 " + availableCount + "개입니다.");

        // 메인으로 돌아가기 버튼 클릭 시
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(StockActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
