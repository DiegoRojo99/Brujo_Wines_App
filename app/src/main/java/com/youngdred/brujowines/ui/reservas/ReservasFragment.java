package com.youngdred.brujowines.ui.reservas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youngdred.brujowines.ReservarActivity;
import com.youngdred.brujowines.databinding.FragmentReservasBinding;

public class ReservasFragment extends Fragment {


    private FragmentReservasBinding binding;

    private TextView visitaBodegaTV;
    private Button visitaBodegaButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        visitaBodegaTV = (TextView) binding.includeVisitaBodega.tvVisitaBodegaDesc;


        visitaBodegaButton = binding.includeVisitaBodega.btnVisitaBodega;
        visitaBodegaButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), ReservarActivity.class)));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}