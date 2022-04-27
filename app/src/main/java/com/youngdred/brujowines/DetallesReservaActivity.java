package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetallesReservaActivity extends AppCompatActivity {


    private int indexCata=0, indexVisita=0;
    private String reservaID="", TAG="TAG_DIEGO";

    public TextView tipoReservaTV, fechaReservaTV, numeroPersonasTV;
    public Button cancelarBtn, verOtraReservaBtn, editarReservaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        Intent reservaIntent = getIntent();
        reservaID = reservaIntent.getStringExtra("ReservaID");

        tipoReservaTV=findViewById(R.id.tv_detalles_tipo_reserva_info);
        numeroPersonasTV=findViewById(R.id.tv_detalles_personas_info);
        fechaReservaTV=findViewById(R.id.tv_detalles_fecha_reserva_info);

        cancelarBtn=findViewById(R.id.detalles_cancelar_reserva_button);
        cancelarBtn.setOnClickListener(view -> cancelarReserva());

        getDetallesReserva();
    }



    private void cancelarReserva(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("reservas").document(reservaID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG_DELETE_VISITA", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG_DELETE_VISITA", "Error deleting document", e);
                    }
                });
    }


    public void getDetallesReserva(){

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("reservas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(reservaID)){

                                    boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));
                                    int numeroPersonas=Integer.parseInt(String.valueOf(document.get("NumeroPersonas")));
                                    Date fechaReserva=document.getDate("FechaReserva");
                                    Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                    actualizarReserva(document.getId(),res, tipo);

                                }

                            }
                        }
                    }
                });
    }
    public void actualizarReserva(String docID,Reserva reserva, boolean tipo){
        if (tipo) {
            tipoReservaTV.setText(R.string.cata_de_vinos);
        } else {
            tipoReservaTV.setText(R.string.visita_a_la_bodega);
        }
        Format format = new SimpleDateFormat("E dd-LL-yyyy HH:mm zzz", Locale.getDefault());
        String fechaString= format.format(reserva.fechaReserva);
        fechaReservaTV.setText(fechaString);
        String numeroPersonas = String.valueOf(reserva.numeroPersonas);
        numeroPersonasTV.setText(numeroPersonas);
        reservaID=docID;

    }

    public void cargarReservas(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
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
                                    actualizarReserva(document.getId(),res, tipo);
                                }

                            }
                        }
                    }
                });
    }
}