package com.zelda.modelos.recolectables;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.GestorAudio;
import com.zelda.modelos.Nivel;

/**
 * Created by carlos on 12/12/17.
 */

public class Trifuerza extends Recolectable {

    public Trifuerza(Context context, double x, double y) {
        super(context, x, y, 15, 15);//10,10
        imagen=context.getResources().getDrawable(R.drawable.triangulo_trifuerza);
        this.y =  y - altura/2;
    }

    @Override
    public void accionRecolectable(Nivel nivel) throws Exception {
        //El jugador se encargara de pasar al sgte nivel cuando se acabe la animacion de recogar la trifuerza
        nivel.jugador.recogiendoTrifuerza = true;
        nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LINK_RECOGIENDO_TRIFUERZA);
    }

    @Override
    public void actualizar(long tiempo) {
        //No hace nada
    }

    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);

    }
}
