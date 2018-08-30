package com.zelda.modelos.enemigos;

import android.content.Context;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.global.Estados;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Nivel;

/**
 * Created by carlos on 23/10/17.
 */

public class Enemigo_goriya_blue extends Enemigo {

    private static final int VIDA = 4;
    private static final double VELOCIDAD = 4;
    private static final double TIEMPO_CAMBIAR_VELOCIDAD = 1000;

    public static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_ABAJO = "Caminando_abajo";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";

    public Enemigo_goriya_blue(Context context, Nivel nivel, double x, double y) {
        super(context, nivel, x, y, 32, 34, VIDA, VELOCIDAD, TIEMPO_CAMBIAR_VELOCIDAD);

        inicializar();
    }

    public void inicializar (){

        Sprite caminandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.goriya_blue_arriba),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_ARRIBA, caminandoArriba);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.goriya_blue_derecha),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.goriya_blue_abajo),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_ABAJO, caminandoAbajo);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.goriya_blue_izquierda),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        sprite = caminandoDerecha;
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        //Si el estado en el que estabas era muriendo y se acabo la animacion de morir, te pongo en el estado eliminar
        if ( estado == Estados.MURIENDO && finSprite == true){
            estado = Estados.ELIMINAR;
            //Se puede generar aleatoriamente una rupia, un corazon o un powerup de inmunidad en la pos en la que estaba el enemigo
            this.generarAleatoriamentePowerUp();
        }

        //Si esta vivo, comprobamos hacia que direccion camina
        if(estado == Estados.VIVO){
            if (velocidadX > 0){
                sprite = sprites.get(CAMINANDO_DERECHA);
                orientacion = DERECHA;
            }
            else if (velocidadX < 0 ){
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
                orientacion = IZQUIERDA;
            }
            else if(velocidadY > 0){
                sprite = sprites.get(CAMINANDO_ABAJO);
                orientacion = ABAJO;
            }
            else if (velocidadY < 0 ){
                sprite = sprites.get(CAMINANDO_ARRIBA);
                orientacion = ARRIBA;
            }
        }
        //Si esta muriendo le ponemos la animacion de morir
        else if(estado == Estados.MURIENDO){
            sprite = sprites.get(MURIENDO);
        }

    }


}
