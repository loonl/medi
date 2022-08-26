package com.example.medi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private Button btnStartDate;
    private Button btnEndDate;
    private Button btnExecute;
    private TextView textStartDate;
    private TextView textEndDate;
    private EditText etInterval;
    private EditText etMediName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("약 추가");

        // 버튼과 text, edittext 바인딩
        btnStartDate = findViewById(R.id.btn_add_startdate);
        btnEndDate = findViewById(R.id.btn_add_enddate);
        btnExecute = findViewById(R.id.btn_add_allmedi);
        textStartDate = findViewById(R.id.text_add_startdate);
        textEndDate = findViewById(R.id.text_add_enddate);
        etInterval = findViewById(R.id.et_add_interval);
        etMediName = findViewById(R.id.et_add_mediname);

        // 시작 날짜 관련 설정
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 날짜 선택해서 넣어주는 구문
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                                // Date Picker에서 선택한 날짜를 startDate text에 설정
                                String YY, MM, DD; mm++;
                                YY = yy + "년 ";
                                if (mm < 10)
                                    MM = "0" + mm + "월 ";
                                else
                                    MM = mm + "월 ";
                                if (dd < 10)
                                    DD = "0" + dd + "일";
                                else
                                    DD = dd + "일";

                                textStartDate.setText(YY + MM + DD);
                            }
                        };

                if (textStartDate.getText() == "") {
                    Calendar cal = Calendar.getInstance();
                    new DatePickerDialog(AddActivity.this, mDateSetListener, cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
                }
                else {
                    String temp = textStartDate.getText().toString();
                    int year = Integer.parseInt(temp.substring(0, 4));
                    int month = Integer.parseInt(temp.substring(6, 8));
                    int day = Integer.parseInt(temp.substring(10, 12));
                    new DatePickerDialog(AddActivity.this, mDateSetListener, year, month - 1, day).show();
                }
            }
        });

        // 종료 날짜 관련 설정
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 날짜 선택해서 넣어주는 구문
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                                // Date Picker에서 선택한 날짜를 startDate text에 설정
                                String YY, MM, DD; mm++;
                                YY = yy + "년 ";
                                if (mm < 10)
                                    MM = "0" + mm + "월 ";
                                else
                                    MM = mm + "월 ";
                                if (dd < 10)
                                    DD = "0" + dd + "일";
                                else
                                    DD = dd + "일";

                                textEndDate.setText(YY + MM + DD);
                            }
                        };

                if (textStartDate.getText() == "") {
                    Calendar cal = Calendar.getInstance();
                    new DatePickerDialog(AddActivity.this, mDateSetListener, cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
                }
                else {
                    String temp = textStartDate.getText().toString();
                    int year = Integer.parseInt(temp.substring(0, 4));
                    int month = Integer.parseInt(temp.substring(6, 8));
                    int day = Integer.parseInt(temp.substring(10, 12));
                    new DatePickerDialog(AddActivity.this, mDateSetListener, year, month - 1, day).show();
                }
            }
        });

        // 일괄 추가 버튼 관련 설정
        btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 안되는 cases handling
                if (textStartDate.getText().toString().compareTo(textEndDate.getText().toString()) >= 0
                && textStartDate.getText().toString().length() > 0 && textEndDate.getText().toString().length() > 0)
                    Toast.makeText(AddActivity.this, "마지막 날짜를 시작 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                else if (textStartDate.getText().toString().length() == 0 || textEndDate.getText().toString().length() == 0 ||
                        etInterval.getText().toString().length() == 0 || etMediName.getText().toString().length() == 0)
                    Toast.makeText(AddActivity.this, "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();

                // 밑의 if문 구문에서 앱이 crash 나는 사태 방지
                else if (etInterval.getText().toString().length() > 0) {
                    if (Integer.parseInt(etInterval.getText().toString()) <= 0)
                        Toast.makeText(AddActivity.this, "간격을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();

                    // 정상적으로 동작되었을 때
                    else {
                        Calendar startCal = Calendar.getInstance();
                        Calendar endCal = Calendar.getInstance();
                        String temp;

                        // 시작 날짜 세팅
                        temp = textStartDate.getText().toString();
                        startCal.set(Calendar.YEAR, Integer.parseInt(temp.substring(0, 4)));
                        startCal.set(Calendar.MONTH, Integer.parseInt(temp.substring(6, 8)) - 1);
                        startCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp.substring(10, 12)));

                        // 종료 날짜 세팅
                        temp = textEndDate.getText().toString();
                        endCal.set(Calendar.YEAR, Integer.parseInt(temp.substring(0, 4)));
                        endCal.set(Calendar.MONTH, Integer.parseInt(temp.substring(6, 8)) - 1);
                        endCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp.substring(10, 12)));


                        Calendar pivot = startCal;
                        String medicineName = etMediName.getText().toString();
                        int interval = Integer.parseInt(etInterval.getText().toString());

                        // preference에 약 목록 업데이트
                        while (pivot.compareTo(endCal) <= 0) {
                            String keyDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(pivot.getTime());
                            String keyList = keyDate + "/list";
                            String keyCheck = keyDate + "/check";
                            ArrayList<String> medis = m_PreferenceManager.getStringArrayList(AddActivity.this, keyList);
                            ArrayList<Boolean> checks = m_PreferenceManager.getBoolArrayList(AddActivity.this, keyCheck);
                            medis.add(medicineName); checks.add(Boolean.FALSE);
                            m_PreferenceManager.setStringArrayList(AddActivity.this, keyList, medis);
                            m_PreferenceManager.setBoolArrayList(AddActivity.this, keyCheck, checks);
                            pivot.add(Calendar.DATE, interval);
                        }

                        Toast.makeText(AddActivity.this, "추가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });


    }
}