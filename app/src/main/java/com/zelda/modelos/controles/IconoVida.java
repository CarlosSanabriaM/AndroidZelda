package com.zelda.modelos.controles;

import android.content.Context;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.modelos.Modelo;

/**
 * Created by carlos on 7/11/17.
 */

public class IconoVida extends Modelo {

    public IconoVida(Context context, double x, double y) {
        super(context, x, y, 16,16);//8,7
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.vida);
    }
}
