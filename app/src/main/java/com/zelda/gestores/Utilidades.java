package com.zelda.gestores;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Jordan on 23/08/2015.
 */
public class Utilidades {

    public static double proximoACero(double a, double b) {
        if (Math.pow(a,2) <  Math.pow(b,2))
            return a;
        else
            return b;
    }

    public static final int ARRIBA = 1;
    public static final int DERECHA = 2;
    public static final int ABAJO = 3;
    public static final int IZQUIERDA = 4;

    public static int dameOrientacionAlAzar(){
        //Generamos un numero al azar entre 1 y 4
        int numAzar =  1 + (int) Math.floor(Math.random() * 3);
        return numAzar;
    }

    public static final int RUPIA = 5;
    public static final int CORAZON = 6;
    public static final int INMUNIDAD = 7;
    public static final int NINGUNO = 8;

    public static int damePowerUpAlAzar(){
        /*
            La importancia de los powerups es la siguiente:
            1.Inmunidad - Probabilidad = 10%
            2.Corazon   - Probabilidad = 15%
            3.Rupia     - Probabilidad = 25%

            Ninguno     - Probabilidad = 50%
         */

        //Generamos un numero al azar entre 0.01 y 1
        double numAzar = Math.random() + 0.01;
        //Solo nos interesan los 2 primeros decimales, así que quitamos todos los demas (sin redondear9
        numAzar = new BigDecimal(numAzar).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        Log.v("Powerups","Valor powerup azar: " + numAzar);

        if (numAzar >= .01 && numAzar <= .50){//50%
            Log.v("Powerups","Ninguno");
            return NINGUNO;
        }else if (numAzar >= .51 && numAzar <= .75){//25%
            Log.v("Powerups","Rupia");
            return RUPIA;
        }else if (numAzar >= .76 && numAzar <= .90){//15%
            Log.v("Powerups","Corazon");
            return  CORAZON;
        }//else if (numAzar >= .91 && numAzar <= 1)
        else{
            Log.v("Powerups","Inmunidad");
            return INMUNIDAD;
        }
    }



}
