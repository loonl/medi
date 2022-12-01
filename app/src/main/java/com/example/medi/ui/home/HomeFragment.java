package com.example.medi.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.medi.CheckActivity;
import com.example.medi.EventDecorator;
import com.example.medi.R;
import com.example.medi.SelectedDecorator;
import com.example.medi.databinding.FragmentHomeBinding;
import com.example.medi.m_PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import org.threeten.bp.LocalDate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private MaterialCalendarView calendar;

    private Integer calendarCurrentMonth;
    private Integer calendarCurrentYear;
    private ArrayList<CalendarDay> dayFin;
    private ArrayList<CalendarDay> dayNotFin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 알람 floating Button 생성
        try {
            FloatingActionButton fab = getActivity().findViewById(R.id.fab);
            fab.show();
        } catch (Exception e) {
        }

        // 처음 설치하고 실행했을 시 해당 내용 고지
        Boolean isFirst = m_PreferenceManager.getFirstNotice(getActivity());
        if (isFirst) {
            AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
            adg.setTitle("어플 안내").setMessage("해당 어플은 전문의약품 에스리시정을 처방 받은 환자의 " +
                    "복용 순응도를 높이기 위해 제작되었습니다. 의사의 진료 및 약사의 복약 지도를 대신 하지 않으며, "
            + "질병의 진단 및 치료에 관한 정확하고 자세한 사항은 담당 의사 선생님께 문의하십시오.");
            adg.setPositiveButton("네, 확인했습니다.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    m_PreferenceManager.setFirstNotice(getActivity(), false);
                }
            });

            adg.show();
        }



        // 현재 calendar 가 보내는 것들을 설정
        calendarCurrentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        calendarCurrentYear = Calendar.getInstance().get(Calendar.YEAR);

        // 캘린더 찾아주기
        calendar = root.findViewById(R.id.main_calendar);

        // 캘린더 날짜 선택하면 나오는 색은 회색으로
        //calendar.setSelectionColor(Color.LTGRAY);

        // 캘린더 날짜 선택하면 나오는 효과는 비활성화
        calendar.addDecorators(new SelectedDecorator(getActivity()));

        // 캘린더 중앙에 년 / 월이 보이는 방식 변경
        calendar.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // CalendarDay -> LocalDate 로 변환 후 잘라서 세팅
                LocalDate inputText = day.getDate();
                String[] calendarHeaderElements = inputText.toString().split("-");
                StringBuilder calendarHeaderBuilder = new StringBuilder();
                calendarHeaderElements[1] = Integer.toString(Integer.parseInt(calendarHeaderElements[1])); // 08 -> 8 같이 앞의 0 제거
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                        .append("년 ")
                        .append(calendarHeaderElements[1]).append("월");
                return calendarHeaderBuilder.toString();
            }
        });

        // 약 다 먹은 날, 안 먹은 날 구분
        dayFin = new ArrayList<CalendarDay>();
        dayNotFin = new ArrayList<CalendarDay>();
        for (int i = 1; i < Calendar.getInstance().get(Calendar.DAY_OF_MONTH); i++) {
            // 캘린더 설정해주고 체크리스트 가져오기
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, calendarCurrentYear);
            cal.set(Calendar.MONTH, calendarCurrentMonth - 1);
            cal.set(Calendar.DAY_OF_MONTH, i);
            String key = new SimpleDateFormat("yyyy년 MM월 dd일").format(cal.getTime()) + "/check";
            ArrayList<Boolean> checked = m_PreferenceManager.getBoolArrayList(getActivity(), key);
            // 전부 true라면 dayFin에, 하나라도 false라면 dayNotFin에, 하나도 없다면 아무데도 추가 X
            if (!checked.isEmpty()) {
                Boolean allchecked = true;
                for (int j = 0; j < checked.size(); j++) {
                    if (!checked.get(j)) {
                        allchecked = false;
                        break;
                    }
                }

                CalendarDay element = CalendarDay.from(LocalDate.of(calendarCurrentYear, calendarCurrentMonth, i));
                if (allchecked)
                    dayFin.add(element);
                else
                    dayNotFin.add(element);
            }
        }

        // 점 찍기
        calendar.addDecorator(new EventDecorator(Color.rgb(0, 51, 153), dayFin, getActivity()));
        calendar.addDecorator(new EventDecorator(Color.RED, dayNotFin, getActivity()));

        // 밑에서도 써야 해서 미리 clear
        dayFin.clear();
        dayNotFin.clear();

        // 캘린더 클릭 이벤트
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay day, boolean selected) {

                // 클릭된 날짜 담아주기
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, day.getYear());
                cal.set(Calendar.MONTH, day.getMonth() - 1);
                cal.set(Calendar.DAY_OF_MONTH, day.getDay());
                Date date_ = cal.getTime();
                String selectedDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(date_);

                // 인텐트에 담아서 CheckActivity로 넘겨주기
                Intent sendIntent = new Intent(getActivity(), CheckActivity.class);
                sendIntent.putExtra("selected_date", selectedDate);
                startActivity(sendIntent);
            }
        });

        // 달 바뀌는 이벤트
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                // 변수 업데이트
                calendarCurrentMonth = date.getMonth();
                calendarCurrentYear = date.getYear();
                String str_curMonth = "";
                if (calendarCurrentMonth < 10)
                    str_curMonth = "0" + Integer.toString(calendarCurrentMonth);
                else
                    str_curMonth = Integer.toString(calendarCurrentMonth);

                int cmp2 = Integer.parseInt(Integer.toString(calendarCurrentYear) + str_curMonth);
                int now = Integer.parseInt(new SimpleDateFormat("yyyyMM").format(Calendar.getInstance().getTime()));

                // 과거의 시각인 연/월에 대해서만 값을 찍어주면 됨
                if (now > cmp2) {
                    // dayFin, dayNotFin 채워주기
                    Calendar maxDaysCal = Calendar.getInstance();
                    maxDaysCal.set(Calendar.YEAR, calendarCurrentYear);
                    maxDaysCal.set(Calendar.MONTH, calendarCurrentMonth);
                    for (int i = 1; i <= maxDaysCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        // 캘린더 설정해주고 체크리스트 가져오기
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, calendarCurrentYear);
                        cal.set(Calendar.MONTH, calendarCurrentMonth - 1);
                        cal.set(Calendar.DAY_OF_MONTH, i);
                        String key = new SimpleDateFormat("yyyy년 MM월 dd일").format(cal.getTime()) + "/check";
                        ArrayList<Boolean> checked = m_PreferenceManager.getBoolArrayList(getActivity(), key);
                        // 전부 true라면 dayFin에, 하나라도 false라면 dayNotFin에, 하나도 없다면 아무데도 추가 X
                        if (!checked.isEmpty()) {
                            Boolean allchecked = true;
                            for (int j = 0; j < checked.size(); j++) {
                                if (!checked.get(j)) {
                                    allchecked = false;
                                    break;
                                }
                            }

                            CalendarDay element = CalendarDay.from(LocalDate.of(calendarCurrentYear, calendarCurrentMonth, i));
                            if (allchecked)
                                dayFin.add(element);
                            else
                                dayNotFin.add(element);
                        }
                    }

                    // 점 찍어주기
                    calendar.addDecorator(new EventDecorator(Color.rgb(0, 51, 153), dayFin, getActivity()));
                    calendar.addDecorator(new EventDecorator(Color.RED, dayNotFin, getActivity()));

                    // 다음을 위해서 두 List 초기화
                    dayFin.clear();
                    dayNotFin.clear();
                }
            }
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 시간 비교
        String str_curMonth = "";
        if (calendarCurrentMonth < 10)
            str_curMonth = "0" + Integer.toString(calendarCurrentMonth);
        else
            str_curMonth = Integer.toString(calendarCurrentMonth);

        int cmp2 = Integer.parseInt(Integer.toString(calendarCurrentYear) + str_curMonth);
        int now = Integer.parseInt(new SimpleDateFormat("yyyyMM").format(Calendar.getInstance().getTime()));

        // 과거라면 업데이트

        // 코드 재활용
        // 과거의 시각인 연/월에 대해서만 값을 찍어주면 됨
        if (now > cmp2) {
            // dayFin, dayNotFin 채워주기
            Calendar maxDaysCal = Calendar.getInstance();
            maxDaysCal.set(Calendar.YEAR, calendarCurrentYear);
            maxDaysCal.set(Calendar.MONTH, calendarCurrentMonth);
            for (int i = 1; i <= maxDaysCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                // 캘린더 설정해주고 체크리스트 가져오기
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, calendarCurrentYear);
                cal.set(Calendar.MONTH, calendarCurrentMonth - 1);
                cal.set(Calendar.DAY_OF_MONTH, i);
                String key = new SimpleDateFormat("yyyy년 MM월 dd일").format(cal.getTime()) + "/check";
                ArrayList<Boolean> checked = m_PreferenceManager.getBoolArrayList(getActivity(), key);
                // 전부 true라면 dayFin에, 하나라도 false라면 dayNotFin에, 하나도 없다면 아무데도 추가 X
                if (!checked.isEmpty()) {
                    Boolean allchecked = true;
                    for (int j = 0; j < checked.size(); j++) {
                        if (!checked.get(j)) {
                            allchecked = false;
                            break;
                        }
                    }

                    CalendarDay element = CalendarDay.from(LocalDate.of(calendarCurrentYear, calendarCurrentMonth, i));
                    if (allchecked)
                        dayFin.add(element);
                    else
                        dayNotFin.add(element);
                }
            }

            // 점 찍어주기
            calendar.addDecorator(new EventDecorator(Color.rgb(0, 51, 153), dayFin, getActivity()));
            calendar.addDecorator(new EventDecorator(Color.RED, dayNotFin, getActivity()));

            // 다음을 위해서 두 List 초기화
            dayFin.clear();
            dayNotFin.clear();
        }

        else if (now == cmp2) {
            // 코드 재활용
            for (int i = 1; i < Calendar.getInstance().get(Calendar.DAY_OF_MONTH); i++) {
                // 캘린더 설정해주고 체크리스트 가져오기
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, calendarCurrentYear);
                cal.set(Calendar.MONTH, calendarCurrentMonth - 1);
                cal.set(Calendar.DAY_OF_MONTH, i);
                String key = new SimpleDateFormat("yyyy년 MM월 dd일").format(cal.getTime()) + "/check";
                ArrayList<Boolean> checked = m_PreferenceManager.getBoolArrayList(getActivity(), key);
                // 전부 true라면 dayFin에, 하나라도 false라면 dayNotFin에, 하나도 없다면 아무데도 추가 X
                if (!checked.isEmpty()) {
                    Boolean allchecked = true;
                    for (int j = 0; j < checked.size(); j++) {
                        if (!checked.get(j)) {
                            allchecked = false;
                            break;
                        }
                    }

                    CalendarDay element = CalendarDay.from(LocalDate.of(calendarCurrentYear, calendarCurrentMonth, i));
                    if (allchecked)
                        dayFin.add(element);
                    else
                        dayNotFin.add(element);
                }
            }

            // 점 찍기
            calendar.addDecorator(new EventDecorator(Color.rgb(0, 51, 153), dayFin, getActivity()));
            calendar.addDecorator(new EventDecorator(Color.RED, dayNotFin, getActivity()));

            // dayFin, dayNotFin 초기화
            dayFin.clear();
            dayNotFin.clear();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}