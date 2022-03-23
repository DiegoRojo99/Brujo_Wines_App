package com.youngdred.brujowines;

import java.util.Date;

public class Reserva {
    public Date fechaReserva;
    public int numeroPersonas;
    public boolean tipo;
    //False sera visita a la bodega y True sera cata de vinos
    public Reserva() {
    }
    public Reserva(Date fechaReserva,int numeroPersonas, boolean tipo) {
        this.fechaReserva = fechaReserva;
        this.numeroPersonas=numeroPersonas;
        this.tipo = tipo;
    }
}
