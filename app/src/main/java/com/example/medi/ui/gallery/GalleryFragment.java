package com.example.medi.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.medi.R;
import com.example.medi.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private TextView textcold;
    private TextView textdigestion;
    private TextView textremovefever;
    private TextView textpainkiller;
    private TextView textanti;
    private TextView textnutrient;
    private TextView textskin;
    private TextView textcream;
    private TextView textEENT;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textcold = root.findViewById(R.id.cold);
        textcold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("감기약").setMessage("항히스타민제, 비충혈 제거제인 슈도에페드린, 아세트아미노펜 성분이 들어갑니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textdigestion = root.findViewById(R.id.digestion);
        textdigestion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("소화기계약").setMessage("소화제, 위염약, 멀미약, 과민성 대장증후군약, 변비약, 지사제 등 소화불량을 막아주거나, 음식물이나 장 속으로 들어간 것들의 소화를 돕습니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textremovefever = root.findViewById(R.id.removefever);
        textremovefever.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("해열제").setMessage("체온이 발열로 인해 비정상적으로 높아졌을 때 열을 내리는 데 쓰입니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textpainkiller = root.findViewById(R.id.painkiller);
        textpainkiller.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("진통제").setMessage("질병이나 상해 또는 수술로 인하여 통증을 느낄 경우 그 통증을 경감시키기 위해 사용하며, 통증을 느끼는 신경의 작용을 둔하게 하여 두뇌에서 통증을 인지하지 못하게 합니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textanti = root.findViewById(R.id.anti);
        textanti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("소염제").setMessage(" 염증 또는 통증이 발생하는 과정에 관여하여 이를 억제하는 약물 제제 또는 염증을 치료하고 방지를 목적으로 합니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textnutrient = root.findViewById(R.id.nutrient);
        textnutrient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("영양제").setMessage("각종 영양소 성분을 배합하여 정제나 음료의 형태로 만들어 복용과 체내 흡수를 쉽게 한 영양을 보충해주는 의약품입니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textskin = root.findViewById(R.id.skin);
        textskin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("피부약").setMessage("습진, 아토피, 두드러기, 무좀, 사마귀, 피부염 등 각종 피부 질환을 치료하기 위하여 피부에 적용합니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textcream = root.findViewById(R.id.cream);
        textcream.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("연고류").setMessage("알레르기, 항바이러스, 항진균제, 화농성, 화상, 동상, 여드름, 상처보호 등에 사용되고, 피부약에 해당한다고 볼 수 있으며 쉽게 바를 수 있고 정착이 잘되어 피부의 상처나 질환을 치료할 때 자주 쓰입니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });

        textEENT = root.findViewById(R.id.EENT);
        textEENT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adg = new AlertDialog.Builder(getActivity());
                adg.setTitle("안과/이비인후과 및 치과약").setMessage("눈(결막염), 귀(중이염), 코(비염), 입(잇몸 질환 및 충치) 등에 사용됩니다.");
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                adg.show();
            }

        });
        // fab button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}