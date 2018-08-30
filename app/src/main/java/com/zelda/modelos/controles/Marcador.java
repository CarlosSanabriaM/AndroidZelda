package com.zelda.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.modelos.Modelo;

/**
 * Created by jordansoy on 27/09/2017.
 */

public class Marcador extends Modelo {
    private int puntos;

    public Marcador(Context context, double x, double y) {
        super(context, x, y, 21, 24);//16,10
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.rupia_puntuacion);
    }

    @Override
    public void dibujar(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);

        //Ponemos la letra en Times New Roman y negrita
        Typeface bold = Typeface.create("Times New Roman", Typeface.BOLD);
        paint.setTypeface(bold);

        canvas.drawText(String.valueOf(puntos), (int)x, (int)y, paint);

        int yArriba = (int)  (y - altura / 2) - 7;
        int xIzquierda = (int) (x - ancho / 2) - 13;//- 9

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void reiniciar(){ this.puntos=0; }


}
