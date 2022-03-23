package com.youngdred.brujowines;

import java.util.Date;

public class Reserva {
    String email;
    Date fechaReserva;
    int numeroPersonas;
    boolean tipo;
    //False sera visita a la bodega y True sera cata de vinos
    public Reserva() {
    }
    public Reserva(String email, Date fechaReserva,int numeroPersonas, boolean tipo) {
        this.email = email;
        this.fechaReserva = fechaReserva;
        this.numeroPersonas=numeroPersonas;
        this.tipo = tipo;
    }
}
