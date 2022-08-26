package com.example.medi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // ListViewAdapter의 생성자
    public ListViewAdapter(ArrayList<ListViewItem> t) {
        listViewItemList = t;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView textView = convertView.findViewById(R.id.text_listview);
        Switch SWITCH = convertView.findViewById(R.id.switch_listview);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // textView 관련 롱클릭 이벤트
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // 삭제 여부 묻고, 예 클릭 시 제거
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("시간 삭제").setMessage("선택하신 시간을 삭제하시겠습니까?");

                // 예 버튼 관련 클릭 이벤트
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 활성화 되어있었다면 삭제하기 전에 알림 해제
                        Integer code = m_PreferenceManager.getRequestCode(context, listViewItem.getName());
                        AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, alarmIntent, PendingIntent.FLAG_MUTABLE);
                        alarmManager.cancel(pendingIntent);

                        // 예 눌리면 그 아이템 삭제
                        m_PreferenceManager.removeKey(context, listViewItem.getName());
                        listViewItemList.remove(position);
                        notifyDataSetChanged(context);

                        Toast.makeText(context, "알림이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 아무것도 안함
                    }
                });

                // alertdialog 보여주기
                builder.show();
                return true;
            }
        });

        // 스위치 관련 ON/OFF 이벤트 핸들러
        SWITCH.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String temp = listViewItem.getName();

                // 만약 활성화 상태라면 알림 활성화 (ON일 때)
                if (b) {
                    PackageManager pm = context.getPackageManager();
                    ComponentName receiver = new ComponentName(context, DeviceBootReceiver.class);
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    int code = Integer.parseInt(temp.substring(0, 2) + temp.substring(5, 7));
                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, alarmIntent, PendingIntent.FLAG_MUTABLE); // 키 값은 position
                    AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));

                    // preference 업데이트
                    m_PreferenceManager.setRequestCode(context, listViewItem.getName(), code);

                    // hour, minute 추출해서 calendar로 변환
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp.substring(0, 2)));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(temp.substring(5, 7)));
                    calendar.set(Calendar.SECOND, 0);

                    // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                    if (calendar.before(Calendar.getInstance())) {
                            calendar.add(Calendar.DATE, 1);
                    }

                    // 반복 설정
                    if (alarmManager != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        else {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                    AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                    }

                    // 부팅 후 실행되는 리시버 사용가능하게 설정
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }

                // check 상태가 아니라면 알림 비활성화 (OFF 일 때)
                else {
                    AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            m_PreferenceManager.getRequestCode(context, listViewItem.getName()),
                            alarmIntent, PendingIntent.FLAG_MUTABLE);
                    alarmManager.cancel(pendingIntent);
                }

                // 데이터 업데이트
                listViewItem.setEnabled(b);
                notifyDataSetChanged(context);
            }
        });

        // 아이템 내 각 위젯에 데이터 반영
        textView.setText(listViewItem.getName());
        SWITCH.setChecked(listViewItem.getEnabled());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(Context context, String name, Boolean bool) {
        ListViewItem item = new ListViewItem();

        item.setName(name);
        item.setEnabled(bool);

        listViewItemList.add(item);
        notifyDataSetChanged(context);
    }

    // 정렬
    public void notifyDataSetChanged(Context context) {
        Comparator<ListViewItem> comp = new Comparator<ListViewItem>() {
            @Override
            public int compare(ListViewItem item1, ListViewItem item2) {
                return item1.getName().compareTo(item2.getName());
            }
        };

        Collections.sort(listViewItemList, comp);
        m_PreferenceManager.setItemList(context, "key", listViewItemList);
        super.notifyDataSetChanged();
    }
}
