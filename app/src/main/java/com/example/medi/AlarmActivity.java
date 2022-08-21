package com.example.medi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {
    private ListView listviewAlarm;
    private ListViewAdapter adapter;
    private Button btnAlarmAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setTitle("알림 설정");

        // 바인딩
        ArrayList<ListViewItem> temp = m_PreferenceManager.getItemList(AlarmActivity.this, "key");
        listviewAlarm = findViewById(R.id.listview_alarm);
        adapter = new ListViewAdapter(temp);
        listviewAlarm.setAdapter(adapter);
        btnAlarmAdd = findViewById(R.id.btn_alarm_add);

        // 버튼 클릭 이벤트
        btnAlarmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // timepickerdialog 띄워서 시간 설정 가능하게 해주기
                TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmActivity.this, TimePickerDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hour, min;
                                if (hourOfDay < 10)
                                    hour = "0" + Integer.toString(hourOfDay);
                                else
                                    hour = Integer.toString(hourOfDay);

                                if (minute < 10)
                                    min = "0" + Integer.toString(minute);
                                else
                                    min = Integer.toString(minute);

                                adapter.addItem(AlarmActivity.this, hour + " : " + min, false);
                            }
                        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }
}
