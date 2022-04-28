package com.youngdred.brujowines;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditarActivity extends AppCompatActivity {

    EditText personasEt, fechaEt, tiempoEt;
    Button editarBtn;

    private int mYear, mMonth, mDay;
    private int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        fechaEt=findViewById(R.id.et_editar_fecha_reserva_date);
        fechaEt.setOnClickListener(v -> sacarCalendario());

        tiempoEt=findViewById(R.id.et_editar_fecha_reserva_tiempo);
        tiempoEt.setOnClickListener(view -> sacarReloj());

        editarBtn=findViewById(R.id.editar_editar_btn);
        editarBtn.setOnClickListener(view -> editarReserva());
    }

    public void editarReserva(){

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