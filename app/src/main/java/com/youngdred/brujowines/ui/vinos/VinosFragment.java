package com.youngdred.brujowines.ui.vinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.youngdred.brujowines.databinding.FragmentVinosBinding;

public class VinosFragment extends Fragment {

    private VinosViewModel vinosViewModel;
    private FragmentVinosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vinosViewModel =
                new ViewModelProvider(this).get(VinosViewModel.class);

        binding = FragmentVinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textVinos;
        vinosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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