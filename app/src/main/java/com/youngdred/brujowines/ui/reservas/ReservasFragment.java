package com.youngdred.brujowines.ui.reservas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.youngdred.brujowines.DetallesReservaActivity;
import com.youngdred.brujowines.R;
import com.youngdred.brujowines.Reserva;
import com.youngdred.brujowines.ReservarActivity;
import com.youngdred.brujowines.TitaniaBlancoActivity;
import com.youngdred.brujowines.databinding.FragmentReservasBinding;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservasFragment extends Fragment {


    private FragmentReservasBinding binding;

    private int indexCata=0, indexVisita=0;
    private String cataActual="", visitaActual="";

    private TextView reservaFecha1, reservaPersonas1, tipo1;
    private TextView reservaFecha2, reservaPersonas2, tipo2;
    public Button visitaBodegaButton, cambiarReservaCataBtn, cambiarReservaVisitaBtn,
                    cancelarCataBtn, cancelarVisitaBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        visitaBodegaButton = binding.btnReservasReservarAhora;
        visitaBodegaButton.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), ReservarActivity.class)));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}