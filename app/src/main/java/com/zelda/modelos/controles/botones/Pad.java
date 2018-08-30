package com.zelda.modelos.controles.botones;

import android.content.Context;

import com.zelda.GameView;
import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;

/**
 * Created by UO250707 on 19/10/2017.
 */

public class Pad extends Boton {

    public Pad(Context context) {
        super(context, GameView.pantallaAncho*0.10, GameView.pantallaAlto*0.8, 100, 100);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
    }

    public int getOrientacionX(float cliclX) {
        return (int) (x - cliclX);
    }

    public int getOrientacionY(float cliclY) {
        return (int) (y - cliclY);
    }

}