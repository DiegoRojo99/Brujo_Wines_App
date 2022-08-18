package com.youngdred.brujowines;

public class Pedido {
    public int blanco;
    public int tinto;
    public int rosado;
    public int precio;
    public Pedido() {
    }
    public Pedido(int blanco, int tinto, int rosado, int precio) {
        this.blanco = blanco;
        this.tinto=tinto;
        this.rosado = rosado;
        this.precio = precio;
    }
}
