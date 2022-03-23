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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservarActivity extends AppCompatActivity {

    TimePickerDialog timePickerDialog;

    EditText personasEt, fechaEt, tiempoEt;
    ToggleButton visitaCataButton;
    Button reservaButton;

    private FirebaseUser usuario;
    private DatabaseReference reference;
    private DatabaseReference reservasReference;

    private String userId;
    private String reservaId;

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
//am pm mode
//                    String AM_PM;
//                    if (hourOfDay>=0&&hourOfDay<12){
//                        AM_PM=" AM";
//                    }else {
//                        AM_PM=" PM";
//                    }
                    tiempoEt.setText ( hourOfDay + ":" + minute);
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
                fechaEt.setText ( dayOfMonth + "/" + (month + 1) + "/" + year );
            }
        }, mYear, mMonth, mDay );
        datePickerDialog.show ();
    }


    private void realizarReserva(){

        personasEt=(EditText) findViewById(R.id.et_numero_personas_number);
        int numeroPersonas=Integer.parseInt(personasEt.getText().toString());

        visitaCataButton=(ToggleButton) findViewById(R.id.toggleButtonCataVisita);
        Boolean cataReserva=visitaCataButton.isChecked();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formateoHora = new SimpleDateFormat("mm:hh");

        // Make sure user insert date into edittext in this format.
        Date dateObject;
        Reserva res= new Reserva(null,numeroPersonas,cataReserva);

        try{
            String dob_var=(fechaEt.getText().toString());
            String hourString=(tiempoEt.getText().toString());

            dateObject = formatter.parse(dob_var);
            int hour=Integer.parseInt(hourString.substring(0,2));
            int minutes=Integer.parseInt(hourString.substring(3,5));
            dateObject.setHours(hour);
            dateObject.setMinutes(minutes);
            res.fechaReserva=dateObject;

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        usuario= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=usuario.getUid();

        reference.child(userId).child("reservas").setValue(res);

        startActivity(new Intent(ReservarActivity.this,MainActivity.class));

    }
}