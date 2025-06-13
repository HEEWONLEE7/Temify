package com.example.temify;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FirebaseUploader {

    public static void uploadInitialData() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String now = sdf.format(cal.getTime());           // 시작 시간
        cal.add(Calendar.HOUR_OF_DAY, 2);
        String end = sdf.format(cal.getTime());           // 종료 시간

        // ✅ GlobalData에 저장
        GlobalData.startTime = now;
        GlobalData.endTime = end;

        // ✅ Firebase에 저장
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("reservation");

        ref.child("battery").setValue(GlobalData.batteryNumber != null ? GlobalData.batteryNumber : "3");
        ref.child("start_time").setValue(now);
        ref.child("end_time").setValue(end);
        ref.child("password").setValue(GlobalData.password != null ? GlobalData.password : "1234");
        ref.child("open").setValue(false);       // 초기에는 닫힘 상태
        ref.child("charging").setValue(1);       // ✅ Temi가 이동 시작 → 충전 중 아님
    }
}
