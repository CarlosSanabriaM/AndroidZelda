package com.zelda.modelos.enemigos;

import android.content.Context;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.global.Estados;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Nivel;
import com.zelda.modelos.disparos.Disparo;
import com.zelda.modelos.disparos.DisparoOctorokRed;

/**
 * Created by jordansoy on 19/09/2017.
 */

public class Enemigo_octorok_red extends Enemigo {

    private static final int VIDA = 2;
    private static final double VELOCIDAD = 2;
    private static final double TIEMPO_CAMBIAR_VELOCIDAD = 1500;

    private static final int CADENCIA_DISPARO = 3000;
    private long milisegundosDisparo = 0;

    public static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_ABAJO = "Caminando_abajo";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";

    public Enemigo_octorok_red(Context context, Nivel nivel, double x, double y) {
        super(context, nivel, x, y, 32, 32, VIDA, VELOCIDAD, TIEMPO_CAMBIAR_VELOCIDAD);
        inicializar();
    }

    public void inicializar (){

        Sprite caminandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.octorok_red_arriba),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_ARRIBA, caminandoArriba);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.octorok_red_derecha),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.octorok_red_abajo),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_ABAJO, caminandoAbajo);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.octorok_red_izquierda),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        sprite = caminandoDerecha;
    }

    @Override
    public Disparo disparar(long milisegundos) {
        //Añadimos cierta aleatoriedad a los disparos (disparan cada [CADENCIA_DISPARO, 2*CADENCIA_DISPARO] ms
        if (milisegundos - milisegundosDisparo > CADENCIA_DISPARO + Math.random()* CADENCIA_DISPARO) {
            milisegundosDisparo = milisegundos;
            return new DisparoOctorokRed(context, x, y, orientacion);
        }
        return null;
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
