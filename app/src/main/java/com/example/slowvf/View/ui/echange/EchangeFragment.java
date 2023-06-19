package com.example.slowvf.View.ui.echange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.slowvf.databinding.FragmentEchangeBinding;

public class EchangeFragment extends Fragment {

    private FragmentEchangeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EchangeViewModel echangeViewModel =
                new ViewModelProvider(this).get(EchangeViewModel.class);

        binding = FragmentEchangeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEchange;
        echangeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}