package com.example.slowvf.View.ui.reglages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.slowvf.databinding.FragmentReglagesBinding;


public class ReglagesFragment extends Fragment {

    private FragmentReglagesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReglagesViewModel reglagesViewModel =
                new ViewModelProvider(this).get(ReglagesViewModel.class);

        binding = FragmentReglagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReglages;
        reglagesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}