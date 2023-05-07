package com.example.slowvf.View.ui.carte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.slowvf.databinding.FragmentCarteBinding;

public class CarteFragment extends Fragment {

    private FragmentCarteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarteViewModel carteViewModel =
                new ViewModelProvider(this).get(CarteViewModel.class);

        binding = FragmentCarteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCarte;
        carteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}