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

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView text_home_date;
    private Button btn_home_add;
    private Button btn_home_del;
    private LinearLayout layout_home_checklist;

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

        // custom
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        String getTime = sdf.format(date);
        text_home_date.setText(getTime);

        // custom2
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

                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox cbx = (CheckBox) v;
                                if (cbx.isChecked()) {
                                    cbx.setPaintFlags(cbx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                                else {
                                    cbx.setPaintFlags(0);
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