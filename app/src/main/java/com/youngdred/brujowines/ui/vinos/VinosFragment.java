package com.youngdred.brujowines.ui.vinos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.youngdred.brujowines.TitaniaBlancoActivity;
import com.youngdred.brujowines.databinding.FragmentVinosBinding;

public class VinosFragment extends Fragment {

    private VinosViewModel vinosViewModel;
    private FragmentVinosBinding binding;

    private Button titaniaBlancoButton;
    private Button titaniaTintoButton;
    private Button titaniaRosadoButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vinosViewModel =
                new ViewModelProvider(this).get(VinosViewModel.class);

        binding = FragmentVinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        titaniaBlancoButton= binding.buttonVinosTitaniaBlanco;
        titaniaBlancoButton.setOnClickListener((View v) ->
                cambiarActividadTitania("blanco")
        );
        titaniaRosadoButton= binding.buttonVinosTitaniaRosado;
        titaniaRosadoButton.setOnClickListener((View v) ->
                cambiarActividadTitania("rosado")
        );
        titaniaTintoButton= binding.buttonVinosTitaniaTinto;
        titaniaTintoButton.setOnClickListener((View v) ->
                cambiarActividadTitania("tinto")
        );

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

    //Este metodo cambia a la actividad del vino correspondiente
    //TODO Falta añadir las actividades de tinto y rosado
    public void cambiarActividadTitania(String vino){
        switch (vino){
            case "tinto":
                break;
            case "blanco":
                getActivity().startActivity(new Intent(getActivity(), TitaniaBlancoActivity.class));
                break;
            case "rosado":
                break;
        }
    }
}