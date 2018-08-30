package com.zelda.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zelda.modelos.Modelo;
import com.zelda.modelos.Nivel;

/**
 * Created by carlos on 24/10/17.
 */

public class BarraEnemigo extends Modelo {

    private int valorMaximo;
    private int valorActual;

    public BarraEnemigo(Context context, double x, double y, int ancho,
                        int valorMaximo, int valorActual) {
        super(context, x, y, 0, 0);//el ancho y el alto que le pasemos da igual, porque vamos a sobrescribir el dibujar
        this.valorActual = valorActual;
        this.valorMaximo = valorMaximo;
        this.ancho=ancho;
    }

    @Override
    public void dibujar(Canvas canvas) {
        Paint linea = new Paint();
        linea.setColor(Color.BLACK);
        linea.setStrokeWidth(5);
        canvas.drawLine((int) (x - Nivel.scrollEjeX)-2, (int) (y - Nivel.scrollEjeY) - 5, (int) (x - Nivel.scrollEjeX)+ancho+2, (int) (y - Nivel.scrollEjeY) - 5, linea);

        linea.setColor(Color.RED);
        linea.setStrokeWidth(3);
        canvas.drawLine((int)(x - Nivel.scrollEjeX), (int) (y - Nivel.scrollEjeY) - 5, (int) (x - Nivel.scrollEjeX)+ancho, (int) (y - Nivel.scrollEjeY) - 5, linea);

        linea.setColor(Color.GREEN);
        linea.setStrokeWidth(3);

        if(valorActual==valorMaximo)
            canvas.drawLine((int)(x - Nivel.scrollEjeX), (int) (y - Nivel.scrollEjeY) - 5, (int) (x - Nivel.scrollEjeX)+ancho, (int) (y - Nivel.scrollEjeY) - 5, linea);
        else
            canvas.drawLine((int)(x - Nivel.scrollEjeX), (int) (y - Nivel.scrollEjeY) - 5, (int) (x - Nivel.scrollEjeX)+((ancho/valorMaximo)*valorActual), (int) (y - Nivel.scrollEjeY) - 5, linea);
    }

    public int getValorActual() {
        return valorActual;
    }

    public void setValorActual(int valorActual) {
        this.valorActual = valorActual;
    }

}

