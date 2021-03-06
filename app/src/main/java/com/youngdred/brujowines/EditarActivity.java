package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditarActivity extends AppCompatActivity {

    EditText personasEt, fechaEt, tiempoEt;
    Button editarBtn;
    ToggleButton tipoReservaBtn;

    TextView tipoReservaActual, fechaReservaActual, numeroPersonasActual;

    String reservaID;
    Reserva resActual;

    private int mYear, mMonth, mDay;
    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Intent reservaIntent = getIntent();
        reservaID = reservaIntent.getStringExtra("ReservaID");

        tipoReservaActual=findViewById(R.id.tv_editar_tipo_reserva_actual_info);
        fechaReservaActual=findViewById(R.id.tv_editar_fecha_reserva_actual_info);
        numeroPersonasActual=findViewById(R.id.tv_editar_numero_personas_actual_info);

        fechaEt=findViewById(R.id.et_editar_fecha_reserva_date);
        fechaEt.setOnClickListener(v -> sacarCalendario());

        personasEt=findViewById(R.id.et_editar_numero_personas);
        tipoReservaBtn=findViewById(R.id.editar_toggleButtonCataVisita);

        tiempoEt=findViewById(R.id.et_editar_fecha_reserva_tiempo);
        tiempoEt.setOnClickListener(view -> sacarReloj());

        editarBtn=findViewById(R.id.editar_editar_btn);
        editarBtn.setOnClickListener(view -> editarReserva());

        getDetallesReserva();
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
                                    resActual=res;
                                    actualizarReservaActual(document.getId(),res, tipo);

                                }

                            }
                        }
                    }
                });
    }
    public void actualizarReservaActual(String docID,Reserva reserva, boolean tipo){
        if (tipo) {
            tipoReservaActual.setText(R.string.cata_de_vinos);
        } else {
            tipoReservaActual.setText(R.string.visita_a_la_bodega);
        }
        Format format = new SimpleDateFormat("E dd-LL-yyyy HH:mm zzz", Locale.getDefault());
        String fechaString= format.format(reserva.fechaReserva);
        fechaReservaActual.setText(fechaString);
        String numeroPersonas = String.valueOf(reserva.numeroPersonas);
        numeroPersonasActual.setText(numeroPersonas);
        reservaID=docID;

    }

    public void getNuevosDatos(){

        // Numero Personas
        int numeroPersonas=Integer.parseInt(personasEt.getText().toString().trim());
        if(numeroPersonas<0||numeroPersonas>10){
            personasEt.setError("El numero tiene que estar entre 1 y 10");
            personasEt.requestFocus();
            numeroPersonas=0;
            return;
        }
        resActual.numeroPersonas=numeroPersonas;

        //Tipo
        Boolean cataReserva=tipoReservaBtn.isChecked();
        resActual.tipo=cataReserva;

        //Fecha Reserva
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

            resActual.fechaReserva=dateObject;

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }
    public void editarReserva(){

        getNuevosDatos();

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference reservaRef = db.collection("reservas").document(reservaID);
        reservaRef
                .update(
                        "FechaReserva", resActual.fechaReserva,
                        "NumeroPersonas", resActual.numeroPersonas,
                        "Tipo", resActual.tipo
                );

        goHome();
    }

    public void goHome(){
        Intent homeIntent= new Intent(EditarActivity.this, MainActivity.class);
        startActivity(homeIntent);
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


}