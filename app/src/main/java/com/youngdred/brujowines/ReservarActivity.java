package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ReservarActivity extends AppCompatActivity {


    EditText personasEt, fechaEt;
    ToggleButton visitaCataButton;
    Button reservaButton;

    private FirebaseUser usuario;
    private DatabaseReference reference;
    private DatabaseReference reservasReference;

    private String userId;
    private String reservaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);


        reservaButton=(Button) findViewById(R.id.btn_reservar);
        reservaButton.setOnClickListener(view -> realizarReserva());



    }

    private void realizarReserva(){

        personasEt=(EditText) findViewById(R.id.et_numero_personas_number);
        int numeroPersonas=Integer.parseInt(personasEt.getText().toString());

        visitaCataButton=(ToggleButton) findViewById(R.id.toggleButtonCataVisita);
        Boolean cataReserva=true;

        //El Fallo puede ser el formato de la fecha

        /*
        fechaEt=(EditText) findViewById(R.id.et_fecha_reserva_date);
        Date fechaReserva= (Date) fechaEt.getText();

        Reserva res=new Reserva("",fechaReserva,numeroPersonas,cataReserva);

        usuario= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=usuario.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario perfilUsuario=snapshot.getValue(Usuario.class);
                if(perfilUsuario!=null){
                    res.email=perfilUsuario.email;
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservarActivity.this,"Algo ha salido mal",Toast.LENGTH_LONG).show();
            }
        });

        //TODO Guardar Reserva
        reference.child(userId).child("reservas").setValue(res);
         */
    }
}