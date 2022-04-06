package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReservarActivity extends AppCompatActivity {

    EditText personasEt, fechaEt, tiempoEt;
    ToggleButton visitaCataButton;
    Button reservaButton;

    private FirebaseUser usuario;
    private DatabaseReference reference;

    private String userId;

    private int mYear, mMonth, mDay;

    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        fechaEt=(EditText) findViewById(R.id.et_fecha_reserva_date);
        fechaEt.setOnClickListener(v -> sacarCalendario());

        tiempoEt=(EditText)findViewById(R.id.et_fecha_reserva_tiempo);
        tiempoEt.setOnClickListener(view -> sacarReloj());

        reservaButton=(Button) findViewById(R.id.btn_reservar);
        reservaButton.setOnClickListener(view -> realizarReserva());

    }

    public void sacarReloj(){
        final Calendar calendar = Calendar.getInstance ();

        final TimePickerDialog timePickerDialog = new TimePickerDialog ( this, new TimePickerDialog.OnTimeSetListener () {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tiempoEt.setText (hourOfDay + ":" + minute);
            }
        }, mHour, mMinute, true );

        timePickerDialog.show ();
    }

    public void sacarCalendario(){

        final Calendar calendar = Calendar.getInstance ();
        mYear = calendar.get ( Calendar.YEAR );
        mMonth = calendar.get ( Calendar.MONTH );
        mDay = calendar.get ( Calendar.DAY_OF_MONTH );

        //show dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog ( this, new DatePickerDialog.OnDateSetListener () {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaEt.setText (new StringBuilder().append(dayOfMonth).append("/").append(month + 1).append("/").append(year).toString());
            }
        }, mYear, mMonth, mDay );
        datePickerDialog.show ();
    }


    private void realizarReserva(){

        personasEt=(EditText) findViewById(R.id.et_numero_personas_number);
        int numeroPersonas=Integer.parseInt(personasEt.getText().toString().trim());

        if(numeroPersonas<0||numeroPersonas>10){
            personasEt.setError("El numero tiene que estar entre 1 y 10");
            personasEt.requestFocus();
            numeroPersonas=0;
            return;
        }


        visitaCataButton=(ToggleButton) findViewById(R.id.toggleButtonCataVisita);
        Boolean cataReserva=visitaCataButton.isChecked();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        Date dateObject;
        Reserva res= new Reserva(null,numeroPersonas,cataReserva);
        try{

            String dob_var=(fechaEt.getText().toString().trim());
            String hourString=(tiempoEt.getText().toString().trim());

            dateObject = formatter.parse(dob_var);

            if(dob_var.isEmpty()){
                fechaEt.setError("Introduce una fecha");
                fechaEt.requestFocus();
                return;
            }
            if(hourString.isEmpty()){
                tiempoEt.setError("Introduce una hora");
                tiempoEt.requestFocus();
                return;
            }


            try {

                int index=hourString.indexOf(":");
                int hour=Integer.parseInt(hourString.substring(0,index));
                int minutes=Integer.parseInt(hourString.substring(index+1,hourString.length()));

                if(hour<9||hour>19){
                    tiempoEt.setError("El horario es de 9:00 a 18:30");
                    tiempoEt.requestFocus();
                    return;
                } else if(hour==18&&minutes>30){
                    tiempoEt.setError("El horario es de 9:00 a 18:30");
                    tiempoEt.requestFocus();
                    return;
                }

                dateObject.setHours(hour);
                dateObject.setMinutes(minutes);

            }catch (Exception e){

            }

            res.fechaReserva=dateObject;

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        usuario= FirebaseAuth.getInstance().getCurrentUser();
        userId=usuario.getUid();
        Map<String,Object> reserva=new HashMap<>();
        reserva.put("FechaReserva",res.fechaReserva);
        reserva.put("Tipo",res.tipo);
        reserva.put("NumeroPersonas",res.numeroPersonas);
        reserva.put("UserId",userId);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("reservas")
                .add(reserva)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        startActivity(new Intent(ReservarActivity.this,MainActivity.class));

    }
}