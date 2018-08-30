package com.zelda.gestores;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by jordansoy on 03/10/2017.
 */

public class GestorAudio implements MediaPlayer.OnPreparedListener {
    public static final int SONIDO_LINK_ATACAR_ESPADA           = 1;
    public static final int SONIDO_LINK_ATACAR_MAGIA            = 2;
    public static final int SONIDO_LINK_GOLPEADO                = 3;
    public static final int SONIDO_LINK_MURIENDO                = 4;
    public static final int SONIDO_ENEMIGO_GOLPEADO             = 5;
    public static final int SONIDO_ENEMIGO_MURIENDO             = 6;
    public static final int SONIDO_CORAZON_RECOGIDO             = 7;
    public static final int SONIDO_RUPIA_RECOGIDA               = 8;
    public static final int SONIDO_ITEM_RECOGIDO                = 9;
    public static final int SONIDO_LLAVE_APARECE                = 10;
    public static final int SONIDO_PUERTA_DESBLOQUEADA          = 11;
    public static final int SONIDO_LINK_ENTRANDO_PUERTA         = 12;
    public static final int SONIDO_LINK_RECOGIENDO_TRIFUERZA    = 13;
    public static final int SONIDO_LINK_RECOGIENDO_LLAVE        = 14;

    private static final float VOLUMEN_MUSICA_FONDO = 0.3f;

    // Pool de sonidos, para efectos de sonido.
    // Suele fallar el utilizar ficheros de sonido demasiado grandes
    private SoundPool poolSonidos;
    private HashMap<Integer, Integer> mapSonidos;
    private Context contexto;
    // Media Player para bucle de sonido de fondo.
    private MediaPlayer sonidoAmbiente;
    private AudioManager gestorAudio;

    private static GestorAudio instancia = null;

    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    public static GestorAudio getInstancia(Context contexto,
                                           int idMusicaAmbiente) {
        synchronized (GestorAudio.class) {
            if (instancia == null) {
                instancia = new GestorAudio();
                instancia.initSounds(contexto, idMusicaAmbiente);
            }
            return instancia;
        }
    }

    public void initSounds(Context contexto, int idMusicaAmbiente) {
        this.contexto = contexto;
        poolSonidos = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mapSonidos = new HashMap<Integer, Integer>();
        gestorAudio = (AudioManager) contexto
                .getSystemService(Context.AUDIO_SERVICE);


        sonidoAmbiente = MediaPlayer.create(contexto, idMusicaAmbiente);
        sonidoAmbiente.setLooping(true);
        sonidoAmbiente.setVolume(VOLUMEN_MUSICA_FONDO, VOLUMEN_MUSICA_FONDO);
    }

    public void registrarSonido(int index, int SoundID) {
        mapSonidos.put(index, poolSonidos.load(contexto, SoundID, 1));
    }

    public void reproducirSonido(int index) {
        float volumen =
                gestorAudio.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumen =
                volumen / gestorAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        poolSonidos.play(
                (Integer) mapSonidos.get(index),
                volumen, volumen, 1, 0, 1f);
    }


    public void reproducirMusicaAmbiente() {
        try {
            if (!sonidoAmbiente.isPlaying()) {
                try {
                    sonidoAmbiente.setOnPreparedListener(this);
                    sonidoAmbiente.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pararMusicaAmbiente() {
        if (sonidoAmbiente.isPlaying()) {
            sonidoAmbiente.stop();
        }
    }

    public static GestorAudio getInstancia() {
        return instancia;
    }

    private GestorAudio() {
    }

}
