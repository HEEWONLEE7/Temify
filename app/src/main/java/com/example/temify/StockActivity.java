package com.example.temify;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StockActivity extends AppCompatActivity {

    TextView stockText;
    // 추가
    Button btnBackToMain;

    btnBackToMain = findViewById(R.id.btnBackToMain);
    btnBackToMain.setOnClickListener(v -> {
        Intent intent = new Intent(StockActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        stockText = findViewById(R.id.stockText);

        // 서버 없이 일단은 하드코딩 값 사용
        int availableCount = 5;
        stockText.setText("🔋 사용 가능한 보조배터리는 " + availableCount + "개입니다.");
    }
}
