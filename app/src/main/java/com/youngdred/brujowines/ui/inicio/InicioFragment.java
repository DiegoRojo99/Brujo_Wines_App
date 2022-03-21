package com.youngdred.brujowines.ui.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.youngdred.brujowines.MainActivity;
import com.youngdred.brujowines.TitaniaBlancoActivity;
import com.youngdred.brujowines.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;
    private FragmentInicioBinding binding;

    private ImageButton titaniaBlancoButton;
    private ImageButton titaniaRosadoButton;
    private ImageButton titaniaTintoButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        titaniaBlancoButton= binding.ibInicioTitaniaBlanco;
        titaniaBlancoButton.setOnClickListener((View v) ->
               cambiarActividadTitania("blanco")
        );
        titaniaRosadoButton= binding.ibInicioTitaniaRosado;
        titaniaRosadoButton.setOnClickListener((View v) ->
                cambiarActividadTitania("rosado")
        );
        titaniaTintoButton= binding.ibInicioTitaniaTinto;
        titaniaTintoButton.setOnClickListener((View v) ->
                cambiarActividadTitania("tinto")
        );

        final TextView textView = binding.textInicio;
        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        if (vino=="tinto"){

        }else if(vino=="blanco"){
            getActivity().startActivity(new Intent(getActivity(), TitaniaBlancoActivity.class));
        }else if(vino=="rosado"){

        }
    }

}