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
import com.youngdred.brujowines.TitaniaRosadoActivity;
import com.youngdred.brujowines.TitaniaTintoActivity;
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
                getActivity().startActivity(new Intent(getActivity(), TitaniaTintoActivity.class));
                break;
            case "blanco":
                getActivity().startActivity(new Intent(getActivity(), TitaniaBlancoActivity.class));
                break;
            case "rosado":
                getActivity().startActivity(new Intent(getActivity(), TitaniaRosadoActivity.class));
                break;
        }
    }

}