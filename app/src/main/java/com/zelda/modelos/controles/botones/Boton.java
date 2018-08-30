package com.zelda.modelos.controles.botones;

import android.content.Context;

import com.zelda.modelos.Modelo;

/**
 * Created by carlos on 13/12/17.
 */

public abstract class Boton extends Modelo {

    public Boton(Context context, double x, double y, int altura, int ancho) {
        super(context, x , y , altura, ancho);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }
}
