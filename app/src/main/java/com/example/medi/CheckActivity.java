package com.example.medi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.Calendar;

public class CheckActivity extends AppCompatActivity {

    private TextView text_check_date;
    private Button btn_check_add;
    private LinearLayout layout_check_checklist;
    private boolean first_execute = true;
    private Integer YEAR;
    private Integer MONTH;
    private Integer DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setTitle("복용상태 체크");

        text_check_date = findViewById(R.id.text_date);
        btn_check_add = findViewById(R.id.btn_add);
        layout_check_checklist = findViewById(R.id.layout_checklist);

        // 1. 인텐트에서 받아온 날짜 정보 세팅
        Intent inIntent = getIntent();
        String today = inIntent.getStringExtra("selected_date");
        text_check_date.setText(today);

        // YEAR, MONTH, DAY 할당
        YEAR = Integer.parseInt(today.substring(0, 4));
        MONTH = Integer.parseInt(today.substring(6, 8));
        DAY = Integer.parseInt(today.substring(10, 12));

        // 2. 약 리스트 불러오기 - first Execution 때만 실행
        if (first_execute) {
            String keyList = today + "/list";
            String keyCheck = today + "/check";
            ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
            ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);

            for (int i = 0; i < medi_lists.size(); i++) {
                String medi_name = medi_lists.get(i);
                Boolean medi_check = medi_checks.get(i);

                // Checkbox 생성
                CheckBox cb = new CheckBox(CheckActivity.this);
                cb.setText(medi_name);
                cb.setPaintFlags(cb.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                cb.setTextSize(20);
                if (medi_check) {
                    cb.setChecked(true);
                    cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

                // checkbox의 onClick 함수 - 체크
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cbx = (CheckBox) v;

                        // 체크된거면 체크 표시, 반대라면 없애주기 & Preference 업데이트
                        if (cbx.isChecked()) {
                            cbx.setPaintFlags(cbx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            // preference 데이터 가져오고 수정
                            ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                            ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                            medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.TRUE);
                            m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);
                        } else {
                            cbx.setPaintFlags(0);

                            // preference 데이터 가져오고 수정
                            ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                            ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                            medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.FALSE);
                            m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);
                        }
                    }
                });

                // checkbox의 onLongClick 함수 - 삭제할건지 물어보는 AlertDialog 출현
                cb.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        // AlertDialog 생성
                        AlertDialog.Builder adg = new AlertDialog.Builder(CheckActivity.this);
                        adg.setTitle("삭제").setMessage("이 약을 삭제하시겠습니까?");

                        // 아니오 버튼
                        adg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int x) {
                                // 할 거 없음
                            }
                        });

                        // 예 버튼 - 약 삭제
                        adg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int x) {
                                // preference 데이터 가져오고 수정
                                ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                                ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                                int index = medi_lists.indexOf(cb.getText().toString());
                                medi_lists.remove(index);
                                medi_checks.remove(index);

                                // preference 업데이트
                                m_PreferenceManager.setStringArrayList(CheckActivity.this, keyList, medi_lists);
                                m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);

                                // 삭제해주기
                                layout_check_checklist.removeView(cb);
                                Toast.makeText(CheckActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        adg.show();
                        return false;
                    }
                });

                layout_check_checklist.addView(cb);
            }
            first_execute = false;
        }

        // 3. 추가 버튼 onClick 이벤트 세팅
        // 3. set button onClick event
        btn_check_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(CheckActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this);
                builder.setTitle("약의 이름을 입력해주세요.");
                builder.setView(edittext);
                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox cb = new CheckBox(CheckActivity.this);
                        cb.setText(edittext.getText().toString());
                        cb.setPaintFlags(cb.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                        cb.setTextSize(20);

                        // preference 업데이트
                        String keyList = today + "/list";
                        String keyCheck = today + "/check";
                        ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                        ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                        medi_lists.add(edittext.getText().toString());
                        medi_checks.add(Boolean.FALSE);
                        m_PreferenceManager.setStringArrayList(CheckActivity.this, keyList, medi_lists);
                        m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);

                        // 체크박스의 onClick 이벤트 - 체크
                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox cbx = (CheckBox) v;
                                if (cbx.isChecked()) {
                                    cbx.setPaintFlags(cbx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                    // preference 데이터 가져오고 수정
                                    ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                                    ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                                    medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.TRUE);
                                    m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);
                                }
                                else {
                                    cbx.setPaintFlags(0);

                                    // preference 데이터 가져오고 수정
                                    ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                                    ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                                    medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.FALSE);
                                    m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);
                                }
                            }
                        });

                        // 체크박스의 onLongClick 이벤트 - 삭제
                        cb.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                                // 위와 동일하게 삭제할 것인지 물어보는 AlertDialog 생성
                                AlertDialog.Builder adg = new AlertDialog.Builder(CheckActivity.this);
                                adg.setTitle("삭제").setMessage("이 약을 삭제하시겠습니까?");

                                adg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 아무것도 안함
                                    }
                                });

                                // 예 버튼 - 약 삭제
                                adg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // preference 데이터 가져오고 수정
                                        ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(CheckActivity.this, keyList);
                                        ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(CheckActivity.this, keyCheck);
                                        int index = medi_lists.indexOf(cb.getText().toString());
                                        medi_lists.remove(index);
                                        medi_checks.remove(index);

                                        // preference 업데이트
                                        m_PreferenceManager.setStringArrayList(CheckActivity.this, keyList, medi_lists);
                                        m_PreferenceManager.setBoolArrayList(CheckActivity.this, keyCheck, medi_checks);

                                        // view 제거 & 완료 메시지 출력
                                        layout_check_checklist.removeView(cb);
                                        Toast.makeText(CheckActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                adg.show();
                                return false;
                            }
                        });

                        layout_check_checklist.addView(cb);
                    }
                });

                // 약을 추가하지 않고 그대로 돌아가는 버튼 추가
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 할거없음
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("year", YEAR);
        resultIntent.putExtra("month", MONTH);
        resultIntent.putExtra("day", DAY);
        setResult(Activity.RESULT_OK, resultIntent);

        super.onBackPressed();
    }
}
