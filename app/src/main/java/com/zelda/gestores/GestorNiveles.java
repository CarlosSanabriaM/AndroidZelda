package com.zelda.gestores;

/**
 * Created by jordansoy on 03/10/2017.
 */

public class GestorNiveles {
    private static GestorNiveles instancia = null;
    private int nivelActual = 0;
    public static final int NUMERO_MAX_NIVELES = 3;

    private GestorNiveles() {
    }

    public static GestorNiveles getInstancia() {
        synchronized (GestorNiveles.class) {
            if (instancia == null) {
                instancia = new GestorNiveles();
            }
            return instancia;
        }
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
    }

    public void siguienteNivel() {
        this.nivelActual++;
    }

}
