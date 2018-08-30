package com.zelda.modelos.controles.botones;

import android.content.Context;

import com.zelda.GameView;
import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;

/**
 * Created by UO250707 on 26/10/2017.
 */

public class BotonDisparar extends Boton {

    public BotonDisparar(Context context) {
        super(context, GameView.pantallaAncho*0.90 , GameView.pantallaAlto*0.6, 70, 70);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.boton_atacar_magia);
    }

}
