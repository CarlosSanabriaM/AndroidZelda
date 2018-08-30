package com.zelda.modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.zelda.GameView;

public class Fondo extends Modelo{

    Bitmap fondo;

    public Fondo(Context context, Bitmap imagen) {
        super(context,
                GameView.pantallaAncho/2,
                GameView.pantallaAlto/2,
                GameView.pantallaAlto,
                GameView.pantallaAncho );

        this.fondo = imagen;
    }

    public void dibujar(Canvas canvas) {
        int xIzquierda = (int) x - ancho / 2;

        Rect origen = new Rect(0,0 ,
                fondo.getWidth(),fondo.getHeight());

        Rect destino = new Rect((int) (x - ancho / 2),
                (int) (y - altura / 2),
                (int) (x + ancho / 2),
                (int) (y + altura / 2));

        canvas.drawBitmap(fondo,origen,destino,null);
    }

}

