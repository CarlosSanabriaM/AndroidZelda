package com.zelda.gestores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class CargadorGraficos {

    private static HashMap<Integer, Drawable> drawables = new HashMap<Integer, Drawable>();
    private static HashMap<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();

    public static Drawable cargarDrawable(Context context, int id){
        if (drawables.containsKey(id)){
            return drawables.get(id);
        }

        Drawable nuevoDrawable = context.getResources().getDrawable(id);
        drawables.put(id, nuevoDrawable);
        return nuevoDrawable;
    }
	
    public static Bitmap cargarBitmap(Context context,int id)
    {
        if (bitmaps.containsKey(id)){
            return bitmaps.get(id);
        }

        Bitmap nuevoBitmap = ((BitmapDrawable)context.getResources().getDrawable(id)).getBitmap();
        bitmaps.put(id,nuevoBitmap);
        return nuevoBitmap;
    }

}
