package com.zelda.modelos.recolectables;

import android.content.Context;

import com.zelda.modelos.Modelo;
import com.zelda.modelos.Nivel;

/**
 * Created by carlos on 23/10/17.
 */

public abstract class Recolectable extends Modelo {

    public Recolectable(Context context, double x, double y, int altura, int ancho) {
        super(context, x, y, altura, ancho);
    }

    public abstract void accionRecolectable(Nivel nivel) throws Exception;
    public abstract void actualizar (long tiempo);

}