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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.youngdred.brujowines.Reserva;
import com.youngdred.brujowines.ReservarActivity;
import com.youngdred.brujowines.databinding.FragmentReservasBinding;

import java.util.Date;

public class ReservasFragment extends Fragment {


    private FragmentReservasBinding binding;

    private TextView reservaFecha1, reservaTipo1, reservaPersonas1;
    private TextView reservaFecha2, reservaTipo2, reservaPersonas2;
    private Button visitaBodegaButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reservaFecha1 = (TextView) binding.includeReserva1.tvReservaCartaFechaInfo;
        reservaTipo1 = (TextView) binding.includeReserva1.tvReservaCartaTipoInfo;
        reservaPersonas1 = (TextView) binding.includeReserva1.tvReservaCartaPersonasInfo;

        reservaFecha2 = (TextView) binding.includeReserva2.tvReservaCartaFechaInfo;
        reservaTipo2 = (TextView) binding.includeReserva2.tvReservaCartaTipoInfo;
        reservaPersonas2 = (TextView) binding.includeReserva2.tvReservaCartaPersonasInfo;

        visitaBodegaButton = binding.btnReservasReservarAhora;
        visitaBodegaButton.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), ReservarActivity.class)));

        cargarReservas();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void actualizarReserva(int index, Reserva reserva){
        switch (index){
            case 0:

                //reservaFecha1.setText(reserva.fechaReserva.toString());
                String numeroPersonas1=String.valueOf(reserva.numeroPersonas);
                reservaPersonas1.setText(numeroPersonas1);
                if(reserva.tipo){
                    reservaTipo1.setText("Cata de Vino");
                }else{
                    reservaTipo1.setText("Visita a la Bodega");
                }
                break;
            case 1:
                //reservaFecha2.setText(reserva.fechaReserva.toString());
                String numeroPersonas2=String.valueOf(reserva.numeroPersonas);
                reservaPersonas2.setText(numeroPersonas2);
                if(reserva.tipo){
                    reservaTipo2.setText("Cata de Vino");
                }else{
                    reservaTipo2.setText("Visita a la Bodega");
                }
                break;
        }
    }

    public void cargarReservas(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("reservas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int index=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(firebaseUser.getUid().equals(document.get("UserId"))){
                                    boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));
                                    int numeroPersonas=Integer.parseInt(String.valueOf(document.get("Numero Personas")));
                                    Date fechaReserva=null;
                                    Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                    actualizarReserva(index, res);
                                    index++;
                                }

                            }
                        }
                    }
                });
    }
}