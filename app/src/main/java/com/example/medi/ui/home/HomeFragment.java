package com.example.medi.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.medi.CheckActivity;
import com.example.medi.EventDecorator;
import com.example.medi.R;
import com.example.medi.databinding.FragmentHomeBinding;
import com.example.medi.m_PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private MaterialCalendarView calendar;
    private int calendarCurrentMonth;
    private int calendarCurrentYear;

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

        // 현재 calendar 가 보내는 것들을 설정
        calendarCurrentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        calendarCurrentYear = Calendar.getInstance().get(Calendar.YEAR);

        // 캘린더 찾아주기
        calendar = root.findViewById(R.id.main_calendar);

        // 캘린더 날짜 선택하면 나오는 색은 회색으로
        calendar.setSelectionColor(Color.LTGRAY);

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
        ArrayList<String> dayFin = new ArrayList<String>();
        ArrayList<String> dayNotFin = new ArrayList<String>();
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

                String element = calendarCurrentYear + "," + calendarCurrentMonth + "," + i;
                if (allchecked)
                    dayFin.add(element);
                else
                    dayNotFin.add(element);
            }
        }

        // 점 찍기
        new DotMaker(dayFin, Color.GREEN).executeOnExecutor(Executors.newSingleThreadExecutor());
        new DotMaker(dayNotFin, Color.RED).executeOnExecutor(Executors.newSingleThreadExecutor());

        // 캘린더 클릭 이벤트
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay day, boolean selected) {

                // 클릭된 날짜 담아주기
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, day.getYear());
                calendar.set(Calendar.MONTH, day.getMonth() - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day.getDay());
                Date date_ = calendar.getTime();
                String selectedDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(date_);

                // 인텐트에 담아서 CheckActivity로 넘겨주기
                Intent sendIntent = new Intent(getActivity(), CheckActivity.class);
                sendIntent.putExtra("selected_date", selectedDate);
                startActivity(sendIntent);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 빨간 점 찍어주는 CLASS
    private class DotMaker extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> Time_Result;
        int color;

        DotMaker(ArrayList<String> Time_Result, int color){
            this.Time_Result = Time_Result;
            this.color = color;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.size(); i ++){
                String[] time = Time_Result.get(i).split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                CalendarDay day = CalendarDay.from(LocalDate.of(year, month, dayy));
                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            calendar.addDecorator(new EventDecorator(color, calendarDays, getActivity()));
        }
    }
}