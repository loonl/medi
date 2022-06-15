package com.example.medi.ui.home;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.medi.R;
import com.example.medi.databinding.FragmentHomeBinding;
import com.example.medi.m_PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView text_home_date;
    private Button btn_home_add;
    private Button btn_home_del;
    private LinearLayout layout_home_checklist;
    boolean first_execute = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // declaration
        text_home_date = root.findViewById(R.id.text_date);
        btn_home_add = root.findViewById(R.id.btn_add);
        layout_home_checklist = root.findViewById(R.id.layout_checklist);

        // 1. first execution - date set
        Boolean date_changed = false;
        long now = System.currentTimeMillis();
        String saved_time = m_PreferenceManager.getSDF(getActivity(), "date");
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        String getTime = sdf.format(date);
        text_home_date.setText(getTime);

        // 1.5. first execution - check date change and adapt into loading medicine lists && update preference
        if (!saved_time.equals(getTime)) {
            date_changed = true;
        }
        m_PreferenceManager.setSDF(getActivity(), "date", getTime);

        // 2. first execution - load medicine lists
        if (first_execute) {
            ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(getActivity(), "medi_list");
            ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(getActivity(), "medi_check");

            for (int i = 0; i < medi_lists.size(); i++) {
                String medi_name = medi_lists.get(i);
                Boolean medi_check = medi_checks.get(i);

                // make checkbox and set
                CheckBox cb = new CheckBox(getActivity());
                cb.setText(medi_name);
                cb.setPaintFlags(cb.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                cb.setTextSize(20);
                if (medi_check && !date_changed) {
                    cb.setChecked(true);
                    cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cbx = (CheckBox) v;
                        if (cbx.isChecked()) {
                            cbx.setPaintFlags(cbx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(getActivity(), "medi_list");
                            ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(getActivity(), "medi_check");
                            medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.TRUE);
                            m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);
                        } else {
                            cbx.setPaintFlags(0);
                            ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(getActivity(), "medi_list");
                            ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(getActivity(), "medi_check");
                            medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.FALSE);
                            m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);
                        }
                    }
                });

                cb.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                        adg.setTitle("삭제").setMessage("이 약을 삭제하시겠습니까?");

                        adg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int x) {
                                // do nothing
                            }
                        });

                        adg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int x) {
                                int index = medi_lists.indexOf(cb.getText().toString());
                                medi_lists.remove(index);
                                medi_checks.remove(index);
                                m_PreferenceManager.setStringArrayList(getActivity(), "medi_list", medi_lists);
                                m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);
                                layout_home_checklist.removeView(cb);
                                Toast.makeText(getActivity(), "삭제가 완료되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });

                        adg.show();
                        return false;
                    }
                });

                layout_home_checklist.addView(cb);
            }
            first_execute = false;
        }

        // 3. set button onClick event
        btn_home_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("약의 이름을 입력해주세요.");
                builder.setView(edittext);
                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox cb = new CheckBox(getActivity());
                        cb.setText(edittext.getText().toString());
                        cb.setPaintFlags( cb.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                        cb.setTextSize(20);

                        // update medi_lists, medi_checks
                        ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(getActivity(), "medi_list");
                        ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(getActivity(), "medi_check");
                        medi_lists.add(edittext.getText().toString());
                        medi_checks.add(Boolean.FALSE);
                        m_PreferenceManager.setStringArrayList(getActivity(), "medi_list", medi_lists);
                        m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);

                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox cbx = (CheckBox) v;
                                if (cbx.isChecked()) {
                                    cbx.setPaintFlags(cbx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(getActivity(), "medi_list");
                                    ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(getActivity(), "medi_check");
                                    medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.TRUE);
                                    m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);
                                }
                                else {
                                    cbx.setPaintFlags(0);
                                    ArrayList<String> medi_lists = m_PreferenceManager.getStringArrayList(getActivity(), "medi_list");
                                    ArrayList<Boolean> medi_checks = m_PreferenceManager.getBoolArrayList(getActivity(), "medi_check");
                                    medi_checks.set(medi_lists.indexOf(cbx.getText().toString()), Boolean.FALSE);
                                    m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);
                                }
                            }
                        });

                        cb.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                                adg.setTitle("삭제").setMessage("이 약을 삭제하시겠습니까?");

                                adg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // do nothing
                                    }
                                });

                                adg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int index = medi_lists.indexOf(cb.getText().toString());
                                        medi_lists.remove(index);
                                        medi_checks.remove(index);
                                        m_PreferenceManager.setStringArrayList(getActivity(), "medi_list", medi_lists);
                                        m_PreferenceManager.setBoolArrayList(getActivity(), "medi_check", medi_checks);
                                        layout_home_checklist.removeView(cb);
                                        Toast.makeText(getActivity(), "삭제가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                    }
                                });

                                adg.show();
                                return false;
                            }
                        });


                        layout_home_checklist.addView(cb);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing to do
                    }
                });

                builder.show();

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
}