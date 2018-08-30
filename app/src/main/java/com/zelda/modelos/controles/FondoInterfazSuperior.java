package com.zelda.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zelda.GameView;
import com.zelda.modelos.Modelo;
import com.zelda.modelos.Nivel;

/**
 * Created by carlos on 13/12/17.
 */

public class FondoInterfazSuperior extends Modelo {

    public FondoInterfazSuperior(Context context) {
        super(context, 0, 0, 0, GameView.pantallaAncho);
    }

    @Override
    public void dibujar(Canvas canvas) {
        Paint linea = new Paint();
        linea.setARGB(200,50,50,50);
        linea.setStrokeWidth(90);
        canvas.drawLine((int) x-2, (int) y- 5, (int) x+ancho+2, (int) y- 5, linea);
    }


}
