package com.youngdred.brujowines.ui.reservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.youngdred.brujowines.MainActivity;
import com.youngdred.brujowines.Reserva;
import com.youngdred.brujowines.databinding.FragmentReservasBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ReservasFragment extends Fragment {


    private FragmentReservasBinding binding;

    EditText personasEt, fechaEt, tiempoEt;
    ToggleButton visitaCataButton;
    Button reservaButton;

    private FirebaseUser usuario;
    private DatabaseReference reference;

    private String userId;

    private int mYear, mMonth, mDay;

    private int mHour, mMinute=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fechaEt=binding.etFechaReservaDate;
        fechaEt.setOnClickListener(v -> sacarCalendario());

        tiempoEt=binding.etFechaReservaTiempo;
        tiempoEt.setOnClickListener(view -> sacarReloj());

        reservaButton=binding.btnReservar;
        reservaButton.setOnClickListener(view -> realizarReserva());

        return root;
    }

    public void sacarReloj(){
        final Calendar calendar = Calendar.getInstance ();

        final TimePickerDialog timePickerDialog = new TimePickerDialog ( getActivity(), new TimePickerDialog.OnTimeSetListener () {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tiempoEt.setText (hourOfDay + ":00");
            }
        }, mHour, 0, true );

        timePickerDialog.show ();
    }

    public void sacarCalendario(){

        final Calendar calendar = Calendar.getInstance ();
        mYear = calendar.get ( Calendar.YEAR );
        mMonth = calendar.get ( Calendar.MONTH );
        mDay = calendar.get ( Calendar.DAY_OF_MONTH );

        //show dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog ( getActivity(), new DatePickerDialog.OnDateSetListener () {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaEt.setText (new StringBuilder().append(dayOfMonth).append("/").append(month + 1).append("/").append(year).toString());
            }
        }, mYear, mMonth, mDay );
        datePickerDialog.show ();
    }


    private void realizarReserva(){

        personasEt=binding.etNumeroPersonasNumber;
        int numeroPersonas=Integer.parseInt(personasEt.getText().toString().trim());

        if(numeroPersonas<0||numeroPersonas>10){
            personasEt.setError("El numero tiene que estar entre 1 y 10");
            personasEt.requestFocus();
            numeroPersonas=0;
            return;
        }


        visitaCataButton=binding.toggleButtonCataVisita;
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

                if(hour<9||hour>18){
                    tiempoEt.setError("El horario es de 9:00 a 18:00");
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

        Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), MainActivity.class));

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}