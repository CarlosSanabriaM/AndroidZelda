package com.zelda.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;

/**
 * Created by carlos on 8/11/17.
 */

public class Puerta extends Modelo {

    public int numeroPuerta;

    public Puerta(Context context, double x, double y, int numeroPuerta) {
        super(context, x, y, 34,32);
        this.y =  y - altura/2 + 1;//la bajamos un poco mas de lo normal para que el jugador no tenga problemas al colisionar con ella

        this.numeroPuerta=numeroPuerta;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.puerta);
    }

    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);

    }

    /**
     * Devuelve cierto si se encuentra la puerta asociada a esta puerta
     * y nos tenemos que salir del metodo aplicarReglasMovimiento
     */
    public boolean accionJugadorEntraPuerta(Nivel nivel){
        //Recorremos las puertas para buscar cual es la puerta asociada a la puerta actual
        for (Puerta puerta_asociada: nivel.puertas) {
            //Una puerta es la asociada a otra puerta si son puertas distintas y tienen el mismo numero
            if(puerta_asociada!=this && puerta_asociada.numeroPuerta==this.numeroPuerta){

                //Desplazamos al jugador a la posicion de dicha puerta
                //(debajo de la puerta para que no genere una serie de desplazamientos infinitos)
                nivel.jugador.x=puerta_asociada.x;
                nivel.jugador.y=puerta_asociada.y + puerta_asociada.altura/2 + nivel.jugador.altura/2 + 8;
                nivel.jugador.orientacion=Jugador.ABAJO;//El jugador pasa a mirar hacia abajo

                nivel.jugador.entrandoPuerta();

                nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LINK_ENTRANDO_PUERTA);

                return true;
            }
        }

        return false;
    }


}
