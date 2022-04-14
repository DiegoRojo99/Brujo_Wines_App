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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.youngdred.brujowines.R;
import com.youngdred.brujowines.Reserva;
import com.youngdred.brujowines.ReservarActivity;
import com.youngdred.brujowines.databinding.FragmentReservasBinding;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservasFragment extends Fragment {


    private FragmentReservasBinding binding;

    private int indexCata=0, indexVisita=0;

    private TextView reservaFecha1, reservaPersonas1, tipo1;
    private TextView reservaFecha2, reservaPersonas2, tipo2;
    private Button visitaBodegaButton, cambiarReservaCataBtn, cambiarReservaVisitaBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reservaFecha1 = (TextView) binding.includeReservaCata.tvReservaCartaFechaInfo;
        reservaPersonas1 = (TextView) binding.includeReservaCata.tvReservaCartaPersonasInfo;
        tipo1 = (TextView) binding.includeReservaCata.tvCartaReservaTipo;
        cambiarReservaCataBtn = (Button) binding.includeReservaCata.verOtraReservaButton;

        reservaFecha2 = (TextView) binding.includeReservaVisita.tvReservaCartaFechaInfo;
        reservaPersonas2 = (TextView) binding.includeReservaVisita.tvReservaCartaPersonasInfo;
        tipo2 = (TextView) binding.includeReservaVisita.tvCartaReservaTipo;
        cambiarReservaVisitaBtn = (Button) binding.includeReservaVisita.verOtraReservaButton;

        tipo1.setText(R.string.carta_reserva_cata);
        tipo2.setText(R.string.carta_reserva_visita);

        visitaBodegaButton = binding.btnReservasReservarAhora;
        visitaBodegaButton.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), ReservarActivity.class)));


        cambiarReservaVisitaBtn.setOnClickListener(view -> cambiarVisita());

        cambiarReservaCataBtn.setOnClickListener(view -> cambiarCata());

        cargarReservas();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void cambiarVisita(){

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("reservas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total=0; boolean found=false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));

                                if(firebaseUser.getUid().equals(document.get("UserId")) && !tipo){
                                    if(total == indexVisita&&!found){
                                        int numeroPersonas=Integer.parseInt(String.valueOf(document.get("NumeroPersonas")));
                                        Date fechaReserva=document.getDate("FechaReserva");
                                        Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                        actualizarReserva(res, tipo);
                                        indexVisita++;
                                        total++;
                                        found=true;
                                    }else{
                                        total++;
                                    }
                                }

                            }

                            if(total==indexVisita){
                                indexVisita=0;
                            }
                        }
                    }
                });


    }
    public void cambiarCata(){

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("reservas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total=0; boolean found=false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));

                                if(firebaseUser.getUid().equals(document.get("UserId")) && tipo){
                                    if(total == indexCata&&!found){
                                        int numeroPersonas=Integer.parseInt(String.valueOf(document.get("NumeroPersonas")));
                                        Date fechaReserva=document.getDate("FechaReserva");
                                        Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                        actualizarReserva(res, tipo);
                                        indexCata++;
                                        total++;
                                        found=true;
                                    }else{
                                        total++;
                                    }
                                }

                            }

                            if(total==indexCata){
                                indexCata=0;
                            }
                        }
                    }
                });

    }

    public void actualizarReserva(Reserva reserva, boolean tipo){
        if (tipo) {
            //new Locale("ES") si el default no funciona
            Format format = new SimpleDateFormat("E dd-LL-yyyy HH:mm zzz", Locale.getDefault());
            String fechaString1 = format.format(reserva.fechaReserva);
            reservaFecha1.setText(fechaString1);
            String numeroPersonas1 = String.valueOf(reserva.numeroPersonas);
            reservaPersonas1.setText(numeroPersonas1);
        } else {
            //new Locale("ES") si el default no funciona
            Format format2 = new SimpleDateFormat("E dd-LL-yyyy HH:mm zzz", Locale.getDefault());
            String fechaString2 = format2.format(reserva.fechaReserva);
            reservaFecha2.setText(fechaString2);
            String numeroPersonas2 = String.valueOf(reserva.numeroPersonas);
            reservaPersonas2.setText(numeroPersonas2);
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(firebaseUser.getUid().equals(document.get("UserId"))){
                                    boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));
                                    int numeroPersonas=Integer.parseInt(String.valueOf(document.get("NumeroPersonas")));
                                    Date fechaReserva=document.getDate("FechaReserva");
                                    Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                    actualizarReserva(res, tipo);
                                }

                            }
                        }
                    }
                });
    }
}