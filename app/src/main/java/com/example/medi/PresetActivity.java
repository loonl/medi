package com.example.medi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PresetActivity extends AppCompatActivity {
    private ImageView imgMedi1;
    private ImageView imgMedi2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset);

        // title bar 설정
        setTitle("약 추가");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3454B7")));

        // 바인딩
        imgMedi1 = findViewById(R.id.img_preset_medi1);
        imgMedi2 = findViewById(R.id.img_preset_medi2);

        // intent 생성
        Intent intent = new Intent(PresetActivity.this, AddActivity.class);

        // imgMedi1 클릭 이벤트
        imgMedi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("code", 1);
                startActivity(intent);
            }
        });

        // imgMedi2 클릭 이벤트
        imgMedi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("code", 2);
                startActivity(intent);
            }
        });
    }
}
